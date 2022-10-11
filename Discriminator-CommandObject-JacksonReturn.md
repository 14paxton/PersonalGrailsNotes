## Usising Discriminator Formula-
## was used to modify existing domain to add a parent , and existing records would have a class
       discriminator formula: "case when (type = 'GROUP_COMPARE') then '${UserGroupType.GROUP_COMPARE.key}' when (type = 'RESULT_COMPARE') then             '${UserGroupType.RESULT_COMPARE.key}' when (type in ('RESULTGROUP', 'MYSAVEDGROUP')) then '${UserGroupType.RESULTGROUP.key}' else '${UserGroupType.GROUPDEFAULT.key}' end " ,
     type: 'string',
     value: UserGroupType.GROUPDEFAULT.key


## Demo Command Object with Validation-*
## command object allows you to validate with services

<a href="https://gist.github.com/14paxton/282d48ed20642c697315e15dffb7df2d"> GRAILSCommandObject </a>

#### Custom Validator for Nested Command objects in a list
> this forces validate on nested command object, then adds errors to parent errors, only the parent need to be confirmed
```groovy
    List<WSConfigCommand> wSConfig
...

    void validateWSConfig() {
        if (this.wSConfig.size() > 0) {
            this.wSConfig.each { WSConfigCommand cmd ->
                if (!cmd.validate()) {
                    cmd.errors.allErrors.each {
                        errors.reject(it.codes[0], it.arguments, it.defaultMessage)
                    }
                }
            }
        }
    }
```

#### Import and excluding other domain constrains
```groovy
        importFrom SOAPClientOrderConfig, exclude: ["binarySecTokenEncodingType", "binarySecTokenValueType", "countryCodeFormat"]
```


### JSON return bodies Spring/Jackson examples
### https://www.baeldung.com/jackson-json-view-annotation

###        https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-jackson
