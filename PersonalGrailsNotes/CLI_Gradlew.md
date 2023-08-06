---
title:        CLI_Gradlew
permalink:    PersonalGrailsNotes/CLI_Gradlew
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

- [Run App](#run-app)
    - [Run with jvm
      debug](#run-with-jvm-debug)
- [Testing](#testing)
    - [run with spaces in method
      name](#run-with-spaces-in-method-name)
    - [run jvm debug](#run-jvm-debug)
    - [Set system
      properties](#set-system-properties)

# Run App

## Run with jvm debug

``` bash
./gradlew bootRun --debug-jvm --full-stacktrace --daemon 
```

# Testing

## run with spaces in method name

``` bash
./gradlew test --tests "com.talentbank.core.UserServiceAPISearchSpec.search for name Dick with clientSetupIds"
```

## run jvm debug

``` bash
./gradlew test --debug-jvm --tests "com.talentbank.tbex.SelfServiceIntegration.SelfServiceIntegrationControllerSpec.test_rest_no_request_object"  --full-stacktrace
```

## Set system properties

``` bash
 ./gradlew -Dsample.message=cool run
```