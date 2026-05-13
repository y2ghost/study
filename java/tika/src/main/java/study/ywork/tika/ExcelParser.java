package study.ywork.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class ExcelParser {
    public static void main(final String[] args) throws IOException, TikaException, SAXException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("example.xlsx"));
        ParseContext pcontext = new ParseContext();
        OOXMLParser excelParser = new OOXMLParser();
        excelParser.parse(inputstream, handler, metadata, pcontext);
        System.out.println("Contents of the excel:" + handler.toString());
        System.out.println("Metadata of the excel:");
        String[] metadataNames = metadata.names();

        for (String name : metadataNames) {
            System.out.println(name + " : " + metadata.get(name));
        }
    }
}
