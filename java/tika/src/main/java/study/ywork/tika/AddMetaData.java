package study.ywork.tika;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class AddMetaData {
    public static void main(final String[] args) throws IOException, TikaException, SAXException {
        File file = new File("example.txt");
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metaData = new Metadata();
        FileInputStream inputstream = new FileInputStream(file);
        ParseContext context = new ParseContext();
        parser.parse(inputstream, handler, metaData, context);
        System.out.println(" metadata elements :" + Arrays.toString(metaData.names()));
        metaData.add("Author", "Zhang San");
        metaData.set(Metadata.CONTENT_TYPE, "txt");
        System.out.println(" metadata name value pair is successfully added");
        System.out.println("Here is the list of all the metadata elements after adding new elements");
        System.out.println(Arrays.toString(metaData.names()));

        for (String name : metaData.names()) {
            System.out.println(name + ": " + metaData.get(name));
        }
    }
}
