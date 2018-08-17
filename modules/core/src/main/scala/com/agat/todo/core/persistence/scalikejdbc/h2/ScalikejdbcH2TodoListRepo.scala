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

  override def getAll(implicit context: PersistContext): Seq[TodoList] = {
    withinSession { implicit session =>
      val l = TodoLists.syntax("l")
      sql"select ${l.result.*} from ${TodoLists.as(l)}".map(TodoLists(l)).list().apply().map(populateTasks)
    }
  }

  override def getById(id: UUID)(implicit context: PersistContext): Option[TodoList] = {
    withinSession { implicit session =>
      val l = TodoLists.syntax("l")
      sql"select ${l.result.*} from ${TodoLists.as(l)} where ${l.id} = $id"
          .map(TodoLists(l))
          .single()
          .apply()
          .map(populateTasks)
    }
  }

  override def save(list: TodoList)(implicit context: PersistContext): TodoList = {
    withinSession { implicit session =>
      val (lc, tc) = (TodoLists.column, TodoTasks.column)
      sql"""
        merge into ${TodoLists.table} (${lc.id}, ${lc.name})
        key (${lc.id})
        values (${list.id}, ${list.name})
      """.update().apply()

      sql"""
        delete
          from ${TodoTasks.table}
          where ${tc.column(TodoTasks.listIdColumn)} = ${list.id}
            and ${tc.id} not in (${list.tasks.map(t => t.id)})
      """.update().apply()

      for (task <- list.tasks) {
        sql"""
          merge into ${TodoTasks.table} (
            ${tc.id},
            ${tc.column(TodoTasks.listIdColumn)},
            ${tc.description},
            ${tc.completed}
          )
          key(${tc.id})
          values (${task.id}, ${list.id}, ${task.description}, ${task.completed})
        """.update().apply()
      }

      list
    }
  }

  override def remove(id: UUID)(implicit context: PersistContext): Unit = {
    withinSession { implicit session =>
      val (lc, tc) = (TodoLists.column, TodoTasks.column)
      sql"delete from ${TodoTasks.table} where ${tc.column(TodoTasks.listIdColumn)} = $id".update().apply()
      sql"delete from ${TodoLists.table} where ${lc.id} = $id".update().apply()
    }
  }

  private def populateTasks(list: TodoList)(implicit session: DBSession): TodoList = {
    val t = TodoTasks.syntax("t")
    val tasks =
      sql"""
        select
          ${t.result.id},
          ${t.result.description},
          ${t.result.completed}
            from ${TodoTasks.as(t)}
            where ${t.column(TodoTasks.listIdColumn)} = ${list.id}
      """
          .map(TodoTasks(t))
          .list()
          .apply()
    list.copy(tasks = tasks.toSet)
  }
}
