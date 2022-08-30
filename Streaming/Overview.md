# Streaming Processing

## TODO
- What are the options to deal with the state when it's getting bigger than memory when using Flink? Write a detailed example on a category (financial transaction, ads, anything else?...)
- Monitoring

## Design Questions
What questions to ask when desining a streaming pipelines/ systems?
- Input Throughput
- QPS
- Query Latency: how much at which percentile (<100ms at P95)?
- Data Freshness
- Delivery/ Processing Semantics (at most, at least, exactly once)
- Scalability
- Event time vs processing time
- Fault Tolerance
- Partitioning Strategy
- Does order matter?
- What are the important things to monitor for?
  - Lagging
  - What else?
- Is there potentially a data skew problem of incoming input from sources? How is it affecting Keyed State? If yes, think about how to solve it.
  - In some cases, there are ways to work around this limitation. For example, if you are computing analytics (e.g., you want to count page views per page per minute, and one page gets 95% of the page views), you can do pre-aggregation -- split the work for the hot key across several parallel instances, and then do one final, non-parallel reduction of the partial results. (This is just standard map/reduce logic.)
- What is the key of the data stream to partition the state?
- How long is the retention of the state? How much to set TTL?

## Exactly Once
- Duplicates can not be avoided at producer side
- Storing deduplication ID in Flink state for deduplication (transaction ID, UUID,...)
- Use Flink Exactly-Once Two-Phase Commit

Pitfalls
- False sense of security. Data can still be loss before the exactly-once processing pipeline.


## Sink
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

## Monitoring

What to monitor for the services below?

- Flink
- Kafka
- Pinot
- Clickhouse

## Managing Flink State
- Streaming joins
- Support arbitrarily late-arriving updates for any side of a join

## References
- https://www.uber.com/en-VN/blog/real-time-exactly-once-ad-event-processing/
- https://www.slideshare.net/FlinkForward/exactlyonce-financial-data-processing-at-scale-with-flink-and-pinot
- https://www.reddit.com/r/bigdata/comments/pse4gb/clickhouse_and_apache_pinot/