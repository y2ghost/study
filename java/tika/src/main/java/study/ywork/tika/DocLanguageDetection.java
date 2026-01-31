package study.ywork.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class DocLanguageDetection {
    public static void main(final String[] args) throws IOException, SAXException, TikaException {
        File file = new File("example.png");
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream content = new FileInputStream(file);
        parser.parse(content, handler, metadata, new ParseContext());
        System.out.println("Content-Type :" + metadata.get("Content-Type"));
    }
}
