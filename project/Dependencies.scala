import sbt._


object Dependencies{

    val ArrowVersion="0.10.0"

    lazy val CirceVersion="0.13.0"
    lazy val circeDerivationVersion="0.13.0-M5"

    lazy val FicusVersion="1.5.1"

    val hadoopVersion="3.3.2"
    val hiveVersion="3.1.3"
    val Http4sVersion="0.21.16" // version da is using

    val icebergVersion="1.2.1"

    val parquetVersion="1.13.0"

    lazy val sangriaVersion="2.1.0"
    lazy val sangriaCirceVersion="1.3.1"
    val sparkVersion="3.5.0" // Delta 3.0.x can only be used with Spark >= 3.5
    val sparkGlueVersion="3.3.2" // glue 4 uses 3.3.0, spark starts support scala 2.13 only since 3.2.0, only delta <3

    // logging
    lazy val LogbackVersion="1.2.3"
    lazy val log4jCoreVersion="2.16.0"
    lazy val log4sVersion="1.10.0"

    lazy val monixVersion="3.3.0"
    lazy val slickVersion="3.3.3"
    lazy val scalaTestVersion="3.2.9"

    lazy val testcontainersVersion="1.16.2"
    lazy val TypesafeConfigVersion="1.4.1"

    lazy val postgresqlVersion="42.3.1"
    lazy val AWSEMFVersion="4.0.2"
    lazy val doobieVersion="0.13.4"

    val arrowDeps:Seq[ModuleID]=Seq(
    "org.apache.arrow"%"arrow-memory"%ArrowVersion,
    "org.apache.arrow"%"arrow-vector"%ArrowVersion
    )

    lazy val awsEMF:Seq[ModuleID]=Seq(
    "software.amazon.cloudwatchlogs"%"aws-embedded-metrics"%AWSEMFVersion
    )

    lazy val configDependencies:Seq[ModuleID]=Seq(
    "com.typesafe"%"config"%TypesafeConfigVersion,
    "com.iheart"%%"ficus"%FicusVersion
    )

    lazy val dbDependencies:Seq[ModuleID]=Seq(
    "org.postgresql"%"postgresql"%postgresqlVersion,
    "com.typesafe.slick"%%"slick"%slickVersion,
    "com.typesafe.slick"%%"slick-hikaricp"%slickVersion
    )

    val hadoopDeps:Seq[ModuleID]=Seq(
    "org.apache.hadoop"%"hadoop-common"%hadoopVersion,
    "org.apache.hadoop"%"hadoop-mapreduce-client-core"%hadoopVersion
    )

    val hiveDeps:Seq[ModuleID]=Seq(
    "org.apache.hive"%"hive-metastore"%hiveVersion
    )

    lazy val httpDependencies:Seq[ModuleID]=Seq(
    "org.http4s"%%"http4s-blaze-server"%Http4sVersion,
    "org.http4s"%%"http4s-blaze-client"%Http4sVersion,
    "org.http4s"%%"http4s-circe"%Http4sVersion,
    "org.http4s"%%"http4s-dsl"%Http4sVersion,
    "org.http4s"%%"http4s-circe"%Http4sVersion,
    // Optional for auto-derivation of JSON codecs
    "io.circe"%%"circe-generic"%CirceVersion,
    "io.circe"%%"circe-core"%CirceVersion,
    "io.circe"%%"circe-optics"%CirceVersion,
    "io.circe"%%"circe-literal"%CirceVersion, // Optional for string interpolation to JSON model
    "io.circe"%%"circe-parser"%CirceVersion,
    "io.circe"%%"circe-generic-extras"%CirceVersion,
    "io.circe"%%"circe-derivation"%circeDerivationVersion
    )

    val icebergDeps:Seq[ModuleID]=Seq(
    "org.apache.iceberg"%"iceberg-api"%icebergVersion,
    "org.apache.iceberg"%"iceberg-arrow"%icebergVersion,
    "org.apache.iceberg"%"iceberg-common"%icebergVersion,
    "org.apache.iceberg"%"iceberg-core"%icebergVersion,
    "org.apache.iceberg"%"iceberg-data"%icebergVersion,
    "org.apache.iceberg"%"iceberg-hive-metastore"%icebergVersion,
    "org.apache.iceberg"%"iceberg-parquet"%icebergVersion
    )

    lazy val loggingDependencies:Seq[ModuleID]=Seq(
    "ch.qos.logback"%"logback-classic"%LogbackVersion,
    "org.log4s"%%"log4s"%log4sVersion
    )

    lazy val monixDependencies:Seq[ModuleID]=Seq(
    "io.monix"%%"monix"%monixVersion,
    "io.monix"%%"monix-reactive"%monixVersion,
    "io.monix"%%"monix-kafka-1x"%"1.0.0-RC7"
    )

    val parquetDeps:Seq[ModuleID]=Seq(
    "org.apache.parquet"%"parquet-arrow"%parquetVersion,
    "org.apache.parquet"%"parquet-column"%parquetVersion,
    "org.apache.parquet"%"parquet-common"%parquetVersion,
    "org.apache.parquet"%"parquet-column"%parquetVersion,
    "org.apache.parquet"%"parquet-hadoop"%parquetVersion,
    // not fixing version(parquet-hadoop-bundle) == version(parquet-hadoop) might cause error below
    // Missing org.apache.parquet.hadoop.ColumnChunkPageWriteStore(...
    // as a different of the bundle is used
    "org.apache.parquet"%"parquet-hadoop-bundle"%parquetVersion
    )

    val sparkDeps:Seq[ModuleID]=Seq(
    "org.apache.spark"%%"spark-core"%sparkVersion,
    "org.apache.spark"%%"spark-sql"%sparkVersion,
    "org.apache.spark"%%"spark-sql-kafka-0-10"%sparkVersion,
    "org.apache.spark"%%"spark-hive"%sparkVersion,
    "org.apache.spark"%%"spark-catalyst"%sparkVersion, // delta-spark provided
    "io.delta"%%"delta-spark"%"3.0.0",
    )

    lazy val testDependencies:Seq[ModuleID]=Seq(
    "org.testcontainers"%"testcontainers"%testcontainersVersion%Test,
    "org.scalatest"%%"scalatest"%scalaTestVersion%Test,
    "org.scalatest"%%"scalatest-wordspec"%scalaTestVersion%Test,
    "junit"%"junit"%"4.13.2"%Test
    )

    // project specific
    val scalaSnippetsDependencies:Seq[ModuleID]=monixDependencies++testDependencies

    val icebergModuleDeps:Seq[ModuleID]=arrowDeps++
    hadoopDeps++
    hiveDeps++
    icebergDeps++
    parquetDeps++
    testDependencies

    val monixModuleDependencies:Seq[ModuleID]=monixDependencies++
    testDependencies++
    loggingDependencies

    val monixCorrIdDependencies:Seq[ModuleID]=monixDependencies++
    testDependencies++
    loggingDependencies++
    Seq(
    "org.http4s"%%"http4s-dsl"%Http4sVersion,
    "org.http4s"%%"http4s-blaze-server"%Http4sVersion,
    "com.softwaremill.sttp"%%"async-http-client-backend-cats"%"1.7.2",
    "org.flywaydb"%"flyway-core"%"5.2.1",
    "org.tpolecat"%%"doobie-core"%doobieVersion,
    "org.tpolecat"%%"doobie-hikari"%doobieVersion,
    "org.tpolecat"%%"doobie-h2"%doobieVersion,
    "ch.qos.logback"%"logback-classic"%"1.4.5",
    "com.typesafe.scala-logging"%%"scala-logging"%"3.9.5"
    )

    val monixMdcDeps:Seq[ModuleID]=monixDependencies++
    httpDependencies++
    testDependencies++
    Seq(
    "org.sangria-graphql"%%"sangria"%sangriaVersion,
    "org.sangria-graphql"%%"sangria-circe"%sangriaCirceVersion,
    "org.apache.logging.log4j"%"log4j-core"%log4jCoreVersion,
    "org.apache.logging.log4j"%"log4j-slf4j-impl"%log4jCoreVersion,
    "org.log4s"%%"log4s"%log4sVersion
    )

    val handsOnScalaDependencies:Seq[ModuleID]=Seq(
    "com.lihaoyi"%%"requests"%"0.7.0"
    )++
    testDependencies

    val algoDSDeps:Seq[ModuleID]=testDependencies++
    Seq(
    "org.projectlombok"%"lombok"%"1.18.26",
    "com.alibaba"%"fastjson"%"1.2.83"
    )

    val queryEngineDeps:Seq[ModuleID]=arrowDeps++
    hadoopDeps++
    icebergDeps++
    parquetDeps++
    testDependencies++
    Seq(
    "io.dropwizard.metrics"%"metrics-core"%"4.2.19"
    )

    val sparkExamplesDeps:Seq[ModuleID]=sparkDeps++
    hadoopDeps++
    parquetDeps++
    testDependencies

    }
