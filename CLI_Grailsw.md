-   [Grails](#grails){#toc-grails}
    -   [Set System Props](#set-system-props){#toc-set-system-props}
    -   [Testing](#testing){#toc-testing}
        -   [](#section){#toc-section}
-   [Liquibase](#liquibase){#toc-liquibase}
    -   [clear liquibase
        checksums](#clear-liquibase-checksums){#toc-clear-liquibase-checksums}
    -   [clear liquibase
        locks](#clear-liquibase-locks){#toc-clear-liquibase-locks}
    -   [ignore checksums in
        liquibase](#ignore-checksums-in-liquibase){#toc-ignore-checksums-in-liquibase}

# Grails

## Set System Props

``` bash
    ./grailsw run-app -Dsample.message=cool
```

## Testing

### 

``` bash
./grailsw test-app 'com.talentbank.core.UserServiceAPISearchSpec.manager_query*' -unit
```

# Liquibase

> [Liquibase grails
> plugin](//grails-plugins.github.io/grails-database-migration/3.0.x/index.html)

## clear liquibase checksums

``` bash
./grailsw dbm-clear-checksum
```

## clear liquibase locks

``` bash
 grails dbm-release-locks
```

## ignore checksums in liquibase

> add to xml or groovy
>
> ``` groovy
> validCheckSum 'any'
> ```