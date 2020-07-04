package gov.anvisa

import org.apache.log4j.Logger
import org.apache.spark.sql.{SaveMode, SparkSession}

object ConverteTA_ORCAMENTO extends Serializable {

  @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {

    val ss = SparkSession.builder
      .appName("CSV to Dataset")
      .master("local[*]")
      .getOrCreate

    //Abri o arquivo CSV
    val TA_ORCAMENTO = ss.read
      .format("csv")
      .option("header", "true")
      .option("sep", ";")
      .option("encoding", "windows-1252")
      .option("inferSchema","True")
      .option("path","D:\\data\\TA_ORCAMENTO.csv")
      .load()

    // Converte TA_ORCAMENTO.csv para TA_PAF.parquet
    TA_ORCAMENTO.write
      .format("parquet")
      .mode(SaveMode.Overwrite)
      .option("encoding", "UTF-8")
      .option("path", "D:\\data\\TA_ORCAMENTO\\")
      .partitionBy( "NU_ANO")
      .option("maxRecordsPerFile", 10000)
      .save()

    TA_ORCAMENTO.show(1)

    logger.info("===========Finished=========")
    ss.stop()
  }
}