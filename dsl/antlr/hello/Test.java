import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

// 集成HelloParser示例
public class Test {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        HelloLexer lexer = new HelloLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HelloParser parser = new HelloParser(tokens);
        ParseTree tree = parser.r();
        System.out.println(tree.toStringTree(parser));
    }
}

