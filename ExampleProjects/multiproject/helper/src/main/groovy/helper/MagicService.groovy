package helper

import grails.artefact.Enhances

@Enhances('Service')        // <1>
trait MagicService {

    def getMagicNumber() {  // <2>
        42
    }
}
