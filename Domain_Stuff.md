## Usising Discriminator Formula-
## was used to modify existing domain to add a parent , and existing records would have a class
```groovy
       discriminator formula: "case when (type = 'GROUP_COMPARE') then '${UserGroupType.GROUP_COMPARE.key}' when (type = 'RESULT_COMPARE') then             '${UserGroupType.RESULT_COMPARE.key}' when (type in ('RESULTGROUP', 'MYSAVEDGROUP')) then '${UserGroupType.RESULTGROUP.key}' else '${UserGroupType.GROUPDEFAULT.key}' end " ,
     type: 'string',
     value: UserGroupType.GROUPDEFAULT.key
```

