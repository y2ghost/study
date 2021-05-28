import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class InsertSerialID {
    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if (args.length>0) {
            inputFile = args[0];
        }

        InputStream is = null;
        if (null != inputFile) {
            is = new FileInputStream(inputFile);
        } else {
            is = System.in;
        }

        ANTLRInputStream input = new ANTLRInputStream(is);
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        ParseTree tree = parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        InsertSerialIDListener extractor = new InsertSerialIDListener(tokens);
        walker.walk(extractor, tree);

        /* print back ALTERED stream */
        System.out.println(extractor.rewriter.getText());
    }
}
