# Data Systems Design and Implementation

Data System Design collection of notes and readings, blog posts.

## Integration

Blogs
- [How Agoda manages 1.8 trillion Events per day on Kafka](https://medium.com/agoda-engineering/how-agoda-manages-1-8-trillion-events-per-day-on-kafka-1d6c3f4a7ad1)


## Data Consumption

Blogs
- [Traveloka - Data Lake API on Microservice Architecture using BigQuery](https://medium.com/traveloka-engineering/data-lake-api-on-microservice-architecture-using-bigquery-10d6e9c5ca8f)

Best Practices
- avoid giving direct access to data platform storage (object storage, database, etc.) as
it creates a tight coupling to the underlying technology, format, etc. Instead, have an API
layer in between to decouple that dependency. What's bad about direct access?
  - change coordination required between teams.
  - lack of access control (column, row levels).
  - lack of audit log (who access, download what).


## Data Quality

Blogs
- [How Google, Uber, and Amazon Ensure High-Quality Data at Scale](https://medium.com/swlh/how-3-of-the-top-tech-companies-approach-data-quality-79c3146fd959)
- [Uber - Monitoring Data Quality at Scale with Statistical Modeling](https://www.uber.com/en-VN/blog/
monitoring-data-quality-at-scale)
- [LinkedIn - Towards data quality management at LinkedIn](https://engineering.linkedin.com/blog/2022/towards-data-quality-management-at-linkedin)

Papers
- [VLDB, Amazon - Automating Large-Scale Data Quality Verification](https://www.vldb.org/pvldb/vol11/p1781-schelter.pdf). It presents the design choices and architecture of a production-grade system for checking data quality at scale, shows the evaluation result on some datasets.

Best Practices
- too little data quality alerts let important issues go unresolved.
- too many alerts overwhelm owners and cause them to not fix the most important ones.
- statistical modeling techniques (PCA, etc.) can be used to reduce computation need.
- separate anomaly detection from anomaly scoring and alerting strategy.

Definition
- issues in `metadata category`  -> can be obtained without checking dataset content
  - data availability
  - data freshness
  - schema changes
  - data completeness (volume deviation)

- issues in `semantic category`(dataset content) -> needs data profiling
  - column value nullability
  - duplication
  - distribution
  - exceptional values
  - etc.


## System Design Resources
  - https://github.com/karanpratapsingh/system-design
  - https://github.com/donnemartin/system-design-primer
  - https://gist.github.com/vasanthk/485d1c25737e8e72759f 


## Databricks

### Delta Lake
- [Managing Recalls with Barcode Traceability on the Delta Lake](https://www.databricks.com/blog/managing-recalls-barcode-traceability-delta-lake)
- [Creating a Spark Streaming ETL pipeline with Delta Lake at Gousto](https://medium.com/gousto-engineering-techbrunch/creating-a-spark-streaming-etl-pipeline-with-delta-lake-at-gousto-6fcbce36eba6)
  - issues and solutions
    - costly Spark op `MSCK REPAIR TABLE` because it needs to scan table' sub-tree in S3 bucket. 
      -> use `ALTER TABLE ADD PARTITION` instead.
    - not caching dataframes for multiple usages. 
      -> use cache
    - rewriting all destination table incl. old partitions when having a new partition. 
      -> append new partition to destination.
    - architecture (waiting for CI, Airflow triggering, EMR spinning up, job run, working with AWS console for logs) slowing down development. Min feedback loop of 20 minutes.
      -> move away from EMR, adopt a platform allowing to have complete control of clusters and prototyping.
  - Databricks Pros
    - Reducing ETL time, latency from 2 hours to 15s by using streaming job and delta architecture.
    - Spark Structured Streaming Autoloader helps manage infra (setting up bucket noti, SNS and SQS in the background).
    - Notebook helps prototype on/ explore production data, debug with traceback and logs interactively. Then CICD to deploy when code is ready.
      This helps reduce dev cycle from 20 mins to seconds.
    - Costs remain the same as before Databricks. (using smaller instances with streaming cluster, which compensated for DBx higher costs vs EMR).
    - Reducing complexity in codebase and deployment (no Airflow).
    - Better ops: performance dashboards, Spark UI, reports.
  - Extra
    - DBT used to manage data modeling, ETL from cooked data to Redshift which is used by BI tools and analysts, create SSOT for biz.
  - Watch out
    - Need to create schema in Catalog before running as Spark SS and Autoload cannot infer schema at this moment.

- [Data Modeling Best Practices & Implementation on a Modern Lakehouse](https://www.databricks.com/blog/data-modeling-best-practices-implementation-modern-lakehouse)


### Backfilling
- https://docs.databricks.com/en/ingestion/auto-loader/production.html#trigger-regular-backfills-using-cloudfilesbackfillinterval
- https://community.databricks.com/t5/data-engineering/how-to-make-structured-streaming-with-autoloader-efficiently-and/td-p/47833
- [Autoloader start and end date for ingestion](https://community.databricks.com/t5/data-engineering/autoloader-start-and-end-date-for-ingestion/td-p/45523)
