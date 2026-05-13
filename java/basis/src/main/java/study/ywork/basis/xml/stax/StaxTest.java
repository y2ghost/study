package study.ywork.basis.xml.stax;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.XMLEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import static java.lang.System.err;

public class StaxTest {
    private static final String NS1 = "http://www.w3.org/1999/xhtml";
    private static final String NS2 = "http://www.javajeff.ca/";

    public static void main(String[] args) {
        try {
            XMLOutputFactory factory = XMLOutputFactory.newFactory();
            FileWriter fw = new FileWriter("stax_recipe.xml");
            XMLEventWriter eventWriter = factory.createXMLEventWriter(fw);
            final XMLEventFactory eventFactory = XMLEventFactory.newFactory();
            XMLEvent event = eventFactory.createStartDocument();
            eventWriter.add(event);
            Iterator<Namespace> nsIter;
            nsIter = new Iterator<>() {
                int index = 0;
                final Namespace[] ns;

                {
                    ns = new Namespace[2];
                    ns[0] = eventFactory.createNamespace("h", NS1);
                    ns[1] = eventFactory.createNamespace("r", NS2);
                }

                @Override
                public boolean hasNext() {
                    return index != 2;
                }

                @Override
                public Namespace next() {
                    return ns[index++];
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };

            String title = "title";
            event = eventFactory.createStartElement("h", NS1, "html", null, nsIter);
            eventWriter.add(event);
            event = eventFactory.createStartElement("h", NS2, "head");
            eventWriter.add(event);
            event = eventFactory.createStartElement("h", NS1, title);
            eventWriter.add(event);
            event = eventFactory.createCharacters("Recipe");
            eventWriter.add(event);
            event = eventFactory.createEndElement("h", NS1, title);
            eventWriter.add(event);
            event = eventFactory.createEndElement("h", NS1, "head");
            eventWriter.add(event);
            event = eventFactory.createStartElement("h", NS1, "body");
            eventWriter.add(event);
            event = eventFactory.createStartElement("r", NS2, "recipe");
            eventWriter.add(event);
            event = eventFactory.createStartElement("r", NS2, title);
            eventWriter.add(event);
            event = eventFactory.createCharacters("Grilled Cheese " + "Sandwich");
            eventWriter.add(event);
            event = eventFactory.createEndElement("r", NS2, title);
            eventWriter.add(event);
            event = eventFactory.createStartElement("r", NS2, "ingredients");
            eventWriter.add(event);
            event = eventFactory.createStartElement("h", NS1, "ul");
            eventWriter.add(event);
            event = eventFactory.createStartElement("h", NS1, "li");
            eventWriter.add(event);

            Iterator<Attribute> attrIter;
            attrIter = new Iterator<>() {
                int index = 0;
                final Attribute[] attrs;

                {
                    attrs = new Attribute[1];
                    attrs[0] = eventFactory.createAttribute("qty", "2");
                }

                @Override
                public boolean hasNext() {
                    return index != 1;
                }

                @Override
                public Attribute next() {
                    return attrs[index++];
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };

            event = eventFactory.createStartElement("r", NS2, "ingredient", attrIter, null);
            eventWriter.add(event);
            event = eventFactory.createCharacters("bread slice");
            eventWriter.add(event);
            event = eventFactory.createEndElement("r", NS2, "ingredient");
            eventWriter.add(event);
            event = eventFactory.createEndElement("h", NS1, "li");
            eventWriter.add(event);
            event = eventFactory.createEndElement("h", NS1, "ul");
            eventWriter.add(event);
            event = eventFactory.createEndElement("r", NS2, "ingredients");
            eventWriter.add(event);
            event = eventFactory.createEndElement("r", NS2, "recipe");
            eventWriter.add(event);
            event = eventFactory.createEndElement("h", NS1, "body");
            eventWriter.add(event);
            event = eventFactory.createEndElement("h", NS1, "html");
            eventWriter.add(event);
            eventWriter.flush();
            eventWriter.close();
        } catch (FactoryConfigurationError fce) {
            err.printf("FCE: %s%n", fce);
        } catch (IOException ioe) {
            err.printf("IOE: %s%n", ioe);
        } catch (XMLStreamException ex) {
            err.printf("exception: %s%n", ex);
        }
    }
}
