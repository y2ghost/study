package study.ywork.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger 用于发送消息
 * Appender 用于写入消息
 * Layout 格式化消息
 * 所有日志记录器默认继承root根日志记录器
 * 日志记录器采用名称的层次结构处理继承机制
 * 层次结构和Java的包结构类似
 */
public class Example {
    private static final Logger logger = LoggerFactory.getLogger(Example.class);

    public String getGreeting() {
        return "hello logback";
    }

    public static void main(String[] args) {
        logger.info("Example log from {}", Example.class.getSimpleName());
        System.out.println(new Example().getGreeting());
        ExampleContext.main(args);
    }
}
