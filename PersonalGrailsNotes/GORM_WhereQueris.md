---
title:        GORM_WhereQueris
permalink:    PersonalGrailsNotes/GORM_WhereQueris
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

# Altering fetch

```groovy
  // Using a dynamic finder
Author.findAllByNameLike("John%", [sort: 'name', order: 'asc', fetch: [location: 'join']])


import org.hibernate.FetchMode as FM

def results = Airport.withCriteria {
    eq "region", "EMEA"
    fetchMode "flights", FM.SELECT
}
```

# More Advanced Subqueries in GORM

```groovy

def results = Person.where {
    firstName in where { age < 18 }.firstName.
}.list()
```

```groovy

def results = Person.withCriteria {
    notIn "firstName", Person.where { age < 18 }.firstName
}
```

## Subqueries can be used with projections:

```groovy
   def results = Person.where {
    age > where { age > 18 }.avg('age')
}

```

## Correlated queries that span two domain classes can be used:

```groovy

def employees = Employee.where {
    region.continent in ['APAC', "EMEA"]
}.id()

def results = Sale.where {
    employee in employees && total > 100000
}.employee.list()

```

## And support for aliases (cross query references) using simple variable declarations has been added to where queries:

```groovy
   def query = Employee.where {
    def em1 = Employee

    exists Sale.where {
        def s1 = Sale
        def em2 = employee
        return em2.id == em1.id
    }.id()

}

def results = query.list()

```

# ARGS Map , list of args that can be passed i.e. findall([sort: ), count(), ect

```groovy
public static final String ARGUMENT_FETCH_SIZE = "fetchSize"
public static final String ARGUMENT_TIMEOUT = "timeout"
public static final String ARGUMENT_READ_ONLY = "readOnly"
public static final String ARGUMENT_FLUSH_MODE = "flushMode"
public static final String ARGUMENT_MAX = "max"
public static final String ARGUMENT_OFFSET = "offset"
public static final String ARGUMENT_ORDER = "order"
public static final String ARGUMENT_SORT = "sort"
public static final String ORDER_DESC = "desc"
public static final String ORDER_ASC = "asc"
public static final String ARGUMENT_FETCH = "fetch"
public static final String ARGUMENT_IGNORE_CASE = "ignoreCase"
public static final String ARGUMENT_CACHE = "cache"
public static final String ARGUMENT_LOCK = "lock"
```

# CriteriaBuilder

## Creating Criteria

```groovy
    static dropdownList(def getActive = false, def excludeClient = null) {
    def clientSetupList = createCriteria().list() {
        resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
        projections {
            property('id', 'id')
            property('clientName', 'clientName')
            property('clientId', 'clientId')
            property('isActive', 'isActive')
        }
        cache true
        if (getActive) eq('isActive', true)
        if (excludeClient) ne('clientName', excludeClient)
        order('clientName', 'asc')
    }

    return clientSetupList
}
```

## Where using a not in and deleteAll()

```groovy
    UserGroupAssessmentOrder.where {
    userGroupId == ug.id
    not { 'in'('assessmentOrderId', command.assessmentOrderIds.collect { (Long) it }) }
}.deleteAll()

def results = Person.withCriteria {

    notIn "firstName", Person.where { age < 18 }.firstName
}
```

## Chain where query

```groovy
    def templateIdQuery = InterviewModel.where {
    source == source
    sourceId == sourceId
}.templateId

def templateContentQuery = TemplateContent.where {
    templateId == templateIdQuery
    themeStringId == coreTextStringId
    type == StringType.THSMY
}.contentStringId

TextString.where {
    id == templateContentQuery.find()
}.en.find() }

 ```

## Creating criteria with a join and row count

```groovy
     UserGroup.createCriteria().list() {
    resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
    createAlias('groupAssessmentOrders', 'groupAssessmentOrders', JoinType.LEFT_OUTER_JOIN)
    projections {
        groupProperty('id', 'id')
        property('name', 'name')
        property('interviewModelId', 'interviewModelId')
        property('visibility', 'visibility')
        property('lastUpdated', 'lastUpdated')
        rowCount('groupAssessmentOrders')
    }

    eq('type', UserGroupType.RESULTGROUP)
    eq('userId', principalUser?.id)

}
```

## WHERE QUERY WITH DETACHED CRITERIA AND PROP PROJECTION JOINS

```groovy
              //subquery find users with managers whose name is like the search string
//query use list from subquery to find all users and create map to match filtered users search
def queryByManagerName(String searchString, List<Long> filteredUsers, sort, order, _clientSetupId,
                       statusFilters, unassignedManagerFilter, unassignedAssessmentFilter) {

    def (firstNameSearch, lastNameSearch, rest) = searchString?.tokenize()
    def stringToRegex = { string -> string ? "^${string}.*" : null }


    def managerQuery = UserRelationship.whereAny {
        manager {
            firstName ==~ ~/${stringToRegex(searchString)}/ ||
                    lastName ==~ ~/${stringToRegex(searchString)}/
        }
        manager {
            firstName ==~ ~/${stringToRegex(firstNameSearch)}/ &&
                    lastName ==~ ~/${stringToRegex(lastNameSearch)}/
        }
        if (filteredUsers) {
            not { 'in'('user.id', filteredUsers.collect { (Long) it }) }
        }

    }.join('user').distinct('user.id')


    DetachedCriteria<User> detachedUserQuery = User.where {
        id in managerQuery
        projections {
            distinct('id')
            property('username')
            property('clientSetupId')
            property('email')
            property('firstName')
            property('lastName')
            property('userMetadata')
            property('lastLogin')
            property('pictureUrl')
        }
    }


    if (_clientSetupId) {
        detachedUserQuery.eq('clientSetupId', _clientSetupId)
    }


    if (unassignedManagerFilter || unassignedAssessmentFilter) {
        def userIdsWithManager = getClientUserIdsWithManagers(_clientSetupId)
        detachedUserQuery.or {
            if (unassignedManagerFilter) {
                if (userIdsWithManager) {
                    not {
                        'in'('id', userIdsWithManager)
                    }
                }
            }

            if (unassignedAssessmentFilter) {
                def userIdsWithAssessment = getClientUserIdsLinkedToAssessments(_clientSetupId)
                if (userIdsWithAssessment) {
                    not {
                        'in'('id', userIdsWithAssessment)
                    }
                }
            }
        }
    }


    detachedUserQuery.sort(sort, order)
            .collect {
                [id                      : it[0], username: it[1], clientSetupId: it[2], email: it[3], firstName:
                        it[4], lastName  : it[5], userMetadata:
                         it[6], lastLogin: it[7], pictureUrl: it[8]]
            }

}


def (firstNameSearch, lastNameSearch, rest) = searchString?.tokenize()
def stringToRegex = { string -> string ? "^${string}.*" : null }

```

### EXAMPLE2

```groovy

DetachedCriteria<UserRelationship> managerQuery = UserRelationship.where {
    manager {
        firstName ==~ ~/${stringToRegex(searchString)}/ ||
                lastName ==~ ~/${stringToRegex(searchString)}/
    } ||
            manager {
                firstName ==~ ~/${stringToRegex(firstNameSearch)}/
                lastName ==~ ~/${stringToRegex(lastNameSearch)}/
            }
    if (filteredUsers) {
        not { 'in'('user.id', filteredUsers.collect { (Long) it }) }
    }
}.join('user').distinct('user.id')


DetachedCriteria<User> detachedUserQuery = User.where {
    id in managerQuery
}.id


if (_clientSetupId) {
    detachedUserQuery.eq('clientSetupId', _clientSetupId)
}


if (unassignedManagerFilter || unassignedAssessmentFilter) {
    def userIdsWithManager = getClientUserIdsWithManagers(_clientSetupId)
    detachedUserQuery.or {
        if (unassignedManagerFilter) {
            if (userIdsWithManager) {
                not {
                    'in'('id', userIdsWithManager)
                }
            }
        }
        if (unassignedAssessmentFilter) {
            def userIdsWithAssessment = getClientUserIdsLinkedToAssessments(_clientSetupId)
            if (userIdsWithAssessment) {
                not {
                    'in'('id', userIdsWithAssessment)
                }
            }
        }
    }
}


User.createCriteria().list(max: params?.max, offset: params?.offset) {
    resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
    projections {
        distinct('id', 'id')
        property('username', 'username')
        property('clientSetupId', 'clientSetupId')
        property('email', 'email')
        property('firstName', 'firstName')
        property('lastName', 'lastName')
        property('userMetadata', 'userMetadata')
        property('lastLogin', 'lastLogin')
        property('pictureUrl', 'pictureUrl')
    }
    'in'('id', detachedUserQuery)

    order(params?.sort, params?.order)
}

```

## Criteria Builder with bean transformation

[TransformResultToCustomBean](https://gist.github.com/14paxton/f384df3ac36befc64c894eeb28439387)