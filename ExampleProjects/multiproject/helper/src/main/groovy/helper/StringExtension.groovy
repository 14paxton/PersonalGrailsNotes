package helper

class StringExtension {

    private static vowels = ['a', 'e', 'i', 'o', 'u']

    static String upperCaseVowels(String str) {
        str.collect { character ->
            character in vowels ? character.toUpperCase() : character
        }.join()
    }

    static String getPigLatin(String s) {
        s[1..-1] + s[0] + 'ay'
    }
}
