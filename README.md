**Spring Demo Application**

Try to build api server over kafka transport

There are 3 profiles:
1. memory - demostrate simply piped structure from input to output without brokers
2. kafka - like a memory profile, but recieve and send result to kafka topics
3. kafka-2 - use pub/sub channels for business logic process, recieve and send result to kafka 


For start kafka use
> docker compose up -d

