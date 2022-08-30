

# OLAP Data Store
 
Overview
- When? OLAP queries: low-latency, data freshness in seconds to minutes, high concurrency queries due to serving user-facing applications (ecommerce seller/ sites analysis, social networks analytics for user e.g. who's seen your profile in LinkedIn)
- Usually in user-facing analytics, real-time analytics

Options
- Apache Pinot
- Clickhouse
- Druid


- Apache Pinot
  - Java
  - distributed OLAP datastore
  - designed for low latency delivery of analytical queries and supports near-real-time ingestion thru Kafka.

- Clickhouse
  - C++

Comparison
- Pinot requires more Devops since it requires more separately-deployed components
- Multi tenancy more easily support with Pinot
- Adding Nodes: Clickhouse requires more manual work for data balancing

# Data Lake Table Formats

Features
- ACID
    - Atomic: updates or appends to the lake don't fail midway and leave data in corrupted state.
    - Consistent: prevents reads from failing or returning incomplete results during writes, handling concurrent writes.
- Schema Evolution
- Upsert
- Time Travel

Options
- Apache Iceberg (originally from Netflix)
- Apache Hudi (originally from Uber)
- Delta Lake ((originally from Databricks)

Integration
- Iceberg
    - Both reading & writing: Spark incl. Structured Streaming, Flink
    - Only reading: Hive, Trino (Presto)
- Delta Lake
    - deep integration with Spark for both reading + writing
    - read support available for Presto, Athena, Redshift Spectrum, Snowflake using Hive's SymlinkTextInputFormat

Update Performance
- Hudi
    - trade-offs between 2 table types
        - Copy on Write - updates are written to parquet files directly, better for read heavy
        - Merge on Read - updates written to row-based log file and periodically merged into columnar parquet.Better for write heavy
- Iceberg
    - copy-on-write
- Delta 
    - Delta table needs periodic compaction processes for small Parquet files 

Concurrency

Optimistic Concurrency Control (OCC), MVCC (Multi Version Concurrency Control)
- Hudi
- Iceberg
    - OCC by doing atomic swapping operation on metadata files during updates.
    - How: creates new table snapshot, Compare-and-Swap (CAS) on current snapshot ID
- Delta 
    - OCC 
     
When with what?
- Hudi
    - supports many query engines
    - supported by AWS, preinstalled in EMR
- Iceberg
    - painpoint is metadata - best when reading huge tables on an object store (>10k partitions) -> solve S3 object listing or Hive metastore parition enumeration.
- Delta Lake
    - using Spark or Databricks customer
    - low write throughput without Delta Engine (built on top of Spark, not open source)


TODO - where does Hive metastore fit in the above picture?