---
title:        Testing
permalink:    PersonalGrailsNotes/Testing
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

- [Config in test](#config-in-test)
    - [Modify config](#modify-config)
    - [Get at
      application](#get-at-application)
    - [Use db in memory to run
      tests](#use-db-in-memory-to-run-tests)
    - [Mocking service and then method call, setting dummy data for
      the return(put in test
      method)](#mocking-service-and-then-method-call-setting-dummy-data-for-the-returnput-in-test-method)
    - [Mocking Service used in a service you are testing(put at
      beginning of the test
      class)](#mocking-service-used-in-a-service-you-are-testingput-at-beginning-of-the-test-class)
    - [Mocking Method in service you are
      testing](#mocking-method-in-service-you-are-testing)
    - [Mocking method in
      domain](#mocking-method-in-domain)
    - [Using Test Data from BuildTest
      plugin](#using-test-data-from-buildtest-plugin)
        - [Snippet from spock
          test](#snippet-from-spock-test)
- [Configurations](#configurations)
    - [Checking validity of
      constraints](#checking-validity-of-constraints)
    - [check if method was called for another
      service](#check-if-method-was-called-for-another-service)
    - [check if method was called for same
      service](#check-if-method-was-called-for-same-service)
    - [create an
      exception](#create-an-exception)
    - [catch exception](#catch-exception)
    - [modify config during/for
      test](#modify-config-duringfor-test)
    - [create a custom manager for a
      test](#create-a-custom-manager-for-a-test)
    - [Mocking hibernate used to test methods using where queriers /
      detached criteria / criteria
      builder](#mocking-hibernate-used-to-test-methods-using-where-queriers-detached-criteria-criteria-builder)
    - [Mock return value for service method used in the service you
      are
      testing](#mock-return-value-for-service-method-used-in-the-service-you-are-testing)
    - [Mock a static method call from a
      domain](#mock-a-static-method-call-from-a-domain)
- [Test Snippets](#test-snippets)
    - [Test Rest](#test-rest)
        - [Grails3](#grails3)

# Config in test

## Modify config

``` groovy

 Holders.grailsApplication.config.outlook.clientId = "GUUNAR5" 
 
```

## Get at application

``` groovy

 grails.util.Holders.grailsApplication.domainClasses.find{it.shortName == 'User'}
 
```

## Use db in memory to run tests

``` groovy

     @shared Sql sql = Sql.newInstance(“jdbc:h2:mem:” , “org.h2.Driver”)
     
```

## Mocking service and then method call, setting dummy data for the return(put in test method)

``` groovy
   controller.openweathermapService = Mock(OpenweathermapService)

  controller.openweathermapService.currentWeatherByGeoID(_) >> currentWeather
```

## Mocking Service used in a service you are testing(put at beginning of the test class)

``` groovy

Closure doWithSpring() {{ ->
              assessmentOrderService AssessmentOrderService
   }}
   
AssessmentOrderService assessmentOrderService
```

## Mocking Method in service you are testing

``` groovy
 @Shared
    GroupCompareJoinUserGroupService groupCompareJoinUserGroupService

setupSpec(){
        mockDomain GroupCompareJoinUserGroup

}

 def "some test"(){
    service.groupCompareJoinUserGroupService = Mock(GroupCompareJoinUserGroupService)
        service.groupCompareJoinUserGroupService.fetchAssociatedAssessments(_ as GroupCompare, true) >> {groupCompare, removeRelationships -> groupCompareJoinUserGroupService.fetchAssociatedAssessments(groupCompare , true)}

    }
  
```

## Mocking method in domain

``` groovy

   [service/controller/domain].metaclass.’static’.[method] = {[arguments] -> [what to return]}
   
```

## Using Test Data from BuildTest plugin

### Snippet from spock test

> Pluggin for using test data builder
> [BuildTestData](http://plugins.grails.org/plugin/longwa/build-test-data)

> import grails.buildtestdata.mixin.Build

> use- implements BuildDomanTest\< \> instead of DomainUnitTest \< \>

``` groovy

@Build([Job, Tag, Type, Publisher])
class StatisticsServiceSpec extends Specification implements AutowiredTest, DataTest, BuildDataTest, ServiceUnitTest<StatisticsService>, GrailsWebUnitTest{

    def setupSpec(){
        mockDomain Job
        mockDomain Tag
        mockDomain Type
        mockDomain Publisher
    }
    def setup() {
    }

    def cleanup() {
    }

    void "get top publishers when we don't have nothing in our system"() {
         given: "when we don't have any job published"

         when: "we get top publishers"
         def publishers = service.getTopPublishers()
         then:"we will see 0 publishers"
         publishers.size() == 0
         }

    void "get top publishers when we have multiple jobs published by the same publisher"() {
        given: "when we have one 2 jobs published by the same publisher"
        def tag = Tag.build()
        def type = Type.build()
        def publisher = Publisher.build()
        Job.build(publisher: publisher, type: type, tags: [tag])
        Job.build(publisher: publisher, type: type, tags: [tag])

        when: "we get top publishers"
        def publishers = service.getTopPublishers()
        def pair = publishers.find { key, value -> key.name.equals(http://publisher.name ) }
        then:"we will see 2 publishers"
        publishers.size() == 1
        pair?.value == 2
    }

}
```

- config file test/resources/TestDataConfig

```java
import com.talentbank.core.ClientSetup

import java.util.concurrent.ThreadLocalRandom

//config file for test data plugin
testDataConfig{
        sampleData{
        unitAdditionalBuild=['com.talentbank.core.assessmentOrder.AssessmentOrder':[com.talentbank.core.ClientSetup]]

        'com.talentbank.core.ClientSetup'{
        //work around for unique constraints
        def i=55
        clientId={->ThreadLocalRandom.current().nextLong(100000)}
        companyCode={->"company${i}"}
        clientName={->"clientName${i++}"}
        }

        'com.talentbank.core.User'{
        def i=55
        username={->"email${i++}@mailinator.com"}
        email={->"email${i}@mailinator.com"}
        }

        'com.talentbank.core.assessmentOrder.AssessmentOrder'{
        clientSetup={->ClientSetup.build()}
        }
        }
        }

```

#### Different ways to build

``` groovy

 def intviewModel = TestData.build(InterviewModel)
 def y = InterviewModel.build(source: Source.TBSIX)
 def z = build(InterviewModel, source: Source.TBSIX)
 
```

# Configurations

## Checking validity of constraints

``` groovy
!newScheduledInterview2.validate(['scheduledBy', 'scheduledDate'])
!newScheduledInterview2.save(flush: true)
newScheduledInterview2.errors['scheduledDate']?.code == 'unique'
```

## check if method was called for another service

``` groovy
def called = false
service.notifierService = Mock(NotifierService)
service.notifierService.sendPostMarkEmail(_ as PostMarkEmail, _) >> { it -> called = true }
```

## check if method was called for same service

``` groovy
service.metaClass.sendReminderEmail = { assessmentOrderId, templateId, sender, newTemplateBody, jobId-> calls++ }
```

## create an exception

``` groovy
//create expando
def testDelete = new Expando()

// add exception to method call
def exception = {new Exception("TEST")}
testDelete.delete = {throw exception}

// add class as return for a method
service.metaClass.[method_to_throw_exception] = {testDelete}

//example in CalenderServiceSpec.groovy / “test delete exception”
//or
service.metaClass.[yourMethod] >> {throw exception}
```

## catch exception

``` groovy
def response = thrown(GraphServiceException)
```

## modify config during/for test

``` groovy
   Holders.grailsApplication.config.outlook.clientId = "GUUNAR5"
```

## create a custom manager for a test

``` groovy

 def managerMap=[:]
 RoleGroup.findAll().each {
 def myUser=User.build(clientSetupId: 1, email:
 "${it.name}@mailinator.com", username: "${it.name}@mailinator.com")
 UserRoleGroup.build(user: myUser, roleGroup: it)
 def tokenAuthentication = new TokenAuthentication(decodedJwt(myUser), myUser)
 tokenAuthentication.details = myUser
 authMap[(it.name)] = tokenAuthentication
 managerMap[(myUser.id)] = it.name ==~ /testManager.*/ ? [1,2,3] : []
 }
 service.userService = Mock(UserService)
 service.userService.fetchDirectReportIds(_) >> {it ->
 managerMap.get(it[0])
 }
```

## Mocking hibernate used to test methods using where queriers / detached criteria / criteria builder

``` groovy
 @Shared
 InterviewModelService interviewModelService

 @Shared
 HibernateDatastore hibernateDatastore

 @Shared
 PlatformTransactionManager transactionManager

 Map configuration = [
 'hibernate.hbm2ddl.auto' : 'create-drop',
 'dataSource.url' : 'jdbc:h2:mem:myDB',
 'hibernate.cache.region.factory_class': 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
 ]
 hibernateDatastore = new HibernateDatastore(configuration, CatalogDetail)
 transactionManager = hibernateDatastore.getTransactionManager()
 catalogDetailService = hibernateDatastore.getService(CatalogDetailService)


 //Set tests to rollback

 @Rollback
 void "test criteria builder for getting interview models should return all"() {
 //test
 }
```

## Mock return value for service method used in the service you are testing

``` groovy
service.springSecurityService = [authentication: [details: currentUser] ]
```

## Mock a static method call from a domain

``` groovy
ClientSetup.metaClass.static.fetchSecurityGroupLabelsByClientSetupId = {Long id, String en -> [secGroupNameLabel : 'secGroupNameLabel', secGroupCodeLabel : 'secGroupCodeLabel']}
```

# Test Snippets

## Test Rest

### Grails3

[Controller](https://gist.github.com/14paxton/0d64ab846b4691d8d6f1ccd5ccc63b58)

### Grails4

#### Ex. Integrations test for controller

```groovy
package musicandcars

import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Integration
@Stepwise
class CarFunctionalSpec extends Specification {

    @Shared
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client = HttpClient.create(baseUrl.toURL())
    }

    void "test that no cars exist"() {
        when:
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/automobiles"), Argument.of(List, Map))

        then:
        resp.status == HttpStatus.OK
        resp.body().size() == 0
        resp.contentType.get().extension == MediaType.EXTENSION_JSON
    }
}
```