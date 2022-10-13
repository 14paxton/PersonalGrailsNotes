-   [Server Side](#server-side)
    -   [GRAILS TYPE
        Converters](#grails-type-converters)
    -   [JSON](#json)
        -   [JSON Parser
            Example](#json-parser-example)
-   [Frontend
    manipulation](#frontend-manipulation)
    -   [Using
        messageSource](#using-messagesource)
    -   [Render grails tags to return in
        controller](#render-grails-tags-to-return-in-controller)
    -   [save grails tag in variable and render on
        page](#save-grails-tag-in-variable-and-render-on-page)
    -   [JSON](#json-1)
        -   [Javascript
            manipulation](#javascript-manipulation)

# Server Side

## GRAILS TYPE Converters

> Convert and check type in controller
> [TypeCheck](http://docs.grails.org/latest/guide/theWebLayer.html#typeConverters)

``` groovy
param.short(...)
param.byte(...)
param.long(...)
param.double(...)
param.boolean(...)
```

## JSON

### JSON Parser Example

``` groovy

def json = '''{

                  "markings": {

                      "headMarkings": "Brindle",

                      "leftForeMarkings": "",

                      "rightForeMarkings": "sock",

                      "leftHindMarkings": "sock",

                      "rightHindMarkings": "",

                      "otherMarkings": ""

                   }

                }'''

 

def jsonObj = grails.converters.JSON.parse(json)
```

> This is your JSON object that should be passed in to the method

``` groovy
print jsonObj 
// optput [markings:[rightForeMarkings:sock, otherMarkings:, leftForeMarkings:, leftHindMarkings:sock, rightHindMarkings:, headMarkings:Brindle]]

def jsonStr = jsonObj.toString()

//This is the string which should be persisted in db
assert jsonStr == '{"markings":{"rightForeMarkings":"sock","otherMarkings":"","leftForeMarkings":"","leftHindMarkings":"sock","rightHindMarkings":"","headMarkings":"Brindle"}}'

//Get back json obj from json str
def getBackJsobObj = grails.converters.JSON.parse(jsonStr)

assert getBackJsobObj.markings.leftHindMarkings == 'sock'
```

# Frontend manipulation

## Using messageSource

[i18n Docs](https://docs.grails.org/4.0.1/guide/i18n.html)

``` groovy
 messageSource.getMessage('batch.user.registration.confirmation.message', [jobId as String].toArray() , LocaleContextHolder.locale)
```

## Render grails tags to return in controller

``` groovy
        render  g.select(from: languages, optionKey: "key" , optionValue: "value",  name: "languageChoice",
        class:"form-control", value: assessmentLanguage)
```

## save grails tag in variable and render on page

``` groovy
`${yourTag.encodeAsRaw()}`
```

> or

``` groovy
   `${raw(user.description)}`
```

## JSON

### Javascript manipulation

``` javascript
var catalogsByType = null;
<g:applyCodec encodeAs="none">
    catalogsByType = ${resultCatalogs.catalogsByType as grails.converters.JSON};
</g:applyCodec>

 

<script>

    var data = ${raw(data)};

</script>
```