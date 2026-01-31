package study.ywork.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.io.IOException;

public class TreeModelExample {
    public static void main(String[] args) throws IOException {
        String inputJson = "{\"name\":\"yy\",\"salary\":3000,\"phones\":"
            + "[{\"phoneType\":\"cell\",\"phoneNumber\":\"111-111-111\"},"
            + "{\"phoneType\":\"work\",\"phoneNumber\":\"222-222-222\"}]," + "\"taskIds\":[11,22,33],"
            + "\"address\":{\"street\":\"one street\",\"city\":\"yueyang\"}}";
        System.out.println("input json: " + inputJson);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputJson);
        System.out.printf("root: %s type=%s%n", rootNode, rootNode.getNodeType());
        traverse(rootNode, 1);
    }

    private static void traverse(JsonNode node, int level) {
        if (node.getNodeType() == JsonNodeType.ARRAY) {
            traverseArray(node, level);
        } else if (node.getNodeType() == JsonNodeType.OBJECT) {
            traverseObject(node, level);
        } else {
            throw new UnsupportedOperationException("还未实现");
        }
    }

    private static void traverseObject(JsonNode node, int level) {
        node.fieldNames().forEachRemaining((String fieldName) -> {
            JsonNode childNode = node.get(fieldName);
            printNode(childNode, fieldName, level);
            if (traversable(childNode)) {
                traverse(childNode, level + 1);
            }
        });
    }

    private static void traverseArray(JsonNode node, int level) {
        for (JsonNode jsonArrayNode : node) {
            printNode(jsonArrayNode, "arrayElement", level);
            if (traversable(jsonArrayNode)) {
                traverse(jsonArrayNode, level + 1);
            }
        }
    }

    private static boolean traversable(JsonNode node) {
        return node.getNodeType() == JsonNodeType.OBJECT || node.getNodeType() == JsonNodeType.ARRAY;
    }

    private static void printNode(JsonNode node, String keyName, int level) {
        int indent = level * 4 - 3;
        String value = null;

        if (traversable(node)) {
            value = node.toString();
        } else {
            if (node.isTextual()) {
                value = node.textValue();
            } else if (node.isNumber()) {
                value = node.numberValue().toString();
            }
        }

        StringBuilder builder = new StringBuilder("%");
        builder.append(indent).append("s|-- %s=%s type=%s%n");
        System.out.printf(builder.toString(), "", keyName, value, node.getNodeType());
    }
}