---
title:        RunningApp
permalink:    PersonalGrailsNotes/RunningApp
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

#### Run server client concurrently

```bash
 ./gradlew server:bootRun

 ./gradlew client:start 

./gradlew bootRun -parallel
```

#### adding plugins in multi project build

[My Example](https://github.com/14paxton/Grails4App/blob/task2-create-react-app/app-web/settings.gradle)

1. add to settings.gradle

```
      include 'client', 'server'`
      `rootProject.name = 'app-web'`
      `include ':mod-domain', ":mod-mobile"``
      `project(':mod-domain').projectDir = new File(settingsDir, '../mod-domain')`
      `project(':mod-mobile').projectDir = new File(settingsDir, '../mod-mobile')`
```

2. add to build.gradle

```groovy
    grails {
    plugins {
        compile project(":mod-domain")
        compile project(":mod-mobile")
    }
}
compile project(':mod-domain')
```

#### Custom JVM args

> set jvm args in build.gradle bootRun{}

```groovy
jvmArgs = ["-server",

           "-XX:ReservedCodeCacheSize=2g",

           "-XX:NewRatio=3",

           "-XX:ActiveProcessorCount=12",

           "-Xss16m",

           "-XX:+UseConcMarkSweepGC",

           "-XX:+CMSParallelRemarkEnabled",

           "-XX:ConcGCThreads=4",

           "-XX:+AlwaysPreTouch",

           "-XX:+TieredCompilation",

           "-XX:+UseCompressedOops", "-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005", "-Xmx8g",

]
```

#### Run with env variabls

grailsw dev -Dgrails.AWS_REGION=us-west-2 -Dgrails.AWS_PROFILE=dev run-app --stacktrace -verbose