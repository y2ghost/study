package study.ywork.basis.api.annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FieldInfo {
    private final Map<String, String> fields;
    private final List<String> mandatoryFields;

    public FieldInfo(Map<String, String> fields, List<String> mandatoryFields) {

        this.fields = fields;
        this.mandatoryFields = mandatoryFields;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public List<String> getMandatoryFields() {
        return mandatoryFields;
    }

    public static FieldInfo get(Element element) {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        List<String> mandatoryFields = new ArrayList<>();

        for (ExecutableElement executableElement : ElementFilter.methodsIn(element.getEnclosedElements())) {
            ElementKind kind = executableElement.getKind();
            String methodName = executableElement.getSimpleName().toString();
            String fieldName = methodToFieldName(methodName);
            String returnType = executableElement.getReturnType().toString();

            if (kind != ElementKind.METHOD
                    || null == fieldName
                    || "void".equals(returnType)) {
                continue;
            }

            fields.put(fieldName, returnType);
            if (executableElement.getAnnotation(Mandatory.class) != null) {
                mandatoryFields.add(fieldName);
            }
        }

        return new FieldInfo(fields, mandatoryFields);
    }

    private static String methodToFieldName(String methodName) {
        if (methodName.startsWith("get")) {
            String str = methodName.substring(3);
            if (str.length() == 0) {
                return null;
            } else if (str.length() == 1) {
                return str.toLowerCase();
            } else {
                return Character.toLowerCase(str.charAt(0)) + str.substring(1);
            }
        }

        return null;
    }
}
