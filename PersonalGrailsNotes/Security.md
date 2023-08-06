---
title:        Security
permalink:    PersonalGrailsNotes/Security
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

# Security Roles

## Properties

```properties
 application.groovy=[pattern: '/adminDashboard/**', access: ['ROLE_ADMIN']]
 ```

## controller above methods

```groovy 
@Secured(['ROLE_USER', 'ROLE_ADMIN']) 
```

```groovy 
@Secured("hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')") 
 ```

 ```groovy 
@Secured("(hasRole('ROLE_ORDER_SHOW') or hasRole('ROLE_RESULT_SHOW') ) and hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')") 
 ```

 ```groovy 
@Secured("hasAnyRole('ROLE_ORDER_SHOW', 'ROLE_RESULT_SHOW') and hasRole('ROLE_PERMISSION_ACCESS_ASSESSMENTS')")
```

```groovy
    @Secured(value = ['permitAll'], httpMethod = 'POST')
```

```groovy
    @Secured(value = ['IS_AUTHENTICATED_ANONYMOUSLY'], httpMethod = 'POST')
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