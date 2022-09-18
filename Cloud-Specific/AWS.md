# Amazon Web Services

## Data Governance
- Third-party vendor: Collibra

## Data Quality
- Deequ

## Machine Learning
Books
- [Data Science on AWS: Implementing End-to-End, Continuous AI and Machine Learning Pipelines](https://www.amazon.com/dp/1492079391)

Articles
- ...

## Streaming 

### Compute

Aspects to consider: CICD, testability, maintenance, observability, configurability, service integration

Kinesis Data Analytics (KDA)
- Pros
    - Serverless, Automatic Scaling based on CPU usage
    - Integration with Kinesis Data Streams
- Cons
    - Flink version might not be the latest. Must use the Flink, Scala, JDK, Beam, Python version that KDA runtime supports (Flink 1.13, JDK 11, Scala 2.12, Python 3.8, and Apache Beam v2.32 - Oct, 2021)
    - Some cluster configs are either fixed or not user-configurable.

Self-Managed Flink
- Pros
- Cons

AWS Lambda
- Cons
    - not suitable for stateful applications.

Container App (EKS/ Fargate)
- Pros
- Cons

Open Questions
- KDA Integration Testing: is spawning a Flink cluster for testing assure the test with KDA or is there any gotchas?


### Kinesis Data Analytics

#### Autoscale
Autoscale behavior

- When in auto scaling mode, the KDA will monitor the cluster for CPU usage. When CPU usage remains at 75 percent or above for 15 minutes, the KDA cluster will scale up.
- In order to scaling up, current KDA app will be shutdown and replace by a new KDA cluster with more KPU.
    - This behavior creates a down time in few minutes range of that KDA App.
    - **The down time period may affect SLA**

https://docs.aws.amazon.com/kinesisanalytics/latest/java/how-scaling.html

#### Deployment
- Flink Deployment Mode: per-job mode (which is deprecated in Flink 1.15 and will be removed in the future versions).


- Flink UI Console does not have log, all log pushed to Cloudwatch.

### Table Format & Governed Table
Basically Governed Table is a table format for data lake (immutable files sitting on 
object storage). It is closed-source version of AWS, similar to open source projects
like Apache Iceberge, Delta Lake, Apache Hudi.

AWS Analytics Services already has some supports for 
- Apache Hudi
- Apache Iceberg, https://iceberg.apache.org/docs/latest/aws/, read AWS considerations and limitations
- Delta Lake

- Record level transaction, dedup, upsert based on a key?: currently not supported. 
If you have micro-batches coming a files and there is duplicated rows, dedup is
achieved by a separate processing engine.

- Does Governed Table provide MERGE for upsert similar to Iceberg?

Reference
- [Implement a CDC-based UPSERT in a data lake using Apache Iceberg and AWS Glue](
https://aws.amazon.com/blogs/big-data/implement-a-cdc-based-upsert-in-a-data-lake-using-apache-iceberg-and-aws-glue/)

Sample code
```sql
-- Create output Iceberg table with partitioning. 
CREATE TABLE iceberg_demo.iceberg_output (
  product_id bigint,
  category string,
  product_name string,
  quantity_available bigint,
  last_update_time timestamp) 
PARTITIONED BY (category, bucket(16,product_id)) 
LOCATION 's3://glue-iceberg-demo/iceberg-output/' 
TBLPROPERTIES (
  'table_type'='ICEBERG',
  'format'='parquet',
  'write_target_data_file_size_bytes'='536870912' 
)
```

```python
args = getResolvedOptions(sys.argv, ['JOB_NAME', 'iceberg_job_catalog_warehouse'])
conf = SparkConf()

## Please make sure to pass runtime argument --iceberg_job_catalog_warehouse with value as the S3 path 
conf.set("spark.sql.catalog.job_catalog.warehouse", args['iceberg_job_catalog_warehouse'])
conf.set("spark.sql.catalog.job_catalog", "org.apache.iceberg.spark.SparkCatalog")
conf.set("spark.sql.catalog.job_catalog.catalog-impl", "org.apache.iceberg.aws.glue.GlueCatalog")
conf.set("spark.sql.catalog.job_catalog.io-impl", "org.apache.iceberg.aws.s3.S3FileIO")
conf.set("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
conf.set("spark.sql.sources.partitionOverwriteMode", "dynamic")
conf.set("spark.sql.iceberg.handle-timestamp-without-timezone","true")

sc = SparkContext(conf=conf)
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args["JOB_NAME"], args)

## Read Input Table
IncrementalInputDyF = glueContext.create_dynamic_frame.from_catalog(database = "iceberg_demo", table_name = "raw_csv_input", transformation_ctx = "IncrementalInputDyF")
IncrementalInputDF = IncrementalInputDyF.toDF()

if not IncrementalInputDF.rdd.isEmpty():
    ## Apply De-duplication logic on input data, to pickup latest record based on timestamp and operation 
    IDWindowDF = Window.partitionBy(IncrementalInputDF.product_id).orderBy(IncrementalInputDF.last_update_time).rangeBetween(-sys.maxsize, sys.maxsize)
                  
    # Add new columns to capture first and last OP value and what is the latest timestamp
    inputDFWithTS= IncrementalInputDF.withColumn("max_op_date",max(IncrementalInputDF.last_update_time).over(IDWindowDF))
    
    # Filter out new records that are inserted, then select latest record from existing records and merge both to get deduplicated output 
    NewInsertsDF = inputDFWithTS.filter("last_update_time=max_op_date").filter("op='I'")
    UpdateDeleteDf = inputDFWithTS.filter("last_update_time=max_op_date").filter("op IN ('U','D')")
    finalInputDF = NewInsertsDF.unionAll(UpdateDeleteDf)

    # Register the deduplicated input as temporary table to use in Iceberg Spark SQL statements
    finalInputDF.createOrReplaceTempView("incremental_input_data")
    finalInputDF.show()
    
    ## Perform merge operation on incremental input data with MERGE INTO. This section of the code uses Spark SQL to showcase the expressive SQL approach of Iceberg to perform a Merge operation
    IcebergMergeOutputDF = spark.sql("""
    MERGE INTO job_catalog.iceberg_demo.iceberg_output t
    USING (SELECT op, product_id, category, product_name, quantity_available, to_timestamp(last_update_time) as last_update_time FROM incremental_input_data) s
    ON t.product_id = s.product_id
    WHEN MATCHED AND s.op = 'D' THEN DELETE
    WHEN MATCHED THEN UPDATE SET t.quantity_available = s.quantity_available, t.last_update_time = s.last_update_time 
    WHEN NOT MATCHED THEN INSERT (product_id, category, product_name, quantity_available, last_update_time) VALUES (s.product_id, s.category, s.product_name, s.quantity_available, s.last_update_time)
    """)

    job.commit()
```