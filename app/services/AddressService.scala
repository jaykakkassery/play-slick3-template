package services

import com.google.inject.Inject
import models.{Address, Addresses, Employee, Employees, User, Users}

import scala.concurrent.Future

class AddressService @Inject() (addresses: Addresses) {

  def addAddress(address: Address) = {
    addresses.add(address)
  }
}
