package demoapp

import demoapp.DemoController
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class DemoControllerSpec extends Specification implements ControllerUnitTest<DemoController> {

    void "test upper case vowels"() {
        when:
        params.msg = 'Selling England By The Pound'
        controller.renderMessage()

        then:
        response.text == 'SEllIng EnglAnd By ThE POUnd'
    }

    void "test pig latin"() {
        when:
        params.msg = 'jake'
        controller.pigLatin()

        then:
        response.text == 'akejay'
    }
}
