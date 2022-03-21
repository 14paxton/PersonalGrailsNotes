## Intersect
$/ 
  `SELECT *
   FROM LAB_TEST_SERVICES_POJO lts
   WHERE EXISTS
   (SELECT lsm.inttestid 
    FROM LAB_SPECIMEN_MAPPING lsm
    WHERE lsm.status = 1
    AND lts.inttestid = lsm.inttestid)
    AND EXISTS
    (SELECT ltl.inttestid 
    FROM LAB_TEST_LOCATION ltl
    WHERE ltl.status = 1
    AND lts.inttestid = ltl.inttestid)`
/$

## join same object to query against 2 lists
`select o from Object as o 
join o.otherObjects as otherObject 
where 
    otherObject in :allowedotherobjects 
    and otherObject not in :excludedotherobjects`


## determine length diff of a group concat
`(CHAR_LENGTH(GROUP_CONCAT(CONCAT(user.id, manager.id))) - CHAR_LENGTH(REPLACE(GROUP_CONCAT(CONCAT(user.id, manager.id)), ',', '' )))
`

# EXAMPLE large query with teary / multi join/ and JSON extractor
$/  
 
    SELECT DISTINCT new com.talentbank.core.dto.userTeam.TeamSearchDTOMap(user.id, user.username, user.clientSetupId, user.email, 
    user.firstName, user.lastName, user.userMetadata, user.lastLogin, user.pictureUrl,
            manager.id, manager.email, manager.firstName, manager.lastName, manager.userMetadata, manager.lastLogin, manager.pictureUrl, 
     manager.externalEmployeeCode,
            ao.id, ao.companyInterviewName, ao.completedDate, ao.catalogDetail.id, sm.alias)
            FROM ${User user}
            LEFT OUTER JOIN ${UserRelationship ur} with ur.user.id = user.id or ur.manager.id = user.id
            LEFT OUTER JOIN ${ur.manager} manager
            INNER JOIN ${AssessmentOrder ao} with ao.id = CAST((CONCAT(FUNCTION('JSON_EXTRACT', user.userMetadata, ' 
          '$.defaultAssessmentOrderId'))) as java.lang.Long)
            LEFT OUTER JOIN ${InterviewModel im} with im.sourceId = ao.catalogDetail.interviewModelId 
                    and im.source = (CASE when ao.catalogDetail.type = 'AO6' THEN 'TBSIX' when ao.catalogDetail.type = 'A05' THEN 'TBFIVE' 
    when ao.catalogDetail.type = 'P2P' THEN 'TBFIVE' end)
            LEFT OUTER JOIN ${ScoringModel sm} on sm.id = (CASE when im.source = 'TBFIVE' then (select s from ScoringModel s where 
     s.interviewModelId = im.id) else (select s from ScoringModel s where s.sourceId = ao.catalogDetail.interviewModelId) end)        
            WHERE user.clientSetupId = ${clientSetupId}
            and user.id in (${searchStrings?.lastName ? findAllIdsByFirstNameAndLastName(searchStrings.firstName.toString(), 
       searchStrings.lastName.toString())*.getId().join(' , ')
                                                      : 
       findAllByFirstNameOrLastNameOrEmail(searchStrings.firstName.toString())*.getId().join(' , ')})


/$


# EXAMPLE calling method in HQL statement

$/



                SELECT DISTINCT new com.talentbank.core.dto.userTeam.TeamSearchDTOMap(user.id, user.username, user.clientSetupId, user.email, 
     user.firstName, user.lastName, user.userMetadata, user.lastLogin, user.pictureUrl,
                manager.id, manager.email, manager.firstName, manager.lastName, manager.userMetadata, manager.lastLogin, manager.pictureUrl, 
     manager.externalEmployeeCode)
                  FROM User user
                  LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
                  LEFT OUTER JOIN User manager on manager.id = ur.manager.id
                  WHERE user.clientSetupId = 2000
                  and user.id in (${ findAllByFirstNameOrLastNameOrEmail(searchStrings.firstName)*.getId().join(' , ')})

/$)

# EXAMPLE case in where statement

$/


                SELECT DISTINCT new Map( user.id as user, manager.id as manager )
                  FROM User user
                  LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
                  LEFT OUTER JOIN User manager on manager.id = ur.manager.id
                  WHERE user.clientSetupId in (55, 2000)
                  AND (user.firstName like '%'||'${testSearch}'||'%' or user.lastName like '%'||'${testSearch}'||'%' OR 
                        manager.firstName like CASE WHEN ${searchManagerName} = true THEN ('%'||'${testSearch}'||'%') ELSE '' END OR
                        manager.lastName like CASE WHEN ${searchManagerName} = true THEN ('%'||'${testSearch}'||'%') ELSE '' END )


/$

# return all if null or empty
$/



                SELECT DISTINCT new Map( user.id as user, manager.id as manager )
                  FROM User user
                  LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
                  LEFT OUTER JOIN User manager on manager.id = ur.manager.id
                  WHERE user.clientSetupId in (${clientSetUpIdList.join(' , ') ?: ClientSetup.all.id.join(' , ') })
                  AND (user.firstName like ${testSearch.user}  or user.lastName like ${testSearch.user}  OR 
                        manager.firstName like ${testSearch.manager}  OR
                        manager.lastName like ${testSearch.manager}  )

/$

# subquery
$/


    select u
    from User u           
     where exists (Select 1
               from User user
               LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
               left OUTER JOIN User manager with manager.id = ur.manager.id
               where user = u
               and user.clientSetupId = 2000) 

   

/$

# create tuple
$/

    SELECT CONCAT('[', user.id, ':', IFNULL(manager.id, 'null'), ']') 
     From User user
     LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
     left OUTER JOIN User manager with manager.id = ur.manager.id
     where user.clientSetupId = 2000

/$

# creative count

## add/concat chars get length
$/


    SELECT LENGTH(CONCAT(FUNCTION('GROUP_CONCAT', ',')))
     From User user
     LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
     left OUTER JOIN User manager with manager.id = ur.manager.id
     where user.clientSetupId = 2000
    group by user.id, manager.id


/$

## get groupings where there may be nulls
$/

           SELECT new Map( MAX(user.id) as userId , (SELECT CONCAT('{', GROUP_CONCAT(CONCAT(COALESCE(ur1.id, 'noRelationship'), ':[{' , 
           user.id, ':' , COALESCE(manager1.id, 'null'), '}]' )) , '}')
                                          FROM User user
                                          JOIN UserRelationship ur1 with ur1.user.id = user.id OR ur1.manager.id = user.id
                                          JOIN User manager1 with manager1.id = ur1.manager.id) as tuples )
              FROM User user
              LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
              LEFT OUTER JOIN User manager with manager.id = ur.manager.id
              WHERE user.clientSetupId = 2000
              GROUP BY user.id, manager.id

/$

## get list of digits
$/

        SELECT Max(user.id), (
        select DISTINCT CONCAT(GROUP_CONCAT(1) )
      From  user u
     LEFT OUTER JOIN UserRelationship ur with ur.user.id = u.id or ur.manager.id = u.id
     left OUTER JOIN User m with m.id = ur.manager.id
     where u.clientSetupId = 2000
        and manager.id = m.id
         )
     From User user
     LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
     left OUTER JOIN User manager with manager.id = ur.manager.id
     where user.clientSetupId = 2000
      group by user, manager

/$

## get correct char but need to count column
#### Not working need to show one number, find way to count column

$/

    SELECT (
       SELECT COUNT(u.id)
      From  user u
     LEFT OUTER JOIN UserRelationship ur with ur.user.id = u.id or ur.manager.id = u.id
     left OUTER JOIN User m with m.id = ur.manager.id
     where u.id in (
       select DISTINCT CONCAT('', GROUP_CONCAT(user.id, manager.id), '')
      from u subu
       LEFT OUTER JOIN UserRelationship subur with subur.user.id = subu.id or subur.manager.id = subu.id
     left OUTER JOIN User subm with subm.id = subur.manager.id
     where user.clientSetupId = 2000
     group by CONCAT( user.id, IFNULL(manager.id, 666)) 
       )
       ) 
     From User user
     LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
     left OUTER JOIN User manager with manager.id = ur.manager.id
     where user.clientSetupId = 2000
     group by 'all'

/$



$/

    SELECT COUNT(*),   
    (SELECT  LENGTH(CONCAT(GROUP_CONCAT('')))
     From  user u
     LEFT OUTER JOIN UserRelationship rel with rel.user.id = u.id or rel.manager.id = u.id
     left OUTER JOIN User m with m.id = rel.manager.id
      where u.clientSetupId = 2000
      and u.id = user.id
      and (m.id = manager.id or (u.id is not null and m.id is null))
      )
      From User user
     LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
     left OUTER JOIN User manager with manager.id = ur.manager.id
     where user.clientSetupId = 2000
      group by user, manager
 
/$
