package models

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

case class Employee(id: Long, name: String)
case class EmployeeWithAddress(employee: Employee, address: Address)

import slick.jdbc.MySQLProfile.api._

class EmployeeTableDef(tag: Tag) extends Table[Employee](tag, "employee") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")

  override def * =
    (id, name) <>(Employee.tupled, Employee.unapply)
}

class Employees @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val employeeTable = TableQuery[EmployeeTableDef]
  val addressTable = TableQuery[AddressTableDef]
  lazy val insertEmployee = employeeTable returning employeeTable.map(_.id)


  def addWithAddress(employee: Employee, address: Address) = {

    val populateFirstTime =
      for {
        empId <- insertEmployee += Employee(0, employee.name)
        count <- addressTable ++= Seq(
          Address(0, address.addressline, empId),
        )
      } yield count
    dbConfig.db.run(populateFirstTime)
  }

  def add(employee: Employee) = {
    dbConfig.db.run(employeeTable += employee).map(res => "Employee successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def listAll = {
    //dbConfig.db.run(employeeTable.result)
    val query = addressTable.join(employeeTable).on(_.employeeId === _.id)
    dbConfig.db.run(query.result).map { a =>
      a.groupBy(_._1.id).map { case (_, tuples) =>
        val (address, employee) = tuples.head
        EmployeeWithAddress(employee, address)
      }.toSeq
    }
  }
}
