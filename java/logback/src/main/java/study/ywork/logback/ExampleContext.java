package study.ywork.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志记录器的级别继承示例
 */
public class ExampleContext {
    public static void main(String[] args) {
        ch.qos.logback.classic.Logger parentLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("study.ywork.logback");
        parentLogger.setLevel(Level.INFO);
        Logger childLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("study.ywork.logback.tests");

        parentLogger.warn("This message is logged because WARN > INFO");
        parentLogger.debug("This message is not logged because DEBUG < INFO");
        childLogger.info("INFO == INFO");
        childLogger.debug("DEBUG < INFO");

        // 测试日志根记录器
        Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.debug("This message is logged because DEBUG == DEBUG");

        rootLogger.setLevel(Level.ERROR);
        parentLogger.warn("warn logged");
        parentLogger.error("error logged");
    }
}
