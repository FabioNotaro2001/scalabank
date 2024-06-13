package scalabank.currency

import sttp.client3._
import io.circe._, io.circe.parser._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Trait representing an exchange rate provider.
 */
trait ExchangeRateProvider:
  /**
   * Gets the exchange rate from one currency to another.
   *
   * @param from The source currency code.
   * @param to   The target currency code.
   * @return The exchange rate.
   */
  def getExchangeRate(from: String, to: String): Future[BigDecimal]


object ExchangeRateProvider:
  def apply(): ExchangeRateProvider = ExchangeRateProviderImpl()

  private case class ExchangeRateProviderImpl() extends ExchangeRateProvider:
    private val backend = HttpURLConnectionBackend()

    override def getExchangeRate(from: String, to: String): Future[BigDecimal] =
      val url = s"https://query1.finance.yahoo.com/v8/finance/chart/${from}${to}=X"
      for
        response <- fetchExchangeRate(url)
        rate <- parseExchangeRate(response)
      yield rate

    private def fetchExchangeRate(url: String): Future[String] = Future:
      val request = basicRequest.get(uri"$url")
      val response = request.send(backend)
      response.body match
        case Right(data) => data
        case Left(error) => throw new Exception(s"Error fetching exchange rate: $error")

    private def parseExchangeRate(data: String): Future[BigDecimal] = Future:
      parse(data) match
        case Left(failure) => throw new Exception(s"Error parsing JSON: $failure")
        case Right(json) => extractInformation(json).getOrElse(throw new Exception(s"Error extracting rate"))

    private def extractInformation(json: Json) =
      for
        chart <- json.hcursor.downField("chart").as[Json]
        resultArray <- chart.hcursor.downField("result").as[List[Json]]
        firstResult <- resultArray.headOption.toRight(DecodingFailure("Result array is empty", Nil))
        meta <- firstResult.hcursor.downField("meta").as[Json]
        regularMarketPrice <- meta.hcursor.downField("regularMarketPrice").as[Double]
      yield regularMarketPrice
