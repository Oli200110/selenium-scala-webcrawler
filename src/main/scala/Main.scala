import org.openqa.selenium.By
import org.openqa.selenium.remote.{DesiredCapabilities, RemoteWebDriver}
import java.net.URL
import scala.collection.mutable
import scala.collection.parallel.CollectionConverters.seqIsParallelizable


object Main {
  private val HUB_URL = "http://localhost:4444/wd/hub"
  private val NUM_OF_DRIVERS = 2
  private val START_URL = "https://www.telex.hu"

  def main(args: Array[String]): Unit = {
    println("Hello world!")
    val desiredCapabilities = new DesiredCapabilities()
    desiredCapabilities.setBrowserName("chrome")
    val driver: RemoteWebDriver = new RemoteWebDriver(new URL(HUB_URL), desiredCapabilities)
    driver.get(START_URL)

    val linkPool = new mutable.ArrayDeque[String]()
    linkPool += START_URL

    linkPool.par.foreach(link => {
      println(link)
      if (link != null) {
        println("--------")
        driver.get(link)
        driver
          .findElements(By.tagName("a"))
          .forEach(elem => {
            val href: String = elem.getAttribute("href")
            if (!linkPool.contains(href)) {
              println(href)
              linkPool.addOne(href)
            }
          })
      }
    })


    driver.quit()
    println(linkPool.length)
  }
}