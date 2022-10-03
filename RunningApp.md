
#  Grails with React app

 

 ./gradlew server:bootRun

 ./gradlew client:start

   

./gradlew bootRun -parallel

 

#  ASYNC

 def resendRegistrationEmailAndLockUserAccount(def userList) {

        def emailsSent = []

        def emailsNotSent = []

        def nThreads = Runtime.getRuntime().availableProcessors()

        def size = (userList.size() / nThreads).intValue()

        def promises = []


        withPool nThreads, {

            userList.collate(size).each { subList ->

                def promise = task {

                    subList.each {userInstance ->

                        User.withTransaction {

                            def auth0Response = sendRegistrationEmail(userInstance)


                            if (auth0Response.type == "success")

                            {

                                emailsSent << userInstance.email

                                userInstance.inAdminResetProcess = true

                                userInstance.save(flush: true)

                                userInstance.setActive(true)

                            }

                            else

                            {

                                emailsNotSent << userInstance.email

                            }

                        }

                    }

                }

                promises.add(promise)

            }

            waitAll(promises)

        }


        return ["emailsSent" : emailsSent, "emailsNotSent" : emailsNotSent]

    }

 

 

# JSON to javascript

var catalogsByType = null;
<g:applyCodec encodeAs="none">
    catalogsByType = ${resultCatalogs.catalogsByType as grails.converters.JSON};
</g:applyCodec>

 

<script>

    var data = ${raw(data)};

</script>

 

# Using messageSource

     [i18n Docs](https://docs.grails.org/4.0.1/guide/i18n.html)

 messageSource.getMessage('batch.user.registration.confirmation.message', [jobId as String].toArray() , LocaleContextHolder.locale)


# JSON Parser EX

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

//This is your JSON object that should be passed in to the method

print jsonObj //[markings:[rightForeMarkings:sock, otherMarkings:, leftForeMarkings:, leftHindMarkings:sock, rightHindMarkings:, headMarkings:Brindle]]

 

def jsonStr = jsonObj.toString()

//This is the string which should be persisted in db

assert jsonStr == '{"markings":{"rightForeMarkings":"sock","otherMarkings":"","leftForeMarkings":"","leftHindMarkings":"sock","rightHindMarkings":"","headMarkings":"Brindle"}}'

 

//Get back json obj from json str

def getBackJsobObj = grails.converters.JSON.parse(jsonStr)

assert getBackJsobObj.markings.leftHindMarkings == 'sock'

 

# adding plugins in multi project build

[My Example](https://github.com/14paxton/Grails4App/blob/task2-create-react-app/app-web/settings.gradle)

1. add to settings.gradle

`      include 'client', 'server'`
      `rootProject.name = 'app-web'`
      `include ':mod-domain', ":mod-mobile"``
      `project(':mod-domain').projectDir = new File(settingsDir, '../mod-domain')`
      `project(':mod-mobile').projectDir = new File(settingsDir, '../mod-mobile')`

2. add to build.gradle

    grails {
    plugins {
        compile project(":mod-domain")
        compile project(":mod-mobile")
    }
    }
    compile project(':mod-domain')

# Tidbit
## Render grails tags to return in controller

        render  g.select(from: languages, optionKey: "key" , optionValue: "value",  name: "languageChoice",
        class:"form-control", value: assessmentLanguage)

## save grails tag in variable and render on page 
    `${yourTag.encodeAsRaw()}`

 or `${raw(user.description)}`

 

 

# Liquibasehttps://grails-plugins.github.io/grails-database-migration/3.0.x/index.html

## clear liquibase checksums

   `grails dbm-clear-checksum`

## clear liquibase locks

 ` grails dbm-release-locks`

 

## ignore checksums in liquibase

 ` validCheckSum 'any'`

# GRAILS TYPE Converters

            Convert and check type in controller

            [TypeCheck](http://docs.grails.org/latest/guide/theWebLayer.html#typeConverters)

`param.short(...)`
`param.byte(...)`
`param.long(...)`
`param.double(...)`
`param.boolean(...)`

# Set system properties

## Now we can invoke the run or bootRun tasks with Gradle:

$ gradle -Dsample.message=cool run

## Or we can execute the run-app command with the grails command:

grails> run-app -Dsample.message=cool
 

# User Domain reference , when hibernate id is already being used

 class UserGroupShare {

    Long userGroupId
    Long userId
    String email
    String encryptedId
    Boolean revoked = Boolean.FALSE
    Date dateCreated
    Date lastUpdated
   
    static belongsTo = [userGroup : UserGroup]

    static constraints = {
        userGroupId unique: 'userId'
        email nullable: true
        encryptedId nullable: true
    }

    static mapping = {
        //can keep userGroupId column, and create usergroup parent reference without creating new db column
        //need to use foreign Key reference to save
        userGroup insertable: false
        userGroup updateable: false
    }

    

# Custom JVM args

set jvm args in build.gradle   bootRun{}

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

# set remote connection

           

Brandon Paxton > Grails Notes > Picture1.png
Use runapp then hit debug for remote connection

grailsw dev -Dgrails.AWS_REGION=us-west-2  -Dgrails.AWS_PROFILE=dev run-app --stacktrace -verbose
