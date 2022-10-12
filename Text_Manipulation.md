# Using messageSource

     [i18n Docs](https://docs.grails.org/4.0.1/guide/i18n.html)
```groovy
 messageSource.getMessage('batch.user.registration.confirmation.message', [jobId as String].toArray() , LocaleContextHolder.locale)
```


# JSON
#### Javascript manipulation

```javascript
var catalogsByType = null;
<g:applyCodec encodeAs="none">
    catalogsByType = ${resultCatalogs.catalogsByType as grails.converters.JSON};
</g:applyCodec>

 

<script>

    var data = ${raw(data)};

</script>
```

### JSON Parser Example

```groovy

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
```groovy
print jsonObj 
// optput [markings:[rightForeMarkings:sock, otherMarkings:, leftForeMarkings:, leftHindMarkings:sock, rightHindMarkings:, headMarkings:Brindle]]

def jsonStr = jsonObj.toString()

//This is the string which should be persisted in db
assert jsonStr == '{"markings":{"rightForeMarkings":"sock","otherMarkings":"","leftForeMarkings":"","leftHindMarkings":"sock","rightHindMarkings":"","headMarkings":"Brindle"}}'

//Get back json obj from json str
def getBackJsobObj = grails.converters.JSON.parse(jsonStr)

assert getBackJsobObj.markings.leftHindMarkings == 'sock'
```
 
