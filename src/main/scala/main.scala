import org.apache.spark.sql.SparkSession
import org.jfree.chart.ChartFactory
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.DefaultXYDataset

import java.io.File

object main extends App{
  val spark=SparkSession.builder
    .config("spark.master", "local")
    .appName("sf_crime_graph")
    .getOrCreate()


  spark.conf.set("spark.sql.shuffle.partitions",6)


  val crime_df=spark.read.option("header","true").option("inferschema","true").csv("Police_Department_Incident_Reports__2018_to_Present.csv")

  val non_null_crime_df=crime_df.na.drop(Seq("Latitude","Longitude","Incident Category"))


  non_null_crime_df.groupBy("Incident Category").count().show(false)

  val hom_df=non_null_crime_df.filter(non_null_crime_df("Incident Category")==="Sex Offense")



  hom_df.show(10)
  print(hom_df.count())


  val dataset=new DefaultXYDataset

  dataset.addSeries("Sexual Offense Latitude and Longitude",Array(hom_df.select("Longitude").collect.map(_.getDouble(0)),hom_df.select("Latitude").collect.map(_.getDouble(0))))

  val chart=ChartFactory.createScatterPlot(
    "San Francisco Sexual Offenses 2018-Present",
    "Longitude",
    "Latitude",
    dataset,
    PlotOrientation.VERTICAL,
    true,
    false,
    false
  )


  val plot=chart.getXYPlot

  import javax.imageio.ImageIO

  val url = new File("sf.png")
  val image2 = ImageIO.read(url)
  plot.setBackgroundImage(image2)


  val renderer=plot.getRenderer.asInstanceOf[XYLineAndShapeRenderer]

  plot.setRenderer(renderer)

  val image=chart.createBufferedImage(800,767)
  ImageIO.write(image,"png",new File("test.png"))


}
