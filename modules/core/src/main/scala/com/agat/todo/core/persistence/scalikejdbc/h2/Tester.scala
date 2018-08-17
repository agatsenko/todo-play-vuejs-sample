/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-08-16
  */
package com.agat.todo.core.persistence.scalikejdbc.h2

import java.util.UUID

import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import scalikejdbc._

import com.agat.todo.core.infrastructure.persistence.PersistService
import com.agat.todo.core.infrastructure.persistence.scalikejdbc.ScalikejdbcPersistService
import com.agat.todo.core.infrastructure.util.using
import com.agat.todo.core.model.{TodoList, TodoListRepo, TodoTask}

// FIXME: need to remove
object Tester {

  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger("tester")
    Class.forName("org.h2.Driver")
    val dataSource: HikariDataSource = {
      val ds = new HikariDataSource()
      ds.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource")
      ds.addDataSourceProperty("url", "jdbc:h2:file:./db/todo-play-vuejs-slick-sample")
      ds.addDataSourceProperty("user", "")
      ds.addDataSourceProperty("password", "")
      ds
    }
    using(new ScalikejdbcPersistService(new DataSourceConnectionPool(dataSource, closer = () => dataSource.close()))) {
      persistService: PersistService =>
        val repo: TodoListRepo = new ScalikejdbcH2TodoListRepo
        for (_ <- 1 to 10) {
          val start = System.nanoTime()
          persistService.exec { implicit context =>
            println(repo.getAll)
          }
          val finish = System.nanoTime()
          logger.info("test duration: {} ms", (finish - start) / 1000000.0)
        }
        println("=================================================")
        persistService.exec { implicit context =>
          repo.remove(UUID.fromString("20fb4b67-4310-4fe8-aff6-b52f1ee7659e"))
          println(repo.getById(UUID.fromString("20fb4b67-4310-4fe8-aff6-b52f1ee7659e")))
        }
        println("=================================================")
        persistService.exec { implicit context =>
          repo.save(
            TodoList(
              UUID.randomUUID(),
              "new list",
              Set(
                TodoTask(UUID.randomUUID(), "task 2", completed = true),
                TodoTask(UUID.randomUUID(), "task 3"),
              )
            )
          )
        }
        println("=================================================")
    }
  }
}
