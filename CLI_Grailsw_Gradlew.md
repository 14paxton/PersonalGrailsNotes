# Grailsw
### Liquibasehttps
[Liquibase grails plugin](//grails-plugins.github.io/grails-database-migration/3.0.x/index.html)

#### clear liquibase checksums
```bash
./grailsw dbm-clear-checksum
```
#### clear liquibase locks
```bash
 grails dbm-release-locks
```

#### ignore checksums in liquibase
> add to xml or groovy
 ```groovy
   validCheckSum 'any'
 ```
