package crawler

import crawler.Main.MAX_POOL_SIZE
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.{By, StaleElementReferenceException}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CrawlDriver {

  def dispatchDriver(driver: RemoteWebDriver, linkPool: mutable.ArrayDeque[String]): Future[mutable.ArrayDeque[String]] = Future {
    val newLinks = mutable.ArrayDeque[String]()
    linkPool.foreach(link => {
      println(link)
      if (link != null && linkPool.length < MAX_POOL_SIZE) {
        println("-------- " + newLinks.length)
        driver.get(link)
        driver
          .findElements(By.tagName("a"))
          .forEach(elem => {
            if (linkPool.length < MAX_POOL_SIZE)
              try {
                val href: String = elem.getAttribute("href")
                if (!linkPool.contains(href) && !newLinks.contains(href)) {
                  println(href)
                  newLinks.addOne(href)
                  linkPool.addOne(href)
                }
              } catch {
                case e: StaleElementReferenceException => println(e)
              }
          })
      }
    })
    newLinks
  }
}
