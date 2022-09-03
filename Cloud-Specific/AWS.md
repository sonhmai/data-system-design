# Amazon Web Services

## Data Governance
- Third-party vendor: Collibra

## Data Quality
- Deequ

## Machine Learning
Books
- [Data Science on AWS: Implementing End-to-End, Continuous AI and Machine Learning Pipelines](https://www.amazon.com/dp/1492079391)

Articles
- ...

## Streaming 

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


### Kinesis Data Analytics

#### Autoscale
Autoscale behavior

- When in auto scaling mode, the KDA will monitor the cluster for CPU usage. When CPU usage remains at 75 percent or above for 15 minutes, the KDA cluster will scale up.
- In order to scaling up, current KDA app will be shutdown and replace by a new KDA cluster with more KPU.
    - This behavior creates a down time in few minutes range of that KDA App.
    - **The down time period may affect SLA**

https://docs.aws.amazon.com/kinesisanalytics/latest/java/how-scaling.html

#### Deployment
- Flink Deployment Mode: per-job mode (which is deprecated in Flink 1.15 and will be removed in the future versions).


- Flink UI Console does not have log, all log pushed to Cloudwatch.
