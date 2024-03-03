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

### Monitoring
- What are the critical things to monitor?