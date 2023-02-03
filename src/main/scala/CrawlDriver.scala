import org.openqa.selenium.By
import org.openqa.selenium.remote.RemoteWebDriver

import scala.collection.mutable
import scala.collection.parallel.CollectionConverters.MutableSeqIsParallelizable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CrawlDriver {

  def dispatchDriver(driver: RemoteWebDriver, linkPool: mutable.ArrayDeque[String]): Future[mutable.ArrayDeque[String]] = Future {
    val newLinks = mutable.ArrayDeque[String]()
    linkPool.foreach(link => {
      println(link)
      if (link != null) {
        println("--------" + newLinks.length)
        driver.get(link)
        driver
          .findElements(By.tagName("a"))
          .forEach(elem => {
            val href: String = elem.getAttribute("href")
            if (!linkPool.contains(href) && !newLinks.contains(href)) {
              println(href)
              newLinks.addOne(href)
              linkPool.addOne(href)
            }
          })
      }
    })
    newLinks
  }
}
