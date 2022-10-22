package demoapp

import helper.MagicService
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class WidgetServiceSpec extends Specification implements ServiceUnitTest<WidgetService> {

    void "test that the service is decorated with the expected trait"() {
        expect:
        MagicService.isAssignableFrom WidgetService
    }

    void "test magicNumber property"() {
        expect:
        service.magicNumber == 42
    }
}
