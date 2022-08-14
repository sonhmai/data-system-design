
processing and delivery guarantees

- Exactly Once (EO)
- At Most Once (AMO)
- At Least Once (ALO)

Failure Recovery Guarantees

### If a worker goes down, how is Flink state affected?

- Deactivated checkpoints - At most once
- At least once
- Exactly once
    - **does not mean that events are processed exactly once**
    - it just means that the effect of their processing is consistent with the state

When to use at least once over exactly once?
- Some sources do not support EO
    - Google PubSub is only at least once
- Some jobs does not need EO e.g. if all stateful operations are idempotent.

### End-to-end EO

If a worker goes down, **how are downstream systems affected?**

ALO and EO requires
- replayable sources so that data can be replayed in case of failure

E2E EO requires
- transactional sink
- or idempotent writes

# References 

- Video, [Streaming Concepts & Introduction to Flink](https://www.youtube.com/watch?v=9pRsewtSPkQ)