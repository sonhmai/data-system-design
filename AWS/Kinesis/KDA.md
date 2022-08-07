# Kinesis Data Analytics

## Autoscale
Autoscale behavior 

- When in auto scaling mode, the KDA will monitor the cluster for CPU usage. When CPU usage remains at 75 percent or above for 15 minutes, the KDA cluster will scale up. 
- In order to scaling up, current KDA app will be shutdown and replace by a new KDA cluster with more KPU.
    - This behavior creates a down time in few minutes range of that KDA App. 
    - **The down time period may affect SLA**

https://docs.aws.amazon.com/kinesisanalytics/latest/java/how-scaling.html

## Deployment
- Flink Deployment Mode: per-job mode (which is deprecated in Flink 1.15 and will be removed in the future versions).


- Flink UI Console does not have log, all log pushed to Cloudwatch.
