** -Usising Discriminator Formula-
** -was used to modify existing domain to add a parent , and existing records would have a class
=discriminator formula: "case when (type = 'GROUP_COMPARE') then '${UserGroupType.GROUP_COMPARE.key}' when (type = 'RESULT_COMPARE') then '${UserGroupType.RESULT_COMPARE.key}' when (type in ('RESULTGROUP', 'MYSAVEDGROUP')) then '${UserGroupType.RESULTGROUP.key}' else '${UserGroupType.GROUPDEFAULT.key}' end " ,
type: 'string',
value: UserGroupType.GROUPDEFAULT.key=


*** -Demo Command Object with Validation-*
*** -command object allows you to validate with services-*

=class CompareCommand implements Serializable, Validateable {
GroupCompareService groupCompareService
ResultCompareService resultCompareService

Long id
String name
UserGroupType type
List<Long> groupIds
List<Long> assessmentOrderIds

CompareCommand() {

}

CompareCommand(String name, UserGroupType type, List<Long> groupIds, List<Long> assessmentOrderIds) {
    this.id = id
    this.name = name
    this.type = type
    this.groupIds = (groupIds && !assessmentOrderIds.isEmpty()) ? groupIds*.toLong() : null
    this.assessmentOrderIds = (assessmentOrderIds && !assessmentOrderIds.isEmpty()) ? assessmentOrderIds*.toLong() : null
}

static constraints = {
    id nullable: true
    name size: 1..50
    type nullable: false, inList: [UserGroupType.GROUP_COMPARE, UserGroupType.RESULT_COMPARE]
    groupIds nullable: true, validator: { val, obj ->
        if (obj?.type == UserGroupType.GROUP_COMPARE) obj.groupCompareService.validateCommand(obj, val)
    }
    assessmentOrderIds nullable: true, validator: { val, obj ->
        if (obj?.type == UserGroupType.RESULT_COMPARE) obj.resultCompareService.validateCommand(obj, val)
    }
}

Long getId() {
    return id
}

void setId(Long id) {
    this.id = id
}
}=


*** -JSON return bodies Spring/Jackson examples-*
*** https://www.baeldung.com/jackson-json-view-annotation*

https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-jackson
