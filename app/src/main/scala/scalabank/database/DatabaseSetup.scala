package scalabank.database

import java.sql.{Connection, DriverManager}

object DatabaseSetup:
  val url = "jdbc:h2:~/test"
  val conn: Connection = DriverManager.getConnection(url)














