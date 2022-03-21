# HQL query examples-
## SELECT DISTINCT mag FROM Magazine mag

    JOIN mag.articles art

    JOIN art.author auth

    WHERE auth.lastName = 'Grisham'

### may equivalently be expressed as follows, using the IN operator:

## SELECT DISTINCT mag FROM Magazine mag,

    IN(mag.articles) art

    WHERE art.author.lastName = 'Grisham'

#Using HQL(hibernate query language) in findall
##USING HQL for execute query

    def query = """
    select new Map(ug.id as id, ug.name as name, ug.interviewModelId as interviewModelId,
    ug.visibility as visibility, ug.lastUpdated as lastUpdated, COUNT(ugao.id) as assessmentCount )
    from UserGroup ug 
    LEFT JOIN UserGroupAssessmentOrder ugao ON ug.id= ugao.userGroupId
    where ug.userId = :userId and ug.type = :type 
    GROUP BY ug.id 
    ORDER BY ug.name"""

    def groups = UserGroup.executeQuery(query , [userId : principalUser?.id, type: UserGroupType.RESULTGROUP])

##  -HQL created using session-
### -Full Example - [Full dynamic HQL, with QueryImpl object ](https://gist.github.com/14paxton/0ed8e82644cd661dc8c9fc0d4b8c2009) 

               User.withSession{ uSession ->
               def q =    uSession.createQuery($/SELECT DISTINCT new com.talentbank.core.UserMap(user.id, 
               user.username, user.clientSetupId, 
                              user.email, user.firstName, user.lastName, user.userMetadata, user.lastLogin, 
              user.pictureUrl,manager.id, manager.email , 
                              manager.firstName ,manager.lastName ,manager.userMetadata, manager.lastLogin 
                             ,manager.pictureUrl 
                              ,manager.externalEmployeeCode)
                          FROM $User user
                          LEFT JOIN FETCH $UserRelationship ur on ur.user.id = user.id or ur.manager.id = 
                            user.idleft 
                          JOIN ur.manager manager
                          WHERE user.firstName like CONCAT('%', $searchString, '%')
                          or user.lastName like CONCAT('%', $searchString, '%')
                          or user.email like CONCAT('%', $searchString, '%')
                          or  manager.firstName like CONCAT('%', $searchString, '%')
                          or manager.lastName like CONCAT('%', $searchString, '%')           
                         /$)

                      q.maxResults = 8
                      q.firstResult = 2
                      q.list()
                     }

# -using Groovy SQL-

       List fetchUsersByNameOrManagerName(String searchString, params) {
        if(!params) return  null
       def (  firstNameSearch,  lastNameSearch,  rest) = searchString?.tokenize()
      //        DataSource dataSource = Holders.grailsApplication.mainContext.getBean('dataSource')
     //        Sql groovySql = new Sql(dataSource)

       String query = """SELECT DISTINCT user.id, user.username, user.client_setup_id, user.email, user.first_name, user.last_name, 
       user.user_metadata, 
       user.last_login, user.picture_url,
                                   manager.first_name AS mgr_first_name, manager.last_name AS mgr_last_name
                          FROM user
                                LEFT JOIN user_relationship as userRelationship on userRelationship.user_id = user.id
                                LEFT JOIN user manager on userRelationship.manager_id = manager.id
                              WHERE ((user.first_name LIKE '%${searchString}%' || user.last_name LIKE '%${searchString}%' || user.email LIKE 
                          '%${searchString}%') ||
                                      (user.first_name LIKE '%${firstNameSearch}%' && user.last_name LIKE '%${lastNameSearch}%')) 
                         UNION
                         SELECT DISTINCT user.id, user.username, user.client_setup_id, user.email, user.first_name, user.last_name, 
                         user.user_metadata, 
                         user.last_login, user.picture_url,
                               manager.first_name AS mgr_first_name, manager.last_name AS mgr_last_name
                         FROM user
                               LEFT JOIN user_relationship as userRelationship on userRelationship.user_id = user.id
                               LEFT JOIN user manager on userRelationship.manager_id = manager.id
                               WHERE ((manager.first_name LIKE '%${searchString}%' || manager.last_name LIKE '%${searchString}%') ||
                              (manager.first_name LIKE '%${firstNameSearch}%' && manager.last_name LIKE '%${lastNameSearch}%')) 
                            """

                         groovySql.rows(query, 0, 15)
                         }


## -pagination server side with PagedListHolder Object- 

        def queryResults = userDataService.searchForUsersWhereNameOrEmailLike(searchString)
        def pages = new PagedListHolder(queryResults)
        pages.setPage(params.off) //set current page number
        pages.setPageSize(params.max) // set the size of page

# CriteriaBuilder
## Creating Criteria

    static dropdownList(def getActive = false, def excludeClient = null)   {
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

 

## Where using a not in and deleteAll()

    UserGroupAssessmentOrder.where {
    userGroupId == ug.id
    not { 'in' ('assessmentOrderId' , command.assessmentOrderIds.collect{(Long) it} )}
    }.deleteAll()

     def results = Person.withCriteria {

      notIn "firstName", Person.where { age < 18 }.firstName
      }

 
## Chain where query

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

 

 
## Creating criteria with a join and row count

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

## WHERE QUERY WITH DETACHED CRITERIA AND PROP PROJECTION JOINS

              //subquery find users with managers whose name is like the search string
             //query use list from subquery to find all users and create map to match filtered users search
          def queryByManagerName(String searchString, List<Long> filteredUsers, sort, order, _clientSetupId, 
          statusFilters, unassignedManagerFilter, 
        unassignedAssessmentFilter) {
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
                          .collect { [id: it[0], username: it[1], clientSetupId: it[2], email: it[3], firstName: 
         it[4], lastName: it[5], userMetadata: 
         it[6], lastLogin: it[7], pictureUrl: it[8]] }
         }

         def (firstNameSearch, lastNameSearch, rest) = searchString?.tokenize()
        def stringToRegex = { string -> string ? "^${string}.*" : null }


### EXAMPLE2
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
    }


## Criteria Builder with bean transformation
[TransformResultToCustomBean](https://gist.github.com/14paxton/f384df3ac36befc64c894eeb28439387)
 
