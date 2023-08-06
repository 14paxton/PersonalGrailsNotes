---
title:        CLI_Grailsw
permalink:    PersonalGrailsNotes/CLI_Grailsw
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

- [Grails](#grails){#toc-grails}
    - [Set System Props](#set-system-props){#toc-set-system-props}
    - [Testing](#testing){#toc-testing}
        - [](#section){#toc-section}
- [Liquibase](#liquibase){#toc-liquibase}
    - [clear liquibase
      checksums](#clear-liquibase-checksums){#toc-clear-liquibase-checksums}
    - [clear liquibase
      locks](#clear-liquibase-locks){#toc-clear-liquibase-locks}
    - [ignore checksums in
      liquibase](#ignore-checksums-in-liquibase){#toc-ignore-checksums-in-liquibase}

# Grails Command Line

<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 50%;">
<col style="width: 50%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top"><strong>Grails Command</strong></th>
<th class="tableblock halign-left valign-top"><strong>Gradle Task</strong></th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">clean</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">clean</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">compile</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">classes</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">package</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">assemble</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">run-app</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">bootRun</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">test-app</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">check</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">test-app --unit</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">test</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">test-app --integration</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">integrationTest</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock">war</p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock">assemble</p></td>
</tr>
</tbody>
</table>

## Set System Props

``` bash
    ./grailsw run-app -Dsample.message=cool
```

## Testing

###   

``` bash
./grailsw test-app 'com.talentbank.core.UserServiceAPISearchSpec.manager_query*' -unit
```

# Liquibase

> [Liquibase grails
> plugin](//grails-plugins.github.io/grails-database-migration/3.0.x/index.html)

## clear liquibase checksums

``` bash
./grailsw dbm-clear-checksum
```

## clear liquibase locks

``` bash
 grails dbm-release-locks
```

## ignore checksums in liquibase

> add to xml or groovy
>
> ``` groovy
> validCheckSum 'any'
> ```