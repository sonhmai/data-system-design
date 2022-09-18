# Data Systems Design and Implementation

Data System Design notes. Wide-range topics
- Data Lake
- Data Governance
- LakeHouse
- Streaming Data System
- Data Engineering, Data Ops for Machine Learning

Structure - still finding a good structure
```
Cloud-specific: info, design considerations, pros and cons of cloud-specific services
The Rest: generic design info and notes, self-explained
```

## Basics

- What is ACID?
    - Atomicity: A transaction should either complete successfully or just fail. There should not be any partial success.
    - Consistency: A transaction will bring the database from one valid state to another state.
    - Isolation: Every transaction should be independent of each other i.e., one transaction should not affect another.
    - Durability: If a transaction is completed, it should be preserved in the database even if the machine state is lost or a system failure might occur.

- Why need ACID on the data lake?

## Demos

TODO

## References
- System Design Resources
  - https://github.com/karanpratapsingh/system-design
  - https://github.com/donnemartin/system-design-primer
  - https://gist.github.com/vasanthk/485d1c25737e8e72759f 
