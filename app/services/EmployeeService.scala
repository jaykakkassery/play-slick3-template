package services

import com.google.inject.Inject
import models.{Address, Employee, Employees, User, Users}

import scala.concurrent.Future

class EmployeeService @Inject() (employees: Employees) {

  def addEmployeeWithAddress(employee: Employee, address: Address) = {
    employees.addWithAddress(employee, address)
  }

  def addEmployee(employee: Employee) = {
    employees.add(employee)
  }

  def listAll = {
    employees.listAll
  }

}
