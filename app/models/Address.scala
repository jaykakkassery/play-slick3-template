package models

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile


import scala.concurrent.{ExecutionContext, Future}

case class Address(id: Long, addressline: String, employeeId: Long)

import slick.jdbc.MySQLProfile.api._

class AddressTableDef(tag: Tag) extends Table[Address](tag, "address") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def addressline = column[String]("addressline")
  def employeeId = column[Long]("employeeId")

  val emptable = TableQuery[EmployeeTableDef]

  def employee =
    foreignKey("EMP_ID", employeeId, emptable)(_.id, onDelete=ForeignKeyAction.Cascade)

  override def * =
    (id, addressline, employeeId) <>(Address.tupled, Address.unapply)
}

class Addresses @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  val addressTable = TableQuery[AddressTableDef]

  def add(address: Address) = {
    dbConfig.db.run(addressTable += address).map(res => "Address successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

}
