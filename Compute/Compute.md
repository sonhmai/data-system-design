# Compute

Mostly concerns with big data processing frameworks
- Spark
- Flink

Difference in threading model between Spark and Flink?
- Spark tries to optimize and to things in parallel in map-reduce style
- Flink offers the Dataflow model where parallelism happens from source to sink
per message, therefore you don't need to worry much about user function 
thread-safety (true? why?)

## Apache Flink

### Concepts

`Flink Cluster`
A distributed system consisting of (typically) one JobManager and 
one or more Flink TaskManager processes.

`Function`
Functions are implemented by the user and encapsulate the application logic of a Flink program. 
Most Functions are wrapped by a corresponding Operator.

`Job`
is the runtime representation of a logical graph (dataflow graph) created and submitted 
by calling execute() in a Flink app.

`JobManager`
- the orchestrator of a Flink Cluster.
- contains 3 components: Resource Manager, Dispatcher, one JobMaster per running Flink Job.

`Operator`
- Node of a Logical Graph (dataflow graph). An Operator performs a certain operation, 
which is usually executed by a Function. `Sources` and `Sinks` are special Operators 
for data ingestion and data egress.
- transform one DataStream to a new DataStream
- example: Map, FlatMap, Filter, KeyBy, Reduce, Window, Union, Window Join, **ProcessFunction**,...

`TaskManager`
TaskManagers are the worker processes of a Flink Cluster. 
Tasks are scheduled to TaskManagers for execution. 
They communicate with each other to exchange data between subsequent Tasks.

`Latency`
- indicates how to it takes for an event to be processed. t(processed) - t(received)
- streaming apps must provide results for incoming data as fast as possible.

`Throughput`
- how many events system can process per unit of time.
- streaming apps must be able to handle high ingest rates of events.

Lowering latency increases throughput. If a system can perform operations faster,
it can perform more operations per time unit.

### Data Types and Serialization
Flink places some restrictions on the type of elements that can be in a DataStream. 
The reason for this is that the system analyzes the types to determine efficient execution strategies.

7 data types
1. Java Tuples and Scala Case Classes
2. Java POJOs
3. Primitive Types
4. Regular Classes
5. Values
6. Hadoop Writables
7. Special Types

What is Type Descriptor?

Generic Type Extraction?

### Stateful operators and applications

What to use? 
- KeyedProcessFunction
- ProcessFunction
- ...

**Watermark**
- used to derive current event-time at each task in an event-time application.
Time-based operator (window e.g.) use this time to trigger computation and make progress.
- used to handle stream with out-of-record timestamps

**Checkpointing**
- While a task checkpoints state, it is blocked and task input is buffered -> `Synchronous` checkpoint
can increase latency and is a problem in low-latency apps. `Async` checkpoint is a solution but
must be supported by `StateBackend` (RocksDB and FileSystem).
- incremental checkpointing

**Savepoints**
- savepoint = checkpoint + metadata
- Flink does NOT auto take a savepoint, must be thru user or external scheduler
- Flink does NOT auto clean up savepoints
- Extra features compared to checkpoints
  - starting same app with different parallelism (scale up or down)
  - starting app on different cluster. Example use cases: updating Flink version + 
  migrating app to datacenter having lowest instance price.

**Stateful Apps Maintainability**
> Requirement: application state can be migrated to a new version of the app or rescaled to more
> or fewer operator tasks.

State backend: 
- storing local state of each task instance
and persisting state to remote storage when checkpoint is taken
- 3 provided by Flink
  - `Memory`: stores state locally on Task Manager JVM heap, checkpoints it to JobManager's heap.
  - `FileSystem`: stores state locally on TM JVM heap, checkpoints to remote fs.
  - `RocksDB`: stores state in local RocksDB, needs serde, checkpoints to remote fs.
  Durable but lower performance due to disk writing and serde time.
  - `custom StateBackend` can be implemented.

Important things re state
- choice of state backend
- size of application state
- config of checkpointing algo

Stateful operators implementation challenges
- State management: protect state from concurrent updates
- State partitioning
- State recovery

Preventing leaky state
- stateful operator must control state size and ensure it's not growing forever
- limit state size: maintain summary of events as state
- aggregation functions (max, min, minBy, sum,...) should only be used if 
key values range is bounded and constant.
- windows with time-based trigger (event and processing time) not affected
because they trigger and purge their state based on time.
- `problem`: want to clean stale key in state but function with `keyed state`
can only access state keyed by record key, how can it know to clean up?

    > should take app requirements and input data key domain into account when designing

- `solution`
  - registering timer in future and clean the state in callback.
  Timer registered in context of current key. 
  Example stateful KeyedProcessFunction that cleans its state
  - [cleaning state with TTL](https://issues.apache.org/jira/browse/FLINK-10471)

Evolving Stateful Apps


References
- [Application of Apache Flink in Real-time Financial Data Lake](https://www.alibabacloud.com/blog/application-of-apache-flink-in-real-time-financial-data-lake_597529)