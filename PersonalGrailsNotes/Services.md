---
title:        Services
permalink:    PersonalGrailsNotes/Services
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

# Transactions

> An instance of ```TransactionStatus``` is available by default in Grails transactional service methods.
> the keys in the Map must correspond to properties
> of org.springframework.transaction.support.DefaultTransactionDefinition

```java
     Account.withTransaction([propagationBehavior:TransactionDefinition.PROPAGATION_REQUIRES_NEW,isolationLevel:TransactionDefinition.ISOLATION_REPEATABLE_READ]){
        ...
        }

```

## Personal Use Case

- [Using TransactionStatus in a Service](https://gist.github.com/14paxton/a212d86552b05b95ef91ee444197fd4e)

## Resources

### Articles

- [Transaction Management](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction)

### Grails Docs

- [Transactions Rollback and the Session](https://docs.grails.org/latest/guide/services.html#transactionsRollbackAndTheSession)
- [GORM Programmatic Transactions](http://gorm.grails.org/6.0.x/hibernate/manual/index.html#programmaticTransactions)

### Java Docs

- [Spring Interface TransactionStatus](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/TransactionStatus.html)
- [Spring Interface SavepointManager](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/SavepointManager.html)