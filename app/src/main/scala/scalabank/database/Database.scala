package scalabank.database

import scalabank.database.person.PersonTable
import java.sql.{Connection, DriverManager}

trait Database:
  def personTable: PersonTable

object Database:
  def apply(url : String): Database = TablesImpl(url)

  private case class TablesImpl(url: String) extends Database:
    val connection: Connection = DriverManager.getConnection(url)
    private val personTab: PersonTable = PersonTable(connection)

    override def personTable: PersonTable = personTab
















