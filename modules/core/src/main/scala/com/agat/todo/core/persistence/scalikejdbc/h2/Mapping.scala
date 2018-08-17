/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.persistence.scalikejdbc.h2

import scalikejdbc._

import com.agat.todo.core.model.{TodoList, TodoTask}

object Mapping {
  import com.agat.todo.core.infrastructure.persistence.scalikejdbc.ExtBinders._

  object TodoLists extends SQLSyntaxSupport[TodoList] {
    override val tableName: String = "todo_lists"

    override val columns: Seq[String] = Seq("id", "name")

    def apply(rs: WrappedResultSet): TodoList = TodoList(rs.get(column.id), rs.get(column.name))

    def apply(rn: ResultName[TodoList])(rs: WrappedResultSet): TodoList = TodoList(rs.get(rn.id), rs.get(rn.name))

    def apply(sp: SyntaxProvider[TodoList])(rs: WrappedResultSet): TodoList = apply(sp.resultName)(rs)
  }

  object TodoTasks extends SQLSyntaxSupport[TodoTask] {
    override val tableName: String = "todo_tasks"

    val listIdColumn = "list_id"

    override val columns: Seq[String] = Seq("id", listIdColumn, "description", "completed")

    def apply(rs: WrappedResultSet): TodoTask = TodoTask(
      rs.get(column.id),
      rs.get(column.description),
      rs.get(column.completed)
    )

    def apply(rn: ResultName[TodoTask])(rs: WrappedResultSet): TodoTask =
      TodoTask(rs.get(rn.id), rs.get(rn.description), rs.get(rn.completed))

    def apply(sp: SyntaxProvider[TodoTask])(rs: WrappedResultSet): TodoTask = apply(sp.resultName)(rs)
  }
}
