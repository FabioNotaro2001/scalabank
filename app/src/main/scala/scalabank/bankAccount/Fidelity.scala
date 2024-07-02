package scalabank.bankAccount

/**
 * Enum representing the different levels of fidelity a customer can achieve.
 */
enum FidelityLevel:
    case Platinum, Gold, Silver, Bronze

/**
 * Trait representing a fidelity program for customers.
 */
trait Fidelity:
    /**
     * @return the current points of the fidelity program.
     */
    def points: Int

    /**
     * Adds points to the fidelity program.
     *
     * @param pointsToAdd the number of points to add.
     */
    def addPoints(pointsToAdd: Int): Unit

    /**
     * Redeems points from the fidelity program.
     *
     * @param pointsToRedeem the number of points to redeem.
     * @return true if the points were successfully redeemed, false otherwise.
     */
    def redeemPoints(pointsToRedeem: Int): Boolean

    /**
     * @return the total points used from the fidelity program.
     */
    def pointsUsed: Int

case class FidelityImpl(var _points: Int) extends Fidelity:
   
    private var _pointsUsed: Int = 0

    override def points: Int = _points

    override def pointsUsed: Int = _pointsUsed

    override def addPoints(pointsToAdd: Int): Unit = _points = _points + pointsToAdd

    override def redeemPoints(pointsToRedeem: Int): Boolean = pointsToRedeem match
        case _pointsToRedeem if _points >= _pointsToRedeem =>
            _points = _points - _pointsToRedeem
            _pointsUsed = _pointsUsed + _pointsToRedeem
            true
        case _ => false

/**
 * Companion object for the Fidelity trait.
 */
object Fidelity:
    def apply(_points: Int): Fidelity = FidelityImpl(_points)