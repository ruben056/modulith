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
