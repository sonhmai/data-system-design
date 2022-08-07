# Streaming with AWS

## Architecture

## Questions

## 1.1 Backfill


## Service Comparison

### Kinesis Data Streams vs MSK (Kafka)

Kinesis Data Streams

MSK (AWS-managed Kafka)

### Compute

Aspects to consider: CICD, testability, maintenance, observability, configurability, service integration

Kinesis Data Analytics (KDA)
- Pros
    - Serverless, Automatic Scaling based on CPU usage
    - Integration with Kinesis Data Streams
- Cons
    - Flink version might not be the latest. Must use the Flink, Scala, JDK, Beam, Python version that KDA runtime supports (Flink 1.13, JDK 11, Scala 2.12, Python 3.8, and Apache Beam v2.32 - Oct, 2021)
    - Some cluster configs are either fixed or not user-configurable.

Self-Managed Flink
- Pros
- Cons

AWS Lambda
- Cons
    - not suitable for stateful applications.

Container App (EKS/ Fargate)
- Pros
- Cons

Open Questions
- KDA Integration Testing: is spawning a Flink cluster for testing assure the test with KDA or is there any gotchas?

## References
