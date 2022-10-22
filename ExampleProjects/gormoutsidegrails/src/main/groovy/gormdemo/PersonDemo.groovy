package gormdemo

import groovy.util.logging.Slf4j
import org.grails.orm.hibernate.HibernateDatastore

@Slf4j
class PersonDemo {

    static void main(args) {
        Map configuration = [
                'hibernate.hbm2ddl.auto': 'create-drop',
                'dataSource.url'        : 'jdbc:h2:mem:myDB'
        ]
        def datastore = new HibernateDatastore(configuration, Person)
        def service = datastore.getService(PersonService)

        service.savePerson('Robert', 'Fripp')
        service.savePerson('Ritchie', 'Blackmore')
        service.savePerson('Eric', 'Clapton')
        service.savePerson('Jeff', 'Beck')
        service.savePerson('David', 'Gilmour')
        service.savePerson('Randy', 'Rhoads')

        def people = service.getPeople()
        for (Person p : people) {
            log.info "${p.lastName}, ${p.firstName}"
        }
    }
}