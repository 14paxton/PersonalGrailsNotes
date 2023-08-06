---
title:        Domain_Stuff
permalink:    PersonalGrailsNotes/Domain_Stuff
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

# Binding

## [Groovy Examples](https://github.com/14paxton/GroovyNotes/wiki/MetaProgramming.md#objects)

```groovy
domain.properties = newMap as BindingResult
```

# Usising Discriminator

## - utilizing fromula

## was used to modify existing domain to add a parent , and existing records would have a class

```java
class Group {
    String name
    UserGroupType type
    Long userId
    Long interviewModelId
    Long clientSetupId
    Date dateCreated
    Date lastUpdated
    Visibility visibility = Visibility.PRIVATE

    static hasMany =[groupAssessmentOrders:UserGroupAssessmentOrder,userGroupShare:UserGroupShare]
    static constraints =

    {
        interviewModelId nullable:true
        name(unique: ['clientSetupId', 'userId', 'type'])
        groupAssessmentOrders cascade:"all-delete-orphan"
        visibility nullable:true
    }

    static mapping =

    {
        table "user_group"
        discriminator formula:"case when (type = 'GROUP_COMPARE') then '${UserGroupType.GROUP_COMPARE.key}' when (type = 'RESULT_COMPARE') then '${UserGroupType.RESULT_COMPARE.key}' when (type in ('RESULTGROUP', 'MYSAVEDGROUP')) then '${UserGroupType.RESULTGROUP.key}' else '${UserGroupType.GROUPDEFAULT.key}' end ",
            type:'string',
            value:UserGroupType.GROUPDEFAULT.key

        groupAssessmentOrders joinTable: ["user_group_assessment_order",
            column:'user_group_assessment_order_id',
            key:'user_group_id'] //Custom Join Table name and Column

        visibility defaultValue:Visibility.PRIVATE
    }
}


class ResultCompare extends Group {

    static hasOne =[groupCompareJoinUserGroup :GroupCompareJoinUserGroup]
    static mapping =

    {
        table 'user_group'
        groupCompareJoinUserGroup nulable:true, cascade:"all-delete-orphan"
        discriminator value:UserGroupType.RESULT_COMPARE.key
        groupAssessmentOrders cascade:"all-delete-orphan"
    }

    ResultCompare() {
        this.type = UserGroupType.RESULT_COMPARE
    }

    ResultCompare(Group group, Long userId, String name) {
        this.type = UserGroupType.RESULT_COMPARE
        this.interviewModelId = group.properties.get('interviewModelId') as Long
        this.clientSetupId = group.properties.get('clientSetupId') as Long
        this.type = UserGroupType.RESULT_COMPARE
        this.userId = userId
        this.name = name
        this.groupCompareJoinUserGroup = new GroupCompareJoinUserGroup(userGroup:group)
    }

}


class UserGroup extends Group {
    static hasMany =[userGroupJoinGroupCompare :GroupCompareJoinUserGroup]

    static constraints =

    {
    }

    static mapping =

    {
        table 'user_group'
        discriminator value:UserGroupType.RESULTGROUP.key

        userGroupJoinGroupCompare cascade:"all", joinTable: [name:
    'user_group_group_compare',
            column:'group_compare_id',
            key:'user_group_id']
    }
}


class GroupCompare extends Group {

    static hasMany =[userGroupJoinGroupCompare:GroupCompareJoinUserGroup]
    static constraints =

    {
        userGroupShare nullable:true
    }

    static mapping =

    {
        table 'user_group'
        discriminator value:UserGroupType.GROUP_COMPARE.key
        userGroupJoinGroupCompare cascade:"all-delete-orphan",
            joinTable: [name:
    'group_compare_user_group', column:'user_group_id', key:'group_compare_id']
        groupAssessmentOrders cascade:"all-delete-orphan"
    }

    GroupCompare() {
        this.type = UserGroupType.GROUP_COMPARE
    }

    GroupCompare(Group group, Long userId, String name) {
        this.interviewModelId = group ?.interviewModelId as Long
        this.clientSetupId = group ?.clientSetupId as Long
        this.type = UserGroupType.GROUP_COMPARE
        this.userId = userId
        this.name = name
        GroupCompareJoinUserGroup join = new GroupCompareJoinUserGroup(userGroup:group, groupToCompare:this)
        addToUserGroupJoinGroupCompare(join)
    }

    void addUserGroupToCompare(Group group) {
        if (this.interviewModelId == group ?.interviewModelId){
            GroupCompareJoinUserGroup join = new GroupCompareJoinUserGroup(userGroup:group)
            addToUserGroupJoinGroupCompare(join)
        }
    }
}

class GroupCompareJoinUserGroup implements Serializable {
    Group groupCompare
    Group userGroup
    static belongsTo =[groupCompare:Group,userGroup :Group]

    static constraints =

    {
        groupCompare unique:'userGroup'
        userGroup cascade:"none", validator:{
        Group val, GroupCompareJoinUserGroup obj, errors ->
        if (val.interviewModelId != obj.groupCompare.interviewModelId) errors.reject("different_interview_models", "interviewModelId is not the same for these groups")
    }
    }

    static mapping =

    {
        version f
```

## standard

```java
class TeamCompare extends Team {

    Collection teamsToCompare

    static hasMany =[teamsToCompare:TeamCompareJoinUser]

    static mapping =

    {
        table "team_compare"
        discriminator value:TeamCompareType.TEAM_COMPARE.key
        teamsToCompare cascade:'all-delete-orphan'
    }

    TeamCompare(User team, User user, String name, Long clientSetupId) {
        this.user = user
        this.name = name
        this.clientSetupId = clientSetupId
        TeamCompareJoinUser join = new TeamCompareJoinUser(team:team, teamCompare:this as Team)
        addToTeamsToCompare(join)
    }

    void addTeamsToCompare(User team) {
        TeamCompareJoinUser join = new TeamCompareJoinUser(team:team)
        addToTeamsToCompare(join)
    }
}
```