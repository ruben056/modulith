# Modulith sandbox
Scenario's aftesten voor wat mogelijk met spring modulith. 
Vooral met focus op de outbox pattern implementatie dat door spring modulith eventing aangeleverd wordt.

# Docker compose file:
- run
```
podman-compose -f compose.yaml up
```
- connect to psql:
``` 
PGPASSWORD=secret psql -U myuser -h localhost mydatabase
```
- rabbitmq admin console  (myuser/secret):
  http://localhost:15672/#/


# Modules:

## modulitha
[README.md](modulitha/README.md)

...




# TODO:
- [x] 2 ordercontrollers
  - one with @Transactional
  - one without @Transactional
    - verify different behaviour (@TransactionalEventListeners will not be triggered when not in @Transaction!!!!)
- [ ] vorige wordt idealiter "gedocumenteerd" dmv  integratietesten...
- [ ] add spring modulith event toestand -> https://docs.spring.io/spring-modulith/docs/current/reference/htmlsingle/#_domain_events
   hier beetje mee spelen  _IN PROGRESS_

-[ ] strategy voor poison events -> https://docs.spring.io/spring-modulith/docs/current/reference/htmlsingle/#_handling_poison_events
   (als consumer bug bevat of error heeft, kan de hele eventstream geblokkeerd geraken...)
 (blijkbaar enkel relevant voor @TransactionalEventListener (en dus @ApplicationModuleListener) , de gewone @EventListener worden niet in event_publication tabel opgeslagen en dus ook niet via outbox pattern verwerkt)
   
   
-[ ] strategy voor opkuisen van events uit tabel uiteindelijk
-[ ]  externalized events toevoegen via amqp (rabbitmq)  https://docs.spring.io/spring-modulith/docs/current/reference/htmlsingle/#_externalized_events
   hier beetje mee spelen
