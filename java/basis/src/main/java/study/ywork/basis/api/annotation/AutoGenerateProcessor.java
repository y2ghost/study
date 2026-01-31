package study.ywork.basis.api.annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SupportedAnnotationTypes({"study.ywork.basis.api.annotation.AutoImplement"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AutoGenerateProcessor extends AbstractProcessor {
    private static final String PUBLIC = "public";
    private static final String PRIVATE = "private";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutoImplement.class);
        List<String> uniqueIdCheckList = new ArrayList<>();

        for (Element element : elements) {
            AutoImplement autoImplement = element.getAnnotation(AutoImplement.class);
            if (element.getKind() != ElementKind.INTERFACE) {
                error("The annotation @AutoImplement can only be applied on interfaces: ", element);
            } else {
                if (uniqueIdCheckList.contains(autoImplement.as())) {
                    error("AutoImplement#as should be uniquely defined", element);
                }

                boolean error = !checkIdValidity(autoImplement.as(), element);
                if (!error) {
                    uniqueIdCheckList.add(autoImplement.as());
                    try {
                        generateClass(autoImplement, element);
                    } catch (Exception e) {
                        error(e.getMessage(), null);
                    }
                }
            }
        }

        return false;
    }

    private JClass generateBuilderClass(String className, FieldInfo fieldInfo) {
        JClass builder = new JClass();
        String builderClassName = className + "Builder";
        builder.defineClass("public static class", builderClassName, null);
        builder.addFields(fieldInfo.getFields());
        builder.addConstructor(PRIVATE, fieldInfo.getMandatoryFields());

        for (Map.Entry<String, String> entry : fieldInfo.getFields().entrySet()) {
            String name = entry.getKey();
            String type = entry.getValue();
            boolean mandatory = fieldInfo.getMandatoryFields().contains(name);

            if (!mandatory) {
                builder.addMethod(new JMethod().defineSignature(PUBLIC, false, builderClassName)
                        .name(name)
                        .addParam(type, name)
                        .defineBody(" this." + name + " = " + name + ";" + JClass.LINE_BREAK + " return this;"));
            }
        }

        JMethod createMethod = new JMethod().defineSignature(PUBLIC, true, builderClassName).name("create");
        StringBuilder paramString = new StringBuilder("(");
        int i = 0;

        for (String s : fieldInfo.getMandatoryFields()) {
            createMethod.addParam(fieldInfo.getFields().get(s), s);
            paramString.append(i != 0 ? ", " : "").append(s);
            i++;
        }

        paramString.append(");");
        createMethod.defineBody("return new " + builderClassName + paramString);
        builder.addMethod(createMethod);
        JMethod buildMethod = new JMethod().defineSignature(PUBLIC, false, className).name("build");
        StringBuilder buildBody = new StringBuilder();
        buildBody.append(className)
                .append(" a = new ")
                .append(className)
                .append(paramString)
                .append(JClass.LINE_BREAK);

        for (String s : fieldInfo.getFields().keySet()) {
            if (fieldInfo.getMandatoryFields().contains(s)) {
                continue;
            }

            buildBody.append("a.").append(s).append(" = ").append(s).append(";").append(JClass.LINE_BREAK);
        }

        buildBody.append("return a;").append(JClass.LINE_BREAK);
        buildMethod.defineBody(buildBody.toString());
        builder.addMethod(buildMethod);
        return builder;
    }

    private void generateClass(AutoImplement autoImplement, Element element) throws Exception {
        String pkg = getPackageName(element);
        FieldInfo fieldInfo = FieldInfo.get(element);
        String interfaceName = getTypeName(element);
        String className = autoImplement.as();
        JClass implClass = new JClass();
        implClass.definePackage(pkg);
        implClass.defineClass("public class ", className, "implements " + interfaceName);

        implClass.addFields(fieldInfo.getFields());
        for (Map.Entry<String, String> entry : fieldInfo.getFields().entrySet()) {
            String name = entry.getKey();
            boolean mandatory = fieldInfo.getMandatoryFields().contains(name);
            implClass.createGetterForField(name);

            if (!mandatory) {
                implClass.createSetterForField(name);
            }
        }

        String constructorAccessModifier = PUBLIC;
        if (autoImplement.builder()) {
            JClass builder = generateBuilderClass(className, fieldInfo);
            implClass.addNestedClass(builder);
            constructorAccessModifier = PRIVATE;
        }

        implClass.addConstructor(constructorAccessModifier, fieldInfo.getMandatoryFields());
        generateClass(pkg + "." + autoImplement.as(), implClass.end());
    }

    private String getPackageName(Element element) {
        List<PackageElement> packageElements = ElementFilter.packagesIn(Collections.singletonList(element.getEnclosingElement()));
        Optional<PackageElement> packageElement = packageElements.stream().findAny();
        return packageElement.map(value -> value.getQualifiedName().toString()).orElse(null);
    }

    private void generateClass(String qfn, String end) throws IOException {
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(qfn);
        try (Writer writer = sourceFile.openWriter()) {
            writer.write(end);
        }
    }

    private boolean checkIdValidity(String name, Element e) {
        boolean valid = true;
        for (int i = 0; i < name.length(); i++) {
            if (i == 0 ? !Character.isJavaIdentifierStart(name.charAt(i))
                    : !Character.isJavaIdentifierPart(name.charAt(i))) {
                error("AutoImplement#as should be valid java " + "identifier for code generation: " + name, e);
                valid = false;
            }
        }

        if (name.equals(getTypeName(e))) {
            error("AutoImplement#as should be different than the Interface name ", e);
        }

        return valid;
    }

    private static String getTypeName(Element e) {
        TypeMirror typeMirror = e.asType();
        String[] split = typeMirror.toString().split("\\.");
        return split.length > 0 ? split[split.length - 1] : null;
    }

    private void error(String msg, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
}
