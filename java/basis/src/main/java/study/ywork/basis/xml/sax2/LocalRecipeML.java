package study.ywork.basis.xml.sax2;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import static java.lang.System.out;

public class LocalRecipeML implements EntityResolver {
    private Map<String, String> mappings = new HashMap<>();

    LocalRecipeML() {
        mappings.put("-//FormatData//DTD RecipeML 0.5//EN", "samples/recipeml.dtd");
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        if (mappings.containsKey(publicId)) {
            out.println("获取已经缓存的 recipeml.dtd 文档");
            String cacheSystemId = mappings.get(publicId);
            InputSource localSource = new InputSource(cacheSystemId);
            out.printf("缓存的系统ID=[%s], 传入的系统ID=[%s]%n", cacheSystemId, systemId);
            return localSource;
        }

        return null;
    }
}