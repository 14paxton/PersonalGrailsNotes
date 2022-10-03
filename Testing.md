 
# Testing

## Config in test
### Modify config 
          Holders.grailsApplication.config.outlook.clientId = "GUUNAR5" 
### Get at application
          grails.util.Holders.grailsApplication.domainClasses.find{
              it.shortName == 'User'}

 ###  Use db in memory to run tests

     @shared Sql sql = Sql.newInstance(“jdbc:h2:mem:” , “org.h2.Driver”)


### Mocking service and then method call, setting dummy data for the return(put in test method)

   controller.openweathermapService = Mock(OpenweathermapService)

  controller.openweathermapService.currentWeatherByGeoID(_) >> currentWeather

###  Mocking Service used in a service you are testing(put at beginning of the  test class)

§   Closure doWithSpring() {{ ->
              assessmentOrderService AssessmentOrderService
   }}

AssessmentOrderService assessmentOrderService

###  Mocking Method in service you are testing

   [service/controller/domain].metaclass.[method] = {[arguments] -> [what to return]}

###      Mocking method in domain

   [service/controller/domain].metaclass.’static’.[method] = {[arguments] -> [what to return]}

 

 

## Using Test Data

  Pluggin for using test data builder [BuildTestData](http://plugins.grails.org/plugin/longwa/build-test-data)

 import grails.buildtestdata.mixin.Build

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

 

Different ways to build

               def intviewModel = TestData.build(InterviewModel)
 
 

                use- implements BuildDomanTest< >  instead of DomainUnitTest < >

       def y = InterviewModel.build(source: Source.TBSIX)

       def z = build(InterviewModel, source: Source.TBSIX)
 
 </pre></td></tr></table></td></tr></tbody></table><h2>GRAILS/GORM</h2><table data-layout="default" ac:local-id="6692d35e-ea57-4f1e-980d-ce4d8bc2c17c" class="confluenceTable"><colgroup><col style="width: 115.0px;" /><col style="width: 645.0px;" /></colgroup><tbody><tr><th data-highlight-colour="#57d9a3" class="confluenceTh"><p><strong>Render grails tags to return</strong></p></th><th class="confluenceTh"><ol><li><p>render a tag or html, can be returned through an async/ajax call and added to div or html on page</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="adb9433a-bd4f-497e-a3cb-bc47947194c1" data-macro-parameters="language=groovy" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGU6bGFuZ3VhZ2U9Z3Jvb3Z5fQ&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>	render  g.select(from: languages, optionKey: "key" , optionValue: "value",  name: "languageChoice",
        class:"form-control", value: assessmentLanguage)
</pre></td></tr></table><p /></li></ol></li><li><p>save a tag in a variable and render it on page</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="81f77ebe-19e3-4b9d-ba59-002edc7ddda0" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>${yourTag.encodeAsRaw()}
 ${raw(user.description)}
</pre></td></tr></table></li></ol></li></ol></th></tr><tr><th data-highlight-colour="#57d9a3" class="confluenceTh"><p><strong>Testing(Mocking )</strong></p></th><td class="confluenceTd"><ol><li><p>Mock return value for service method used in the service you are testing</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="0d8267c1-04ba-4d0b-be60-e2234edb65c8" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>service.springSecurityService = [authentication: [details: currentUser] ]</pre></td></tr></table></li></ol></li><li><p>Mock a static method call from a domain</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="32aa0d3a-006e-4c19-a745-0072ea0d64a2" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>ClientSetup.metaClass.static.fetchSecurityGroupLabelsByClientSetupId = {Long id, String en -> [secGroupNameLabel : 'secGroupNameLabel', secGroupCodeLabel : 'secGroupCodeLabel']}</pre></td></tr></table><p /></li></ol></li></ol></td></tr><tr><th data-highlight-colour="#57d9a3" class="confluenceTh"><p><strong>Tesing</strong></p></th><td class="confluenceTd"><ol><li><p>Checking validity of constraints</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="223083e8-bbe8-49fe-a938-16fedba60695" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>!newScheduledInterview2.validate(['scheduledBy', 'scheduledDate'])
!newScheduledInterview2.save(flush: true)
newScheduledInterview2.errors['scheduledDate']?.code == 'unique'
</pre></td></tr></table><p /></li></ol></li><li><p>check if method was called for another service</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="e1922e4d-60c2-43b1-93a7-4b247b587ea7" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>def called = false
service.notifierService = Mock(NotifierService)
service.notifierService.sendPostMarkEmail(_ as PostMarkEmail, _) >> { it -> called = true }</pre></td></tr></table><p /></li></ol></li><li><p>check if method was called for same service</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="203a7c9e-f6e1-4a88-a974-8a8714e07fa9" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>service.metaClass.sendReminderEmail = { assessmentOrderId, templateId, sender, newTemplateBody, jobId-> calls++ }</pre></td></tr></table><p /></li></ol></li><li><p>create an exception </p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="c770512d-1702-44a3-8bec-2306a4c0e93e" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>//create expando
def testDelete = new Expando()

// add exception to method call
def exception = {new Exception("TEST")}

testDelete.delete = {throw exception}

// add class as return for a method
service.metaClass.[method_to_throw_exception] = {testDelete}</pre></td></tr></table><p /></li><li><p>- example in CalenderServiceSpec.groovy / “test delete exception”</p><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="7df49720-85db-4ff1-85fc-e419670f760c" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>//or
service.metaClass.[yourMethod] >> {throw exception}</pre></td></tr></table><p /></li></ol></li><li><p>catch exception</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="3d395048-d951-4a39-bbd1-cc316c60d341" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>def response = thrown(GraphServiceException)</pre></td></tr></table><p /></li></ol></li><li><p>modify config during/for test</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="a139584e-15e4-4694-ad73-6fed76f35731" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>Holders.grailsApplication.config.outlook.clientId = "GUUNAR5"</pre></td></tr></table><p /></li></ol></li><li><p>create a custom manager for a test</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="1e9e20ab-13c6-49ab-b4c4-645664cce8e1" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>def managerMap = [:]

RoleGroup.findAll().each {
    def myUser = User.build(clientSetupId: 1, email: "${it.name}@mailinator.com", username: "${it.name}@mailinator.com")
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
</pre></td></tr></table><p /></li></ol></li><li><p>Mocking hibernate used to test methods using where queriers / detached criteria / criteria builder</p><ol><li><p /><table class="wysiwyg-macro" data-macro-name="code" data-macro-id="c7095e9d-dd5f-4f4b-b6c6-8806ab118c66" data-macro-schema-version="1" style="background-image: url(https://talentplus.atlassian.net/wiki/plugins/servlet/confluence/placeholder/macro-heading?definition=e2NvZGV9&amp;locale=en_US&amp;version=2); background-repeat: no-repeat;" data-macro-body-type="PLAIN_TEXT"><tr><td class="wysiwyg-macro-body"><pre>@Shared
InterviewModelService interviewModelService

@Shared
HibernateDatastore hibernateDatastore

@Shared
PlatformTransactionManager transactionManager

Map configuration = [
        'hibernate.hbm2ddl.auto'              : 'create-drop',
        'dataSource.url'                      : 'jdbc:h2:mem:myDB',
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
 

 
