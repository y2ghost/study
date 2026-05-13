package study.ywork.basis.api.annotation;

import java.time.LocalDate;

/*
 * 测试可插拔的注解处理器: AutoGenerateProcessor
 * 步骤先编译整个项目，得到xxx.jar
 * 然后测试下单独编译本类
 * 编译指令示例:
 * javac -cp build/libs/basis-1.1.jar src/main/java/study/ywork/basis/api/annotation/IUser.java
 */
@AutoImplement(as = "User", builder = true)
public interface IUser {
    @Mandatory
    String getFirstName();

    @Mandatory
    String getLastName();

    LocalDate getDateOfBirth();

    String getPlaceOfBirth();

    String getPhone();

    String getAddress();
}
