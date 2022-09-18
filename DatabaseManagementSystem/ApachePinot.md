# Apache Pinot

- A distributed, columnar OLAP data store. 
- When? user-facing, low latency analytics, data freshness in seconds to minutes, 
high concurrency queries due to serving user-facing applications 
(ecommerce seller/ sites analysis, social networks analytics for user 
e.g. who's seen your profile in LinkedIn)

## Architecture
- Controller
- Zookeeper
- Broker
- Server
- Minion (optional)

## Upsert

- immutable data store -> individual record is not updated via a write, 
updates are appended to a log, a pointer maintains most recent version of a record.

## Capacity Planning
(refer to ref 5)

Key Factors
1. Throughput
   - Read QPS -> #cores needed in `Server` (data serving/ processing)
   and `Broker` (query scatter/ gather)
   - Write QPS -> #cores needed for `Server` component to support **real time ingestion**
   - Num streaming partitions on data source -> `degree of parallelism`
   -> #cores in `Server` component
2. Data Size
3. Types of workloads/ queries
4. Number of tables and segments -> num and compute capacity of `Controller` and `Zookeeper`
5. Minion: sizing depends on num tasks, SLA, amount of data to be ingested/ upserted/ purged.
6. Environment
   1. HA for Compute: >=2 AZ for prod
   2. HA for Data: Replication Factor >= 3 for prod
   3. Segment Replica Groups: >=2 replica groups for prod

Sizing
1. Broker: 16 CPU, 64 GB RAM ~ 1000 QPS standard analytical queries
2. Server
   - recommended 1:1 ratio of (pinot consumers):(physical cores)
3. Controller and ZK: 16 CPU, 64 GB - 100k segment counts

## Monitoring & Alert

What to monitor?
- common things for stateless service: physical resource monitoring
- stateful things: data level metrics. How data is distributed, 
how usage patterns affect the query SLA and hence user experience?

Good questions to ask when there are issues?
- is resource usage high on all nodes (both system and JVM) or is it only a few nodes?

Data Metrics Monitoring
- most metrics are of tables (one series/table). 
- Examples:
  - Table Query Latency
  - Table Query QPS

Monitoring stack example
- activate Prometheus JMX exporter of Pinot JVM(s)
- use Prometheus to scrape the metrics exposed 
- plug Grafana to Prometheus for dashboards

Alerts

## References
1. [Upsert Design Revisited](https://docs.google.com/document/d/1qljEMndPMxbbKtjlVn9mn2toz7Qrk0TGQsHLfI--7h8)
2. https://github.com/kbastani/order-delivery-microservice-example
3. [Monitoring Pinot with JMX, Prometheus, Grafana](https://medium.com/apache-pinot-developer-blog/monitoring-apache-pinot-99034050c1a5)
4. [Helm & k8s - Monitoring Pinot using Prometheus and Grafana](https://docs.pinot.apache.org/operators/tutorials/monitor-pinot-using-prometheus-and-grafana)
5. [Capacity Planning in Pinot Part 1](https://www.startree.ai/blog/capacity-planning-in-apache-pinot-part-1)