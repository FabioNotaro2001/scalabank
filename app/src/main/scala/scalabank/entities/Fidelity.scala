package scalabank.entities

enum FidelityLevel:
        case Platinum, Gold, Silver, Bronze
    
import FidelityLevel.*


trait Fidelity:
    def points: Int
    def addPoints(pointsToAdd: Int): Unit
    def redeemPoints(pointsToRedeem: Int): Boolean
    def currentLevel: FidelityLevel
    def pointsUsed: Int

object Fidelity:

    def apply(_points: Int): Fidelity = FidelityImpl(_points)

    case class FidelityImpl(var _points: Int) extends Fidelity:
        var _pointsUsed : Int = 0
        override def points: Int = _points

        override def pointsUsed: Int = _pointsUsed

        override def addPoints(pointsToAdd: Int): Unit = _points =  _points + pointsToAdd
        override def redeemPoints(pointsToRedeem: Int): Boolean = pointsToRedeem match
            case _pointsToRedeem if _points >= _pointsToRedeem => 
                _points = _points - _pointsToRedeem
                _pointsUsed = _pointsUsed + _pointsToRedeem 
                true
            case _ => false
        override def currentLevel: FidelityLevel = ??? 
        
