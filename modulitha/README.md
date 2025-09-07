## testing:

### create an order
```
// not transactional
curl -XPOST -d'{"lineItems":[{"product":"1","quantity":"1"}]}' -H'Content-Type:application/json' http://localhost:8080/orders
// transactional
curl -XPOST -d'{"lineItems":[{"product":"1","quantity":"1"}]}' -H'Content-Type:application/json' http://localhost:8080/orderstx
```

### connect to db to verify tables:
````- connect:
``` 
PGPASSWORD=secret psql -U myuser -h localhost mydatabase
```````
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

# SCENARIOS

# 2 endpoints voorzien
- /orders  -> without @Transactional
  - hier worden de gewone @EventListener gebruikt, maar de @TransactionalEventListener worden niet getriggerd omdat er geen transaction is bij het gooien van het event.
- /orderstx -> with @Transactional
  - hier worden zowel de gewone @EventListener als de @TransactionalEventListener getriggerd omdat er wel een transaction is bij het gooien van het event.

_conclusie_  : zolang we gewoon @EventListener gebruiken blijft alles werken zoals voorheen. 
Als we echter @TransactionalEventlistener will gebruiken voor het outbox pattern moeten we ervoor zorgen dat alle triggers in een transactie context zitten.
(alternatief is om fallbackExecution om true te zetten op de @TransactionalEventListener, maar dan verlies je de garanties van het outbox pattern)
**TODO ... Te bekijken wat dit heeft als we deze events in db zullen opslaan via modulith eventing ...**
