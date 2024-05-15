package scalabank.entities

import scalabank.entities.Employee.Position

abstract class StaffMember extends Person:
  def position: Position
  def salary: Double



