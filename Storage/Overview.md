# Storage Overview

## Data Lake Table Formats

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