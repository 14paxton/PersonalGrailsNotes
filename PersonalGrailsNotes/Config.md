---
title:        Config
permalink:    PersonalGrailsNotes/Config
category:     PersonalGrailsNotes
parent:       PersonalGrailsNotes
layout:       default
has_children: false
share:        true
shortRepo:
  - personalgrailsnotes
  - default
---


<br/>

<details markdown="block">
<summary>
Table of contents
</summary>
{: .text-delta }
1. TOC
{:toc}
</details>

<br/>

***

<br/>

# Config

## Datasource

Adding DB connection

In application.yml you can add to dataSource above environments to establish connection

```yml
dataSource:
  pooled:          true
  jmxExport:       true
  driverClassName: com.mysql.jdbc.Driver
  username:        root
  password:        'computer'

  Or/and update update an  environment

environments:
  development:
    dataSource:
      dbCreate: update
      url:      jdbc:mysql://localhost:3306/mytest





production:
  dataSource:
    pooled:          true
    dbCreate:        update
    url:             jdbc:mysql://localhost:3306/jobboard
    driverClassName: com.mysql.jdbc.Driver
    dialect:         org.hibernate.dialect.MySQL5InnoDBDialect
    username:        root
    password:        'computer'
    properties:
      jmxEnabled:                    true
      initialSize:                   5
      maxActive:                     50
      minIdle:                       5
      maxIdle:                       25
      maxWait:                       10000
      maxAge:                        600000
      timeBetweenEvictionRunsMillis: 5000
      minEvictableIdleTimeMillis:    60000
      validationQuery:               SELECT 1
      validationQueryTimeout:        3
      validationInterval:            15000
      testOnBorrow:                  true
      testWhileIdle:                 true
      testOnReturn:                  false
      jdbcInterceptors:              ConnectionState
      defaultTransactionIsolation:   java.sql.Connection.TRANSACTION_READ_COMMITTED

 ```

- For dbCreate there are 4 values

create - On startup of your application, this will drop and recreate your schema. This will make sure that you will always have a clean table structure, and all your data is reset on every startup. This is ideal when you are in the early stages of the project and heavily modifying your data model. You will need to spend more time on your Bootstrap though, to create all background data that you need.

create-drop - This will behave exactly like create when starting up your application, with addition that it will drop all your tables when the application is shut down properly or gracefully. I have no idea why you would need to do this. I would prefer the create method, because you still have the chance to inspect your database after stopping your application.

update - This will not your schema or any of your tables, but will instead try to synch the database with your current data model.
This is done by adding the missing tables or columns to your database.
In my testing, this will not drop tables or columns from the database when you removed their corresponding items in the data model.
It does not guarantee a clean table representation of your data model.
This however is ideal when you are in the middle of development, where it is not practical to
put most of your test data in the Bootstrap.

validate - this will not alter your database, but will just compare your data model with the database on start up. And create warnings if necessary. This is ideal when deploying to production environment.

- Add the mysql deps as runtime in the dependencies of your build.gradle. E.g.

- runtime 'mysql:mysql-connector-java:5.1.36' 