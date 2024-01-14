# Spark Code Snippets

Spark Structured Streaming

```python
data_schema = spark.table(f"glue_data_lake_raw_{schema}.{table}").schema

df = spark \
    .readStream \
    .format("cloudFiles") \
    .option("cloudFiles.format", "csv")\
    .option("cloudFiles.region", "eu-west-1")\
    .option("cloudFiles.includeExistingFiles", "true")\ 
    .option("badRecordsPath", f"s3://bad-records-bucket/raw_to_silver/{schema}/{table}/")\
    .schema(data_schema)\
    .load(f"s3a://data-lake-raw-bucket/{schema}/{table}/")

df.writeStream \
    .format("delta")\
    .option("checkpointLocation", f"s3://checkpoints-bucket/raw_to_cooked/{schema}/{table}/")\
    .partitionBy(["_year", "_month"])\
    .trigger(processingTime="1 minute")\
    .start(f"s3a://data-lake-cooked-bucket/{schema}/{table}/")
```