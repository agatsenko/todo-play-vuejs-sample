/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-15
  */
package com.agat.todo.core.persistence.scalikejdbc.h2

import java.util.UUID

import scalikejdbc._

import com.agat.todo.core.infrastructure.persistence.PersistContext
import com.agat.todo.core.infrastructure.persistence.scalikejdbc.ScalikejdbcRepo
import com.agat.todo.core.model.{TodoList, TodoListRepo}
import com.agat.todo.core.persistence.scalikejdbc.h2.Mapping._

class ScalikejdbcH2TodoListRepo extends TodoListRepo with ScalikejdbcRepo {
  import ScalikejdbcH2TodoListRepo._
  import com.agat.todo.core.infrastructure.persistence.scalikejdbc.ExtBinders._

  override def getAll(implicit context: PersistContext): Seq[TodoList] = {
    withinSession { implicit session =>
      withSQL {
        select.from(TodoLists as lst)
      }.map(TodoLists(lst)).list().apply().map(populateTasks)
    }
  }

  override def getById(id: UUID)(implicit context: PersistContext): Option[TodoList] = {
    withinSession { implicit session =>
      withSQL {
        select.from(TodoLists as lst).where.eq(lst.id, id)
      }.map(TodoLists(lst)).single().apply().map(populateTasks)
    }
  }

  override def save(list: TodoList)(implicit context: PersistContext): TodoList = {
    withinSession { implicit session =>
      sql"""
        merge into ${TodoLists.table} (${lstClm.id}, ${lstClm.name})
        key (${lstClm.id})
        values (${list.id}, ${list.name})
      """.update().apply()

      if (list.tasks.isEmpty) {
        withSQL {
          delete.from(TodoTasks).where.eq(tskClm.column(TodoTasks.listIdColumn), list.id)
        }.update().apply()
      }
      else {
        withSQL {
          delete
              .from(TodoTasks)
              .where.eq(tskClm.column(TodoTasks.listIdColumn), list.id)
              .and.not.in(tskClm.id, list.tasks.map(t => t.id).toSeq)
        }.update().apply()
        
        for (task <- list.tasks) {
          sql"""
            merge into ${TodoTasks.table} (
              ${tskClm.id},
              ${tskClm.column(TodoTasks.listIdColumn)},
              ${tskClm.description},
              ${tskClm.completed}
            )
            key(${tskClm.id})
            values (${task.id}, ${list.id}, ${task.description}, ${task.completed})
          """.update().apply()
        }
      }

      list
    }
  }

  override def remove(id: UUID)(implicit context: PersistContext): Unit = {
    withinSession { implicit session =>
      withSQL {
        delete.from(TodoTasks).where.eq(tskClm.column(TodoTasks.listIdColumn), id)
      }.update().apply()
      withSQL {
        delete.from(TodoLists).where.eq(lstClm.id, id)
      }.update().apply()
    }
  }

  private def populateTasks(list: TodoList)(implicit session: DBSession): TodoList = {
    val tasks = withSQL {
      select(tsk.result.id, tsk.result.description, tsk.result.completed)
          .from(TodoTasks as tsk)
          .where.eq(tsk.column(TodoTasks.listIdColumn), list.id)
    }.map(TodoTasks(tsk)).list().apply()
    list.copy(tasks = tasks.toSet)
  }
}

object ScalikejdbcH2TodoListRepo {
  val lst = TodoLists.syntax("lst")
  val lstClm = TodoLists.column

  val tsk = TodoTasks.syntax("tsk")
  val tskClm = TodoTasks.column
}
