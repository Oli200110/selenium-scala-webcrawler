package crawler

import org.apache.spark.graphx
import org.apache.spark.graphx.{Edge, Graph, PartitionStrategy}
import org.apache.spark.graphx.impl._
import org.apache.spark.graphx.lib._
import org.apache.spark.graphx.util._
import org.apache.spark.sql._


class PageRank {
  private val spark: SparkSession = SparkSession.builder().master("local").getOrCreate()
  private val sc = spark.sparkContext

  /*private val graph: Graph[Double, Int] = {
    GraphGenerators.logNormalGraph(sc, numVertices = 10).mapVertices((id, _) => id.toDouble).partitionBy(PartitionStrategy.EdgePartition2D, 4)
  }

  graph.pageRank(0.0001).vertices.sortBy(-_._2).collect.foreach(println)
  graph.pageRank(0.0001).vertices
    .sortBy(-_._2).toDF()
    .withColumnRenamed("_1", "VertexId")
    .withColumnRenamed("_2", "PageRank")
    .createOrReplaceTempView("pagerank")
  spark.sql(
    "select sum (PageRank) from pagerank").show()
*/
  /*val log = List(
    List("a", "b", "c"),
    List("a", "c", "b", "h", "c"),
    List("a", "d", "e"),
    List("a", "d", "e", "f", "d", "e")
  )
  val vertices = log.flatMap(x => x).toSet.toSeq
  val vertexMap = (0 until vertices.size)
    .map(i => vertices(i) -> i.toLong)
    .toMap

  val edgeSet = log
    .filter(_.size > 1)
    .flatMap(list => list.indices.tail.map(i => list(i - 1) -> list(i)))
    .map(x => Edge(vertexMap(x._1), vertexMap(x._2), "1"))

  val edges = sc.parallelize(edgeSet.toSeq)
  val vertexNames = sc.parallelize(vertexMap.toSeq.map(_.swap))
  val graph = Graph(vertexNames, edges)*/

  private val list: List[(String, String)] = List()

  private val iters = 10
  private val lines = sc.parallelize(list)
  /*val links = lines.map { s =>
    val parts = s.split("\\s+")
    (parts(0), parts(1))
  }.distinct().groupByKey().cache()*/
  private val links = lines.distinct().groupByKey().cache()
  private var ranks = links.mapValues(v => 1.0)

  for (i <- 1 to iters) {
    val contribs = links.join(ranks).values.flatMap { case (urls, rank) =>
      val size = urls.size
      urls.map(url => (url, rank / size))
    }
    ranks = contribs.reduceByKey(_ + _).mapValues(0.15 + 0.85 * _)
  }

  private val output = ranks.collect()
  output.foreach(tup => println(s"${tup._1} has rank:  ${tup._2} ."))

  spark.stop()


}
