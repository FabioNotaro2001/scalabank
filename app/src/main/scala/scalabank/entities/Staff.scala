package scalabank.entities

import scalabank.entities.Employee.Position

abstract class Staff extends Person:
  def position: Position
  def salary: Double



