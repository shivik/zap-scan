import org.zaproxy.clientapi.core.{ClientApi, ApiResponseElement}
import scala.util.{Try}

class ZapScanner(zapAddress: String, zapPort: Int, apiKey: String) {

  private val api = new ClientApi(zapAddress, zapPort, apiKey)

  def scanEndpoint(targetUrl: String): Try[String] = Try {
    api.core.accessUrl(targetUrl, true)

    val spiderResp = api.spider.scan(targetUrl, "", "", "", "")
    val spiderScanId = spiderResp.asInstanceOf[ApiResponseElement].getValue
    waitForSpider(spiderScanId)

    val ascanResp = api.ascan.scan(targetUrl, "true", "false", null, null, null)
    val ascanScanId = ascanResp.asInstanceOf[ApiResponseElement].getValue
    waitForActiveScan(ascanScanId)

    api.core.jsonreport()
  }

  private def waitForSpider(scanId: String): Unit = {
    var progress = 0
    while (progress < 100) {
      Thread.sleep(1000)
      progress = api.spider.status(scanId).toInt
      println(s"Spider progress: $progress%")
    }
  }

  private def waitForActiveScan(scanId: String): Unit = {
    var progress = 0
    while (progress < 100) {
      Thread.sleep(2000)
      progress = api.ascan.status(scanId).toInt
      println(s"Active scan progress: $progress%")
    }
  }
}
