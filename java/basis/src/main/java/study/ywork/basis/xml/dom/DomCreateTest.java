package study.ywork.basis.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import static java.lang.System.err;
import static java.lang.System.out;

public class DomCreateTest {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            // Create the root element.
            Element root = doc.createElement("movie");
            doc.appendChild(root);
            // Create name child element and add it to the
            // root.
            Element name = doc.createElement("name");
            root.appendChild(name);
            // Add a text element to the name element.
            Text text = doc.createTextNode("Le Fabuleux " + "Destin" + "Poulain");
            name.appendChild(text);
            // Create language child element and add it to the
            // root.
            Element language = doc.createElement("language");
            root.appendChild(language);
            // Add a text element to the language element.
            text = doc.createTextNode("franais");
            language.appendChild(text);
            out.printf("Version = %s%n", doc.getXmlVersion());
            out.printf("Encoding = %s%n", doc.getXmlEncoding());
            out.printf("Standalone = %b%n%n", doc.getXmlStandalone());
            NodeList nl = doc.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                    dump((Element) node);
            }
        } catch (FactoryConfigurationError fce) {
            err.printf("FCE: %s%n", fce.toString());
        } catch (ParserConfigurationException pce) {
            err.printf("PCE: %s%n", pce.toString());
        }
    }

    static void dump(Element e) {
        out.printf("Element: %s, %s, %s, %s%n", e.getNodeName(), e.getLocalName(), e.getPrefix(), e.getNamespaceURI());
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element element) {
                dump(element);
            } else if (node instanceof Text text) {
                out.printf("Text: %s%n", text.getWholeText());
            }
        }
    }
}