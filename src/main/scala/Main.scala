import org.openqa.selenium.Capabilities
import org.openqa.selenium.chromium.ChromiumOptions
import org.openqa.selenium.remote.{DesiredCapabilities, RemoteWebDriver}

import java.net.URL


object Main {
  private val HUB_URL = "http://localhost:4444/wd/hub"

  def main(args: Array[String]): Unit = {
    println("Hello world!")
    val chromiumOptions = new DesiredCapabilities()
    chromiumOptions.setBrowserName("chromium")
    val driver: RemoteWebDriver = new RemoteWebDriver(new URL(HUB_URL), chromiumOptions)
    driver.get("https://www.google.com")
    driver.quit()
  }
}