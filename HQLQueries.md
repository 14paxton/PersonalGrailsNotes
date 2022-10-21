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