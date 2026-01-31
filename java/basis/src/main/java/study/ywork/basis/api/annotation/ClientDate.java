package study.ywork.basis.api.annotation;

import java.time.LocalDate;

/*
 * 测试可插拔的注解处理器: DateFormatProcessor
 * 步骤先编译整个项目，得到xxx.jar
 * 然后测试下单独编译本类，结果如下:
 * 错误: 日期格式无效 yyyy/MM/xd
 * 编译指令示例:
 * javac -cp build/libs/basis-1.1.jar src/main/java/study/ywork/basis/api/annotation/ClientDate.java
 */
public class ClientDate {
    @DateFormat("yyyy/MM/xd")
    private LocalDate date;
}
