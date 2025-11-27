import scala.util.{Success, Failure}

object EnterpriseZapKafkaScannerApp {

  val zapAddress = "localhost"
  val zapPort = 8080
  val zapApiKey = "<your-zap-api-key>"

  val baseUrl = "https://api.navigatorexpo.com"
  val endpoints = List("/users", "/healthcheck", "/databases", "/user/getbyid")

  val kafkaBootstrapServers = "localhost:9092"
  val kafkaTopic = "zap-scan-results"

  def main(args: Array[String]): Unit = {
    val zapScanner = new ZapScanner(zapAddress, zapPort, zapApiKey)
    val kafkaProducerUtil = new KafkaProducerUtil(kafkaBootstrapServers)

    endpoints.foreach { endpoint =>
      val targetUrl = s"$baseUrl$endpoint"
      println(s"Starting scan for endpoint: $targetUrl")

      zapScanner.scanEndpoint(targetUrl) match {
        case Success(alertsJson) =>
          kafkaProducerUtil.send(kafkaTopic, endpoint, alertsJson)
        case Failure(ex) =>
          println(s"[Error] Scan failed for $targetUrl: ${ex.getMessage}")
      }
    }

    kafkaProducerUtil.close()
  }
}
