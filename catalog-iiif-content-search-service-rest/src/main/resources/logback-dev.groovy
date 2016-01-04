statusListener(OnConsoleStatusListener)
scan()

def appenderList = []
appenderList.add("CONSOLE")

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}

root(WARN, appenderList)
logger("org.springframework.web.client.RestTemplate", ERROR)

logger("no.nb.microservices", DEBUG)