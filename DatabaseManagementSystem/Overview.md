# Database Management System

## OLAP Data Store

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
