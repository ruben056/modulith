## testing:

### create an order
```
// not transactional
time curl -XPOST -d'{"lineItems":[{"product":"1","quantity":"1"}]}' -H'Content-Type:application/json' http://localhost:8080/orders
// transactional
time curl -XPOST -d'{"lineItems":[{"product":"1","quantity":"1"}]}' -H'Content-Type:application/json' http://localhost:8080/orderstx
// voor error -> poison event testen: -> lijkt voorlopig subsequente correcte orders events niet te blokkeren, dus toch geen poison event?
time curl -XPOST -d'{"lineItems":[{"product":"1","quantity":"0"}]}' -H'Content-Type:application/json' http://localhost:8080/orderstx
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


# spring modulith start-jdbc toegevoegd
- mits property _spring.modulith.events.jdbc.schema-initialization.enabled=true_ worden de nodige tabellen(event_publication in geval van psql) voor eventing worden aangemaakt in de db
- bij testen blijkt volgende
  - als we gewoon @EventListener gebruiken, worden de events niet in de event_publication tabel opgeslagen
    - alles blijft hier dus werken zoals "voorheen"
  - als we echter @TransactionalEventListener gebruiken, worden de events wel in de event_publication tabel opgeslagen en wordt dus gebruikt gemaakt van de outbox pattern.
    - indien de event getriggered wordt zonder transactie context, zal gaat de event wel in de event_publication tabel terecht komen, maar zal deze niet verwerkt worden (de consumer wordt niet getriggerd) en dus ook geen "completion_date" krijgen.
      - TODO zitten we hier dan niet met poison events? -> blijkbaar niet, deze events komen wel in de tabel maar zijn nooit gescheduled geweest en blokkeren dan ook de daaropvolgende events niet.
      - dit zijn "orphan" events die nooit verwerkt zullen worden, maar ook niet blokkeren.
  - TODO error in transactionaleventlistener gooien en kijken of we een poison event hebben dat blokkeerd... 
    
