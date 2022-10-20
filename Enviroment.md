 

How To Check If Environment is Test, Development, or Production in Grails 

Mar 05, 2014 Snippet 1 comments 

Sometimes it is very helpful to execute some code depending on which environment you are in. For example, you may want to insert test data inside Bootstrap when you run the application in development mode, but not in production mode.  
This is simple in Grails on different sections of your application.  
 

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

application.groovy  [pattern: '/adminDashboard/**', access: ['ROLE_ADMIN']], 

in controller  @Secured(['ROLE_USER', 'ROLE_ADMIN']) 

@Secured("hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')") 

 
@Secured("(hasRole('ROLE_ORDER_SHOW') or hasRole('ROLE_RESULT_SHOW') ) and hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')") 

 
@Secured("hasAnyRole('ROLE_ORDER_SHOW', 'ROLE_RESULT_SHOW') and hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')") 

 

in controller body SpringSecurityUtils.ifAllGranted('ROLE_ADMIN') 

also ifNotGranted(), ifAnyGranted() 

in views/gsp   <sec:ifLoggedIn>  <sec:ifNotLoggedIn> 

Check user security roles 

def user = springSecurityService?.authentication?.details 

Check security context 

def authentication = SecurityContextHolder.getContext().getAuthentication() 

Checking variables in security notation in controller 

@Secured(closure = { 
    def status = ctx.templateService.getTemplate(request.getParameter('id')).status.name() 
    (hasRole("ROLE_ADMIN_TP") && status != 'UNPUBLISH' || 'DRAFT') 
}) 

 

 

 
