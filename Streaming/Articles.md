
# Articles
- https://www.uber.com/en-VN/blog/real-time-exactly-once-ad-event-processing/
- https://www.slideshare.net/FlinkForward/exactlyonce-financial-data-processing-at-scale-with-flink-and-pinot
- https://www.reddit.com/r/bigdata/comments/pse4gb/clickhouse_and_apache_pinot/

# Uber Ad Exactly Once Processing

Notes from reading the article.

Components
- Aggregation Job (Flink): cleansing, persistence for order attribution, aggregation, record UUID generation.
- Attribution Job (Flink): 
    - order filtering
    - order enriching with ad events + order details from external service
    - assign record UUID for Pinot and Hive idempotency
    - sink data to Attributed Orders Kafka topic 
- Union and Load Job (Flink)
    - union events from multiple regions
- Ads Metrics (Kafka): output topic, data sinks to Pinot for merchant-facing workload and Hive for ML workload
- Data Warehouse (Hive)
- OLAP Data Store (Pinot)

How do Flink jobs achieve scalability?


How to read only committed events written to Kafka by upstream Flink jobs?

    ```
    Consumers of the Kafka topic (e.g., Ad Budget Service and Union & Load Job) are configured to read committed events only. This means that all uncommitted events that could be caused by Flink failures are ignored. So when Flink recovers, it re-processes them again, generates new aggregation results, commits them to Kafka, and then they become available to the consumers for processing.
    ```

How is exactly-once ensured in downstream storage? `adding UUID to upstream record`
- Hive: adding UUID to records in Kafka, use them for deduplication
- Pinot: use upsert feature with incoming records having UUID

Deduplication in Aggregation Job/ Data Cleasing
-  Keyby operator we partition the data into logical groupings
- Map operator to deduplicate events from the input stream. leverage Flink’s keyed state in the deduplication mapper function to keep track of previously seen events

How can Pinot UPSERT feature avoid race condition when ingesting multiple Kafka topics/ partitions if based-on a deduplication key (UUID for example)?

What are the things to care for when using Pinot UPSERT table?
- `Create Kafka topic with more partitions`: more Kafka partitions = more Pinot table partitions -> more server distribution, more horizontal scalability.
- `Capacity planning`: A simple way is to measure the amount of the primary keys in the Kafka throughput per partition and time the primary key space cost to approximate the memory usage.

Regarding Pinot capacity planning, what is the window/ limit we need to use? If measuring the unbounded primary keys per Kafka partition, then this is unlimited which do not make sense.
- `TODO`
