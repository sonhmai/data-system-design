
# Transaction Count

Design a system that reports the transactions count in near real time.

## Requirement Clarification
---

- what does it mean by near real time? `freshness under 1 min, latency 30s`
- expected number of users? `500k MAU, expected to double every year in 3 years (4M)`
- expected Transactions Per Second (TPS) of incoming data ? `10k/ seconds peak`
- expected load shape (TPS throughout the day)? `10k TPS peak, normally 2k TPS throughout the day, at night GMT+7 very limited`
- expected Queries Per Second (QPS) of the serving side ? `5k/ seconds`
- expected Delivery Guarantee? `exactly once end-to-end`
- what does end-to-end mean? `from Kafka to Analytics Serving`
- expected analytics?

top 10 accounts that a user received money from and the amount

| from_user_id | transaction_count | transaction_amount
| --- | --- | ---
| 111 | 100 | 100,000,000
| 222 | 50  | 50,000,000
| ... | ... | ...

- need data updates, deletes?


## High-level Design
---

### Sizing

- Quantity: ```4M users * 10 transactions / user / day  
    = 40M transactions per day
    = 1.2B transactions per month
    = 14.6B transactions per year```

### Architecture
Message queue => Dedup, Preprocessing => Serving Data Store => Analytics Serving App 

- Message queue: Kafka
- Dedup and Preprocessing: Flink
- Serving Data Store: Pinot
- Analytics Serving App: stateless JVM or Go app running on Kubernetes

Transaction event
```json
{
    "from_account_id": "111111",
    "to_account_id": "222222",
    "transaction_id": "123e4567-e89b-12d3-a456-426614174000",
    "amount": "1000000.5"
}
```

### Serving Data Model

**Option 1**: Store all transaction events in Pinot
- Pros
    - Aggregation within any timeframe
- Cons
    - Duplication with transaction OLTP data store
    - Storage cost

```SQL
-- top 10 account by total transaction amount
select 
    from_account_id,
    count(amount) as transaction_count,
    sum(amount) as transaction_amount
group by from_account_id, to_account_id
having to_account_id="11111"
order by transaction_amount desc
limit 10;
```

**Option 2**


### Why Pinot?

Other options would be
- In memory store such as Redis
- SQL database such as Postgres
- other OLAP database such as Clickhouse

## Detailed Design

