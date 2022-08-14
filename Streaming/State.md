
# [Storing State Forever](https://www.youtube.com/watch?v=tiGxEGPyqCg&t=66s)

## Late-Arriving Updates
- Order edits
- Order deletion
- Refunds

Often, joining stream happens in Window (e.g. tumbling window of 1 hour).
When joining late-arriving updates, we don't have all the original events to join with, for example there is an Order update event where we don't have the sale event anymore to join with.

## Joining Streams

Global Window vs Custom Non Temporal Join
- GW less control over state (no StateTtlConfig or Timers)

External State Lookup vs Stateful Operator
- much slower
- can get complicated (e.g. keeping state in-mem for fast query leads to having to deal with in-mem cache and data store syncing)

## Next Steps
- scaling state is not the biggest problem, but savepoint recovery is
- state optimizations
    - for immutable sources (ledger,...)
        - accumulate all sides of the join
        - emit result and clear the state

    - product tradeoffs to reduce state (e.g. only support deletes in the last month)
- state with TTL
- when receiving late-arriving record without accumulated state: backfill state for the current key and re-emit the join (can be implemented using compacted Kafka topics)
