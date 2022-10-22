// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern =
                '%d{yyyy-MM-dd HH:mm:ss.SSS} ' +
                        '%5p ' +
                        '--- %-30.30logger{20} : ' +
                        '%msg%n'
    }
}

root(ERROR, ['STDOUT'])

logger 'gormdemo.PersonDemo', INFO, ['STDOUT'], false