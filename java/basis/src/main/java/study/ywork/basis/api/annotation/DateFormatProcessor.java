package study.ywork.basis.api.annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// 自定义日期检查注解处理器
public class DateFormatProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Messager messager = processingEnv.getMessager();
        for (TypeElement typeElement : annotations) {
            for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                DateFormat annotation = element.getAnnotation(DateFormat.class);
                TypeMirror typeMirror = element.asType();

                // 检查字段类型
                // 尚未构建代码，所以没有字段类型信息
                // 所以采用字符串比较的方式
                if (!typeMirror.toString().equals(LocalDate.class.getName())) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "用@DateFormat注释的类型必须是LocalDate");
                }

                try {
                    // 检查日期格式
                    DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern(annotation.value());
                    LocalDate.now().format(simpleDateFormat);

                } catch (Exception e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "日期格式无效 " + annotation.value());
                }
            }
        }

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(DateFormat.class.getName()));
    }
}
