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

