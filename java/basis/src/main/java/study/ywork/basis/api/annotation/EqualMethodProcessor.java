package study.ywork.basis.api.annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class EqualMethodProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ElementFilter.typesIn(roundEnv.getRootElements())
                .stream()
                .filter(this::implementsDataInterface)
                .forEach(typeElement -> {
                    List<ExecutableElement> methods = ElementFilter.methodsIn(typeElement.getEnclosedElements());
                    if (methods.stream().noneMatch(m -> m.getSimpleName().contentEquals("equals"))) {
                        processingEnv.getMessager()
                                .printMessage(Diagnostic.Kind.ERROR, "对于类实现，必须重写'equals'方法 "
                                        + "study.ywork.basis.api.annotation.Datable. \n错误类: " + typeElement);
                    }
                });
        return false;
    }

    private boolean implementsDataInterface(TypeElement typeElement) {
        List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
        return interfaces.stream().anyMatch(theMirror -> theMirror.toString().equals(Datable.class.getName()));
    }
}
