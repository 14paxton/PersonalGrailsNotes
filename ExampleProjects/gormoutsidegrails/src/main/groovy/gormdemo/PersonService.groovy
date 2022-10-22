package gormdemo

import grails.gorm.services.Service

@Service(Person)
interface PersonService {
    List<Person> getPeople()

    Person savePerson(String firstName, String lastName)
}
