

## Upsert

- immutable data store -> individual record is not updated via a write, 
updates are appended to a log, a pointer maintains most recent version of a record.

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