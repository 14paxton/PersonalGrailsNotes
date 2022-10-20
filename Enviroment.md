# How To Check If Environment is Test, Development, or Production in Grails

[Reference](http://grails.asia/how-to-check-if-environment-is-test-development-or-production-in-grails)

## Bootstrap

```groovy
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->

        if (Environment.current == Environment.DEVELOPMENT) {

            // insert Development environment specific code here 

        }
        else if (Environment.current == Environment.TEST) {

            // insert Test environment specific code here 

        }
        else if (Environment.current == Environment.PRODUCTION) {

            // insert Production environment specific code here 

        }

    }

}
```

## Controller

```groovy 
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
```

## View/GSP

```html

<g:if env="development">
    We are in Development Mode
</g:if>

<g:if env="production">
    We are in Production Mode
</g:if>

<g:if env="test">
    We are in Test Mode
</g:if> 
```

# Security Roles

## Properties

```properties
 application.groovy [pattern: '/adminDashboard/**', access: ['ROLE_ADMIN']]
 ```

## controller above methods

```groovy 
@Secured(['ROLE_USER', 'ROLE_ADMIN']) 

@Secured("hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')") 
 
@Secured("(hasRole('ROLE_ORDER_SHOW') or hasRole('ROLE_RESULT_SHOW') ) and hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')") 
 
@Secured("hasAnyRole('ROLE_ORDER_SHOW', 'ROLE_RESULT_SHOW') and hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')") 
```

## controller body

```groovy
SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')

SpringSecurityUtils.ifNotGranted()

SpringSecurityUtils.ifAnyGranted() 
```

## views/gsp

```html

<sec:ifLoggedIn>
    <sec:ifNotLoggedIn> 
```

## Check user security roles

```groovy
def user = springSecurityService?.authentication?.details

//Check security context 
def authentication = SecurityContextHolder.getContext().getAuthentication()

//Checking variables in security notation in controller 
@Secured(closure = {
    def status = ctx.templateService.getTemplate(request.getParameter('id')).status.name()
    (hasRole("ROLE_ADMIN_TP") && status != 'UNPUBLISH' || 'DRAFT')
}) 
```