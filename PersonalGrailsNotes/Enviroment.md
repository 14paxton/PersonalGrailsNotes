---
title:        Enviroment
permalink:    PersonalGrailsNotes/Enviroment
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

# How To Check If Environment is Test, Development, or Production in Grails

[Reference](http://grails.asia/how-to-check-if-environment-is-test-development-or-production-in-grails)

## Bootstrap

```groovy
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->

        if (Environment.current == Environment.DEVELOPMENT) {

            // insert Development environment specific code here 

        } else if (Environment.current == Environment.TEST) {

            // insert Test environment specific code here 

        } else if (Environment.current == Environment.PRODUCTION) {

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

        } else if (Environment.current == Environment.TEST) {

            // insert Test environment specific code here 

        } else if (Environment.current == Environment.PRODUCTION) {

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