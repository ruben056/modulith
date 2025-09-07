## testing:

### create an order
```
curl -XPOST -d'{"lineItems":[{"product":"1","quantity":"1"}]}' -H'Content-Type:application/json' http://localhost:8080/orders
```

### connect to db to verify tables:
- connect:
``` 
PGPASSWORD=secret psql -U myuser -h localhost mydatabase
```
- list tables:
```
\d
```
- query tables:
```
// do not forget the semicolon at the end of the query!!!
select * from orders;
select * from orders_line_items;
```


TODO: 
1. 2 ordercontrollers
-- one with @Transactional
-- one without @Transactional
  - verify different behaviour (@TransactionalEventListeners will not be triggered when not in @Transaction!!!!)

2. add spring modulith event toestand -> https://docs.spring.io/spring-modulith/docs/current/reference/htmlsingle/#_domain_events
hier beetje mee spelen

3. externalized events toevoegen via amqp (rabbitmq)  https://docs.spring.io/spring-modulith/docs/current/reference/htmlsingle/#_externalized_events
hier beetje mee spelen

