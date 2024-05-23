# chapter 1 - my program is too slow

what are the steps to analyze and solve occasionally slow transaction problem?
```
1. identify which layer of code is slow.
2. identify what is the `interference` affecting it
3. fix that

prediction-observation-comparison loop
```

---
what is typically the `interference`?
```
Interference usually comes from sharing 5 fundamental resources
1. CPU
2. Memory
3. Disk/SSD
4. Network
5. Software critical section (sync mechanisms like lock multi-threaded programs)
```

It is important to have all necessary components to analyze performance
1. programmer expectation
2. observability tools to show actual metrics

Then compare those 2 things to know the reason.


![alt text](image.png)

Order of magnitude estimate

![alt text](image-1.png)

Terms
- `offered load`: #transactions sent per sec
- `service`: collection of programs handling 1 particular kind of trans
- `tail latency`: slowest trans in the latency probability distribution
