# Zookeeper

## High Level Questions
> Distributed system coordination services like `Google Chubby` existed before `Zookeeper`. Why did Zookeeper need to be built? What problems will it solve that the others cannot?
- Custom coordination mechanism. Existing services provide only a predefined set of coordination primitives/ mechanisms. Zookeeper allow clients to create their own by offering a more generic APIs: hierarchical namespace like filesystem, znodes and watches.
- `Damage perimeter`. Existing services use blocking primitives like locks where slow or faulty clients can affect fast, normal clients.
- `Wait-free data objects`. Zookeeper chooses the trade-off of lock-free for a more relaxed consistency guarantee to avoid the above problem.


> What are the main APIs that Zookeeper provide to client to enable above features?
1. `create()` znode in a namespace
2. `delete()` znode from a namespace
3. `exists()` check if znode exists at a specified path

> What are the functional requirements of Zookeeper?
- design a service to coordinate (distributed) processes.
- client-server API must let clients create custom coordination primitives. (not giving them a predefined set e.g. locking)

> What are the non-functional requirements of Zookeeper?
- highly available: one process in the cluster down does not affect the cluster.
- scalable: more load can be handled by adding more nodes.
- fast recovery: when a node comes back up after a failure, its state must be recovered fast.

> What are the main design decisions and why?
- Client requests are executed in FIFO mode
- Write requests are linearizable


## Detailed Design Questions
> How does Zookeeper achieve high availability?
- By replicating data to all the servers in the cluster.
- If a follower server fails, reads are forwarded to another follower.
- If a leader server fails, a `leader election` process is started amongst the rest.
- When a failed ZooKeeper leader comes back up after a new leader has already been elected, the recovered node rejoins the ensemble as a follower.

> What consistency guarantee Zookeeper offers?
- https://zookeeper.apache.org/doc/current/zookeeperInternals.html#sc_consistency

> How does Zookeeper achieve scalable read?
- Reads are distributed to every server.
- Serving read request is very fast because it is just a read from the in-mem database without consensus with other nodes needed.
- Increasing read loads can be dealt with by adding follower servers.

> What are the trade-offs of fast reads design decision?
- reads are not linearizable and can return stale data in case a more recent write to the read znode has been committed.
- to prevent stale read, client calls `sync` and then read.

> How does Zookeeper achieve scalable write?
1. `vertical scaling` of leader (need to be all servers since leadership can rotate).
2. `sharding` the znode space amongst several Zookeeper ensemble/cluster. For each shard then we have separate leader. Cons is to implement discovery (which znode is in which cluster) in client-side.

> How does Zookeeper achieve fast failure recovery for a node?
- keep a Write-Ahead-Log (WAL) if committed operation and generate periodic snapshots of the in-mem db.
- log updates to disk, force writes to be on disk before applied to in-mem db.


> What is the read path for a client?
1. Client sends `read request` to only 1 server in the ensemble.
2. The client-facing server reads the state of local database in memory and replies.
3. Reads therefore can be stale (not linearizable).

> What is the write path for a client?
1. Client sends write request to any server in the ensemble.
2. Server (if it is a follower) forwards the request to the leader.
3. Leader processes the request and sends it to a quorum of servers.
4. Quorum of servers acknowledges the proposal.
5. Leader commits the proposal and delivers any messages if present.

> Why need to follow this write sequences?
- To make sure that write requests are linearizable, meaning that they happen atomically between request and its execution.

> What is ZAB?
- ZooKeeper Atomic Broadcast (ZAB) is a consensus protocol used by ZooKeeper for replicating and ordering changes across its ensemble of servers.

> What can be the benchmarking metrics for a system like Zookeeper?
- throughput and request latency.


## Zookeeper and Kafka
> If Zookeeper is built according to those reasons, why is `Kafka` removed it as a dependency?
- Pros
  - Simplifying Architecture, Deployment, Operations
  - Centralizing Security and ACL Management
  - single-node mode for Kafka is possible
- Cons
  - complexity in migration, new operational model.
  - compatibility with existing clients working with Zk directly.


> What are the use cases of Zookeeper in Kafka?
1. Cluster Membership Management
   - Zookeeper stores all brokers in the cluster.
   - when a broker starts, it registers itself in Zookeeper.
   - why? to keep track of which brokers are in the cluster to manage health and scalability.
2. Topic Configuration Management
3. Leader Election for Partitions
4. Storing Access Control Lists (ACL)
5. Storing Quota Management

##  Diagrams

### From [Zookeeper paper](https://www.usenix.org/legacy/event/atc10/tech/full_papers/Hunt.pdf) and website

![img.png](zk_cluster.png)

![img.png](zookepper_hierarchical_namespace.png)

![img_1.png](zookeeper_components.png)

### From [educative system design, Zookeeper introduction](https://www.educative.io/courses/grokking-the-principles-and-practices-of-advanced-system-design/introduction-to-zookeeper)

![img.png](zk_concept_map.png)


## References
- [KIP-500: Replace ZooKeeper with a Self-Managed Metadata Quorum](https://cwiki.apache.org/confluence/display/KAFKA/KIP-500%3A+Replace+ZooKeeper+with+a+Self-Managed+Metadata+Quorum)