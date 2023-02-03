import CrawlDriver.dispatchDriver
import org.openqa.selenium.By
import org.openqa.selenium.remote.{DesiredCapabilities, RemoteWebDriver}

import java.net.URL
import scala.collection.mutable
import scala.collection.parallel.CollectionConverters.seqIsParallelizable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}


object Main {
  private val HUB_URL = "http://localhost:4444/wd/hub"
  private val NUM_OF_DRIVERS = 2
  private val MAX_POOL_SIZE = 1000
  private val START_URL = "https://www.telex.hu"

  def main(args: Array[String]): Unit = {
    println("Hello world!")
    val desiredCapabilities = new DesiredCapabilities()
    desiredCapabilities.setBrowserName("chrome")
    val drivers = Array.fill(NUM_OF_DRIVERS) {
      new RemoteWebDriver(new URL(HUB_URL), desiredCapabilities)
    }

    val linkPool = new mutable.ArrayDeque[String]()
    linkPool += START_URL

    val fg = {
      for {
        res <- dispatchDriver(drivers(0), linkPool)
        result <- dispatchDriver(drivers(0), res)
        _ <- Future {
          println(result)
        }
      } yield ()
    }


    Await.result(fg, Duration.Inf)
    println(fg)
    //driver.quit()
    //drivers.foreach(driver => driver.quit())
    //println(linkPool.toSeq.length)

  }
}
