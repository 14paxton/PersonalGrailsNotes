## Usising Discriminator Formula-
## was used to modify existing domain to add a parent , and existing records would have a class
       discriminator formula: "case when (type = 'GROUP_COMPARE') then '${UserGroupType.GROUP_COMPARE.key}' when (type = 'RESULT_COMPARE') then             '${UserGroupType.RESULT_COMPARE.key}' when (type in ('RESULTGROUP', 'MYSAVEDGROUP')) then '${UserGroupType.RESULTGROUP.key}' else '${UserGroupType.GROUPDEFAULT.key}' end " ,
     type: 'string',
     value: UserGroupType.GROUPDEFAULT.key


## Demo Command Object with Validation-*
## command object allows you to validate with services

<a href="https://gist.github.com/14paxton/282d48ed20642c697315e15dffb7df2d"> GRAILSCommandObject </a>


### JSON return bodies Spring/Jackson examples
### https://www.baeldung.com/jackson-json-view-annotation

###        https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-jackson
