-   [Testing](#testing){#toc-testing}
-   [run with spaces in mthod
    name](#run-with-spaces-in-mthod-name){#toc-run-with-spaces-in-mthod-name}
-   [Set system
    properties](#set-system-properties){#toc-set-system-properties}

# Testing

# run with spaces in mthod name

``` bash
./gradlew test --tests "com.talentbank.core.UserServiceAPISearchSpec.search for name Dick with clientSetupIds"
```

# Set system properties

``` bash
 ./gradlew -Dsample.message=cool run
```