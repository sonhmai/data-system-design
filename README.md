# Data Systems Design and Implementation

Data System Design collection of notes and readings, blog posts.

## Data Consumption

Blogs
- [Traveloka - Data Lake API on Microservice Architecture using BigQuery](https://medium.com/traveloka-engineering/data-lake-api-on-microservice-architecture-using-bigquery-10d6e9c5ca8f)
  Best Practices: avoid giving direct access to data platform storage (object storage, database, etc.) as
  it creates a tight coupling to the underlying technology, format, etc. Instead, have an API
  layer in between to decouple that dependency. What's bad about direct access?
    - change coordination required between teams.
    - lack of access control (column, row levels).
    - lack of audit log (who access, download what).


## Data Processing
- [Agoda, How to Design and Maintain a High-Performing Data Pipeline](https://medium.com/agoda-engineering/how-to-design-maintain-a-high-performing-data-pipeline-63b1603b8e4a) 
  - Data pipeline scalability: SLA, partioning, data freshness, resource usage, scheduling, data dependency, monitoring.
  - Data quality: freshness, integrity (uniqueness e.g. no dup keys), completeness (e.g. no empty, NULLS), 
    accuracy (value is not abnormal by checking with previous trend, ThridEye), 
    consistency (source = destination, Quilliup, running when pipeline completes).
  - Ensuring data quality: validating before writing to destination, testing, monitoring, alerting, responding,
    automatic Jira tickets creation.
- [Idempotency Keys: How PayPal and Stripe Prevent Duplicate Payment](https://medium.com/@sahintalha1/the-way-psps-such-as-paypal-stripe-and-adyen-prevent-duplicate-payment-idempotency-keys-615845c185bf)


## Data Quality

Blogs
- [How Google, Uber, and Amazon Ensure High-Quality Data at Scale](https://medium.com/swlh/how-3-of-the-top-tech-companies-approach-data-quality-79c3146fd959)
- [Uber - Monitoring Data Quality at Scale with Statistical Modeling](https://www.uber.com/en-VN/blog/monitoring-data-quality-at-scale)
- [LinkedIn - Towards data quality management at LinkedIn](https://engineering.linkedin.com/blog/2022/towards-data-quality-management-at-linkedin)
- [Data Quality: Timeseries Anomaly Detection at Scale with Thirdeye](https://medium.com/the-ab-tasty-tech-blog/data-quality-timeseries-anomaly-detection-at-scale-with-thirdeye-468f771154e6)

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


## Integration

Blogs
- [How Agoda manages 1.8 trillion Events per day on Kafka](https://medium.com/agoda-engineering/how-agoda-manages-1-8-trillion-events-per-day-on-kafka-1d6c3f4a7ad1)


## Distributed Systems
- [Patterns of Distributed Systems. Unmesh Joshi](https://www.amazon.com/Patterns-Distributed-Systems-Addison-Wesley-Signature/dp/0138221987)


## Machine Learning Platform
- [Featureflow: Democratizing ML for Agoda](https://medium.com/agoda-engineering/featureflow-democratizing-ml-for-agoda-aec7a6c45b30)
  - Challenge: time-consuming feature analysis, training, validation vs fast changing customers and competitors in travel industry;
  lacking of consistency from analysis to training, from feature development to deployment.
  - Solution: Featureflow with components (UI, data pipeline, monitoring, sandbox env, experiment platform)
  - Result: feature analysis reduced from a week to a day, quarterly experiments increased from 6 to 20, 
  feature contributors from ~3 to ~50, larger feature pool, more robust feature screening process.


## System Design Resources
  - https://github.com/karanpratapsingh/system-design
  - https://github.com/donnemartin/system-design-primer
  - https://gist.github.com/vasanthk/485d1c25737e8e72759f 


## Interview Guides
- [Preparing for Interview at Agoda](https://medium.com/agoda-engineering/preparing-for-interview-at-agoda-2c07b7d13ca5): 
Clear guide for the interview process at Agoda with advices for candidates in each stage.

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
