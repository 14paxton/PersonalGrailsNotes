package demoapp

class DemoController {

    def renderMessage(String msg) {
        render msg?.upperCaseVowels()
    }

    def pigLatin(String msg) {
        render msg?.pigLatin
    }
}
