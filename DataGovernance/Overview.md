# Data Governance

- Data Quality, Data Veracity, Drift Monitoring
- Data Catalog, Dictionary
- Data Lineage
- Metadata Management
- GDPR (Right to be Erasure, Right to be forgotten): 
How to have a data architecture that can accomplish this?
Examples of the challenges:
  - Data on the Data Lake usually not indexed -> searching for specific user data is full table scan.
  - Data Lake is append-only (object store) -> need to rewrite data partitions.
  - Read/ write consistency: without ACID, readers (e.g. downstream analytics/ .processing) 
  are impacted while user data is deleted, updated