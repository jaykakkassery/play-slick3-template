package controllers

import javax.inject._
import models.{Address, Employee, User, UserForm}
import play.api.Logging
import play.api.mvc._
import services.{AddressService, EmployeeService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ApplicationController @Inject()(cc: ControllerComponents, userService: UserService, employeeService: EmployeeService, addressService: AddressService) extends AbstractController(cc) with Logging {

  def index() = Action.async { implicit request: Request[AnyContent] =>
    userService.listAllUsers map { users =>
      Ok(views.html.index(UserForm.form, users))
    }
  }

  def addUser() = Action.async { implicit request: Request[AnyContent] =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        logger.warn(s"Form submission with error: ${errorForm.errors}")
        Future.successful(Ok(views.html.index(errorForm, Seq.empty[User])))
      },
      data => {
        val newUser = User(0, data.firstName, data.lastName, data.mobile, data.email)
        userService.addUser(newUser).map( _ => Redirect(routes.ApplicationController.index()))
      })
  }

  def addEmployeeWithAddress() = Action { implicit request: Request[AnyContent] =>
        val newEmployee = Employee(0, "JP")
        val empAddress = Address(0, "2244 Jam Pickle Line",0)
        employeeService.addEmployeeWithAddress(newEmployee, empAddress)
        Ok("")
  }

  def addEmployee() = Action { implicit request: Request[AnyContent] =>
    val newEmployee = Employee(0, "JP-NoAddress")
    employeeService.addEmployee(newEmployee)
    Ok("")
  }

  def listAllEmployees() = Action.async { implicit request: Request[AnyContent] =>
    employeeService.listAll map { employees =>
      Ok(employees.toString())
    }
  }

  def addAddress() = Action { implicit request: Request[AnyContent] =>
    val newAddress = Address(0, "2255 Lodge Ct", 1)
    addressService.addAddress(newAddress)
    Ok("")
  }


  def deleteUser(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    userService.deleteUser(id) map { res =>
      Redirect(routes.ApplicationController.index())
    }
  }

}
