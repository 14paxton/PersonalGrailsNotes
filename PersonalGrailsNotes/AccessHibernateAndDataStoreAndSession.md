---
title:        AccessHibernateAndDataStoreAndSession
permalink:    PersonalGrailsNotes/AccessHibernateAndDataStoreAndSession
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

# -Access to session and hibernate-

## -getting session-

`def sessionFactory`

`def session = sessionFactory?.getCurrentSession()`

`RequestContextHolder.currentRequestAttributes().getSession()`

# -get hibernate datastore in session-

## -Get hibernatedatasource-

[hibernate datastore ex.](https://guides.grails.org/grails-dynamic-multiple-datasources/guide/index.html  )

    @Autowired
    HibernateDatastore hibernateDatastore

    @Autowired
    DatabaseProvisioningService databaseProvisioningService

    @Listener(User) 
    void onUserPostInsertEvent(PostInsertEvent event) { 
        String username = event.getEntityAccess().getPropertyValue("username")
        DatabaseConfiguration databaseConfiguration = databaseProvisioningService.findDatabaseConfigurationByUsername(username) 
        hibernateDatastore.getConnectionSources().addConnectionSource(databaseConfiguration.dataSourceName, databaseConfiguration.configuration) 
    }

## -get table columns-

hibernateDatastore.getSessionFactory().getClassMetadata(GroupCompare).getProperties().sort()

ctx.sessionFactory.getClassMetadata(Team).attributes.collect{it.name}

## -get data bindings/properties/class/domain table/declared fields-

`def mapping = org.grails.orm.hibernate.cfg.GrailsDomainBinder.getMapping(UserGroup)`

`sessionFactory.getClassMetadata(Foo).tableName`

`org.grails.orm.hibernate.cfg.GrailsDomainBinder.getMapping(groupCompare.class).class.declaedFields`

## -get a service-

    1. @Autowired HibernateDatastore hibernateDatastore
       UserDataService userDataService
     
        UserService(HibernateDatastore hibernateDatastore) {
            this.userDataService = hibernateDatastore.getService(UserDataService)
          }
    
    2. (YourService)Holders.grailsApplication.mainContext["yourService"]
    3. applicationContext."${yourServiceName}".serviceMethod()
    4. ctx.getBean('userGroupService')
    5. Holders.applicationContext.getBean("myService")
    6. ApplicationContext ctx = (ApplicationContext)ServletContextHolder.

       getServletContext().getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)

       statisticsService = (StatisticsService ) ctx.getBean("statisticsService ")