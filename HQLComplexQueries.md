# Intersect

```sql
SELECT *
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
AND lts.inttestid = ltl.inttestid)
```

## join same object to query against 2 lists

```sql
select o from Object as o
join o.otherObjects as otherObject
where
otherObject in :allowedotherobjects
and otherObject not in :excludedotherobjects
```

## determine length diff of a group concat

```sql
(CHAR_LENGTH(GROUP_CONCAT(CONCAT(user.id, manager.id))) - CHAR_LENGTH(REPLACE(GROUP_CONCAT(CONCAT(user.id, manager.id)), ',', '' )))
```

# EXAMPLE large query with teary / multi join/ and JSON extractor

```sql
SELECT USER.FIRSTNAME,
       USER.LASTNAME,
       USER.USERMETADATA,
       USER.LASTLOGIN,
       USER.PICTUREURL,
       MANAGER.ID,
       MANAGER.EMAIL,
       MANAGER.FIRSTNAME,
       MANAGER.LASTNAME,
       MANAGER.USERMETADATA,
       MANAGER.LASTLOGIN,
       MANAGER.PICTUREURL,
       MANAGER.EXTERNALEMPLOYEECODE,
       AO.ID,
       AO.COMPANYINTERVIEWNAME,
       AO.COMPLETEDDATE,
       AO.CATALOGDETAIL.ID,
       SM.ALIAS)
FROM ${User user}
    LEFT OUTER JOIN ${UserRelationship ur}
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN ${ur.manager} manager
    INNER JOIN ${AssessmentOrder ao}
WITH ao.id = CAST ((CONCAT(FUNCTION ('JSON_EXTRACT', USER.userMetadata, ' $.defaultAssessmentOrderId '))) AS java.lang.Long)
    LEFT OUTER JOIN ${InterviewModel im}
WITH im.sourceId = ao.catalogDetail.interviewModelId
    AND im.source = (CASE WHEN ao.catalogDetail.type = ' AO6 ' THEN ' TBSIX ' WHEN ao.catalogDetail.type = ' A05 ' THEN ' TBFIVE '
    WHEN ao.catalogDetail.type = ' P2P ' THEN ' TBFIVE ' END)
    LEFT OUTER JOIN ${ScoringModel sm}
ON sm.id = (CASE WHEN im.source = ' TBFIVE ' THEN (SELECT s FROM ScoringModel s WHERE
    s.interviewModelId = im.id) ELSE (SELECT s FROM ScoringModel s WHERE s.sourceId = ao.catalogDetail.interviewModelId) END)
WHERE USER.clientSetupId = ${clientSetupId}
  AND USER.id IN (${searchStrings?.lastName ? findAllIdsByFirstNameAndLastName(searchStrings.firstName.toString()
    , searchStrings.lastName.toString())*.getId().join(', ')
  
  
```

> calling method

```groovy
findAllByFirstNameOrLastNameOrEmail(searchStrings.firstName.toString())*.getId().join('
```

# EXAMPLE calling method in HQL statement

```sql
SELECT DISTINCT new COM.TALENTBANK.CORE.DTO.USERTEAM.TEAMSEARCHDTOMAP(USER.ID, USER.USERNAME, USER.CLIENTSETUPID, USER.EMAIL, USER.FIRSTNAME, USER.LASTNAME, USER.USERMETADATA, USER.LASTLOGIN, USER.PICTUREURL,
MANAGER.ID, MANAGER.EMAIL, MANAGER.FIRSTNAME, MANAGER.LASTNAME, MANAGER.USERMETADATA, MANAGER.LASTLOGIN, MANAGER.PICTUREURL,
MANAGER.EXTERNALEMPLOYEECODE)
FROM USER USER
LEFT OUTER JOIN UserRelationship ur
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN USER manager
ON manager.id = ur.manager.id
WHERE USER.clientSetupId = 2000
  AND USER.id IN (${ findAllByFirstNameOrLastNameOrEmail(searchStrings.firstName)*.getId().join(' , ')}))
```

# EXAMPLE case in where statement

```sql
SELECT DISTINCT new Map( USER.ID AS USER, MANAGER.ID AS manager )
FROM USER USER
  LEFT OUTER JOIN UserRelationship ur
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN USER manager
ON manager.id = ur.manager.id
WHERE USER.clientSetupId IN (55
    , 2000)
  AND (USER.firstName LIKE '%'||'${testSearch}'||'%'
   OR USER.lastName LIKE '%'||'${testSearch}'||'%'
   OR
    manager.firstName LIKE CASE WHEN ${searchManagerName} = TRUE THEN ('%'||'${testSearch}'||'%') ELSE '' END
   OR
    manager.lastName LIKE CASE WHEN ${searchManagerName} = TRUE THEN ('%'||'${testSearch}'||'%') ELSE '' END )
```

# return all if null or empty

```sql
SELECT DISTINCT new Map( USER.ID AS USER, MANAGER.ID AS manager )
FROM USER USER
  LEFT OUTER JOIN UserRelationship ur
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN USER manager
ON manager.id = ur.manager.id
WHERE USER.clientSetupId IN (${clientSetUpIdList.join(' , ') ?: ClientSetup.all.id.join(' , ') })
  AND (USER.firstName LIKE ${testSearch.user}
   OR USER.lastName LIKE ${testSearch.user}
   OR
    manager.firstName LIKE ${testSearch.manager}
   OR
    manager.lastName LIKE ${testSearch.manager} )
```

# subquery

```sql
SELECT u
FROM USER u
WHERE exists (SELECT 1
              FROM USER user
    LEFT OUTER JOIN UserRelationship UR WITH UR.USER.ID = USER.ID OR UR.MANAGER.ID = USER.ID
    left OUTER JOIN User manager WITH MANAGER.ID = UR.MANAGER.ID
    WHERE user = u
        AND USER.CLIENTSETUPID = 2000)
```

# create tuple

```sql
SELECT CONCAT('[', USER.ID, ':', IFNULL(MANAGER.ID, 'null'), ']')
FROM USER USER
LEFT OUTER JOIN UserRelationship ur
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN USER manager
WITH manager.id = ur.manager.id
WHERE USER.clientSetupId = 2000
```

# creative count

## add/concat chars get length

```sql
SELECT LENGTH(CONCAT(FUNCTION('GROUP_CONCAT', ',')))
                         From User user
                         LEFT OUTER JOIN UserRelationship ur with ur.user.id = user.id or ur.manager.id = user.id
                         left OUTER JOIN User manager with manager.id = ur.manager.id
                         where user.clientSetupId = 2000
                         group by user.id, manager.id
```

## get groupings where there may be nulls

```sql
SELECT NEW Map( MAX(USER.ID) AS userId , (SELECT CONCAT('{', GROUP_CONCAT(CONCAT(COALESCE(UR1.ID, 'noRelationship'), ':[{' ,
                         USER.ID, ':' , COALESCE(MANAGER1.ID, 'null'), '}]' )) , '}')
                         FROM USER USER
                         JOIN UserRelationship ur1 WITH ur1.user.id = USER.id OR ur1.manager.id = USER.id
                         JOIN USER manager1 WITH manager1.id = ur1.manager.id) AS TUPLES )
FROM USER USER
    LEFT OUTER JOIN UserRelationship ur
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN USER manager
WITH manager.id = ur.manager.id
WHERE USER.clientSetupId = 2000
GROUP BY USER.id, manager.id
```

## get list of digits

```sql
SELECT Max(USER.ID),
       (SELECT DISTINCT CONCAT(GROUP_CONCAT(1))
        FROM USER u
                 LEFT OUTER JOIN USERRELATIONSHIP ur
WITH ur.user.id = u.id OR ur.manager.id = u.id
    LEFT OUTER JOIN USER M
WITH M.id = ur.manager.id
WHERE u.clientSetupId = 2000
  AND manager.id = m.id
    )
FROM USER USER
    LEFT OUTER JOIN UserRelationship ur
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN USER manager
WITH manager.id = ur.manager.id
WHERE USER.clientSetupId = 2000
GROUP BY USER, manager
```

## get correct char but need to count column

### Not working need to show one number, find way to count column

```sql
SELECT (SELECT COUNT(u.id)
        FROM USER u
                 LEFT OUTER JOIN USERRELATIONSHIP ur
WITH ur.user.id = u.id OR ur.manager.id = u.id
    LEFT OUTER JOIN USER M
WITH M.id = ur.manager.id
WHERE u.id IN (
    SELECT DISTINCT CONCAT(''
    , GROUP_CONCAT(USER.id
    , manager.id)
    , '')
    FROM u subu
    LEFT OUTER JOIN UserRelationship subur WITH subur.user.id = subu.id
   OR subur.manager.id = subu.id
    LEFT OUTER JOIN USER subm WITH subm.id = subur.manager.id
    WHERE USER.clientSetupId = 2000
    GROUP BY CONCAT( USER.id
    , IFNULL(manager.id
    , 666))
    )
    )
FROM USER USER
    LEFT OUTER JOIN UserRelationship ur
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN USER manager
WITH manager.id = ur.manager.id
WHERE USER.clientSetupId = 2000
GROUP BY 'all'
```

```sql
SELECT COUNT(*),
       (SELECT LENGTH(CONCAT(GROUP_CONCAT('')))
        FROM USER u
                 LEFT OUTER JOIN USERRELATIONSHIP rel
WITH rel.user.id = u.id OR rel.manager.id = u.id
    LEFT OUTER JOIN USER M
WITH M.id = rel.manager.id
WHERE u.clientSetupId = 2000
  AND u.id = USER.id
  AND (m.id = manager.id
   OR (u.id IS NOT NULL
  AND m.id IS NULL))
    )
FROM USER USER
    LEFT OUTER JOIN UserRelationship ur
WITH ur.user.id = USER.id OR ur.manager.id = USER.id
    LEFT OUTER JOIN USER manager
WITH manager.id = ur.manager.id
WHERE USER.clientSetupId = 2000
GROUP BY USER, manager
```
