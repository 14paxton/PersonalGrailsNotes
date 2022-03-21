

## Adding DB connection

In application.yml you can add to  dataSource above environments to establish connection

dataSource:
    pooled: true
    jmxExport: true
    driverClassName: com.mysql.jdbc.Driver
    username: root
    password: 'computer'

Or/and update update an  environment

environments:

    development:

        dataSource:

            dbCreate: update

            url: jdbc:mysql://localhost:3306/mytest
 
 
production:

    dataSource:

        pooled: true

        dbCreate: update

        url:  jdbc:mysql://localhost:3306/jobboard

        driverClassName: com.mysql.jdbc.Driver

        dialect: org.hibernate.dialect.MySQL5InnoDBDialect

        username: root

        password: 'computer'

        properties:

            jmxEnabled: true

            initialSize: 5

            maxActive: 50

            minIdle: 5

            maxIdle: 25

            maxWait: 10000

            maxAge: 600000

            timeBetweenEvictionRunsMillis: 5000

            minEvictableIdleTimeMillis: 60000

            validationQuery: SELECT 1

            validationQueryTimeout: 3

            validationInterval: 15000

            testOnBorrow: true

            testWhileIdle: true

            testOnReturn: false

            jdbcInterceptors: ConnectionState

            defaultTransactionIsolation: java.sql.Connection.TRANSACTION_READ_COMMITTED
 
a.       For dbCreate there are 4 values

create - On startup of your application, this will drop and recreate your schema. This will make sure that you will always have a clean table structure, and all your data is reset on every startup. This is ideal when you are in the early stages of the project and heavily modifying your data model. You will need to spend more time on your Bootstrap though, to create all background data that you need.

create-drop - This will behave exactly like create when starting up your application, with addition that it will drop all your tables when the application is shut down properly or gracefully. I have no idea why you would need to do this. I would prefer the create method, because you still have the chance to inspect your database after stopping your application.

update - This will not your schema or any of your tables, but will instead try to synch the database with your current data model. This is done by adding the missing tables or columns to your database. In my testing, this will not drop tables or columns from the database when you removed their corresponding items in the data model. It does not guarantee a clean table representation of your data model. This however is ideal when you are in the middle of development, where it is not practical to put most of your test data in the Bootstrap.

validate - this will not alter your database, but will just compare your data model with the database on start up. And create warnings if necessary. This is ideal when deploying to production environment.

Add the mysql deps as 

runtime in the dependencies of your build.gradle. E.g.

a.       runtime 'mysql:mysql-connector-java:5.1.36'

Add Spring Security

For installing the Spring Security plugin we have to add a new dependency in build.gradle 1 compile 'org.grails.plugins:spring-security-core:3.2.3'

grails s2-quickstart app.admin.security User Role

plugin repo, create and modify views/login/auth.gsp for custom login

a.       https://github.com/grails-plugins/grails-spring-security-core/blob/master/plugin/grails-app/views/login/auth.gsp

 

plugin for spring security ui

compile 'org.grails.plugins:spring-security-ui:4.0.0.M1'

to use go to securityInfo/config

 

 

 

 

 

 

## How To Check If Environment is Test, Development, or Production in Grails

Mar 05, 2014 Snippet 1 comments

Sometimes it is very helpful to execute some code depending on which environment you are in. For example, you may want to insert test data inside Bootstrap when you run the application in development mode, but not in production mode. 
This is simple in Grails on different sections of your application. 

Build.gradle


if (!project.hasProperty('grailsEnv') || project.grailsEnv.equals('prod')) {
//
}else{
//
}

Bootstrap

As discussed above, it is common practice to insert different set of data in Bootstrap depending on the environment you are currently running. This is the sample code. 

import grails.util.Environment

class BootStrap {

    def init = { servletContext ->

        if (Environment.current == Environment.DEVELOPMENT) {

            // insert Development environment specific code here

        } else

        if (Environment.current == Environment.TEST) {

            // insert Test environment specific code here

        } else

        if (Environment.current == Environment.PRODUCTION) {

            // insert Production environment specific code here

        }

    }

}

Controller

The same code can be used in Controllers 

package asia.grails.myexample

import grails.util.Environment

class SomeController {

    def someAction() {

        if (Environment.current == Environment.DEVELOPMENT) {

            // insert Development environment specific code here

        } else

        if (Environment.current == Environment.TEST) {

            // insert Test environment specific code here

        } else

        if (Environment.current == Environment.PRODUCTION) {

            // insert Production environment specific code here

        }

        render "Environment is ${Environment.current}"

    }

}

View/GSP

In view, we can use the if tag: 

<g:if env="development">

    We are in Development Mode

</g:if>

<g:if env="production">

    We are in Production Mode

</g:if>

<g:if env="test">

    We are in Test Mode                                                                  

</g:if>

http://grails.asia/how-to-check-if-environment-is-test-development-or-production-in-grails

 

 

 

 

Security Role

·         application.groovy  [pattern: '/adminDashboard/**', access: ['ROLE_ADMIN']],

·         in controller  @Secured(['ROLE_USER', 'ROLE_ADMIN'])

·         @Secured("hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')")

·        
@Secured("(hasRole('ROLE_ORDER_SHOW') or hasRole('ROLE_RESULT_SHOW') ) and hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')")

·        
@Secured("hasAnyRole('ROLE_ORDER_SHOW', 'ROLE_RESULT_SHOW') and hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')")

o    

·         in controller body SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')

o   also ifNotGranted(), ifAnyGranted()

·         in views/gsp   <sec:ifLoggedIn>  <sec:ifNotLoggedIn>

·         Check user security roles

o   def user = springSecurityService?.authentication?.details

·         Check security context

o   def authentication = SecurityContextHolder.getContext().getAuthentication()

·         Checking variables in security notation in controller

o   @Secured(closure = {
    def status = ctx.templateService.getTemplate(request.getParameter('id')).status.name()
    (hasRole("ROLE_ADMIN_TP") && status != 'UNPUBLISH' || 'DRAFT')
})

 

 

 

Render GSP

<g:javascript type="text/javascript">
$(document).ready(function(){
$("button.btn-primary").click(function(){
var city = document.getElementById('cityList').value;
$.ajax(
    {url: "${g.createLink( controller:'activeUser', action:'getCurrentWeather')}" ,
type: 'POST',
    data: {cityChoice: city},
success: function(result){
    console.log("here");
$("#curWeather").html(result);
}});
});
});
</g:javascript>

Ajax methods https://www.w3schools.com/jquery/ajax_ajax.asp

Render template in a div

render  template: 'currentWeather', model: [currentWeather: currentWeather]

CACHING/Memory

 

http://gorm.grails.org/6.0.x/hibernate/manual/

Caching is often a good strategy, but remember that caches have expiry parameters so if left idle your app may reload from the DB again. Depending on your cache provider you'll have to tune this, eg For ehcache edit your ehcache.xml in the grails config folder and set (cache name is the same as your Domain class including package name):

<cache name="Country" maxElementsInMemory="1000" timeToIdleSeconds="0" timeToLiveSeconds="0"/>

You should also move the query into a service, the service is by default singleton scoped and the service method also cachable.

An alternative is to store them in application scope such as in your Bootstrap.groovy run the query and assign the results:

servletContext.countries = Country.list()

Retrieve them in the same way, eg

in a controller:

List<Country> countries = servletContext.countries

or gsp:

<g:each var="country" in="${application.countries}">

   ${country}

</g:each>

If you have eager fetching on then you should see no DB hits.

 

You can cache queries such as dynamic finders and criteria. To do so using a dynamic finder you can pass the cacheargument:

def person = Person.findByFirstName("Fred", [cache: true])

You can also cache criteria queries:

def people = Person.withCriteria {

    like('firstName', 'Fr%')

    cache true

}

 

 

SET 2nd LEVEL CACHING

Install plugin in build.gradle

//caching
compile "org.grails.plugins:cache"
compile "org.hibernate:hibernate-ehcache:5.4.4.Final"

 

Update cache in application.yml

hibernate:
    cache:
        queries: false
        use_second_level_cache: true
        use_query_cache: false
        region.factory_class: org.hibernate.cache.ehcache.EhCacheRegionFactory

 

 

 

 

 
