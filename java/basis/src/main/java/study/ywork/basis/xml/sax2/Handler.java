package study.ywork.basis.xml.sax2;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;
import static java.lang.System.out;

public class Handler extends DefaultHandler2 {
    private Locator locator;

    @Override
    public void characters(char[] ch, int start, int length) {
        out.print("元素内容 [");
        for (int i = start; i < start + length; i++)
            out.print(ch[i]);
        out.println("]");
    }

    @Override
    public void comment(char[] ch, int start, int length) {
        out.print("注释内容 [");
        for (int i = start; i < start + length; i++)
            out.print(ch[i]);
        out.println("]");
    }

    @Override
    public void endCDATA() {
        out.println("CDATA结束");
    }

    @Override
    public void endDocument() {
        out.println("XML文档结束");
    }

    @Override
    public void endDTD() {
        out.println("DTD结束");
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        out.print("元素结束 ");
        out.printf("uri=[%s], ", uri);
        out.printf("localName=[%s], ", localName);
        out.printf("qName=[%s]%n", qName);
    }

    @Override
    public void endEntity(String name) {
        out.print("实体结束 ");
        out.printf("entityName=[%s]%n", name);
    }

    @Override
    public void endPrefixMapping(String prefix) {
        out.print("前缀映射结束 ");
        out.printf("prefix=[%s]%n", prefix);
    }

    @Override
    public void error(SAXParseException saxpe) {
        out.printf("分析错误: %s%n", saxpe.toString());
    }

    @Override
    public void fatalError(SAXParseException saxpe) {
        out.printf("致命错误 %s%n", saxpe.toString());
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) {
        out.print("忽略空白字符 [");
        for (int i = start; i < start + length; i++)
            out.print(ch[i]);
        out.println("]");
    }

    @Override
    public void notationDecl(String name, String publicId, String systemId) {
        out.print("符号定义 ");
        out.printf("declName=[%s], ", name);
        printPublicSystemId(publicId, systemId);
    }

    @Override
    public void processingInstruction(String target, String data) {
        out.print("处理指令 ");
        out.printf("target=[%s], ", target);
        out.printf("data=[%s]%n", data);
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        out.print("解析实体 ");
        printPublicSystemId(publicId, systemId);
        InputSource is = new InputSource();
        is.setPublicId(publicId);
        is.setSystemId(systemId);
        return is;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        out.print("setDocumentLocator() ");
        out.printf("locator=[%s]%n", locator);
        this.locator = locator;
    }

    @Override
    public void skippedEntity(String name) {
        out.print("忽略实体 ");
        out.printf("name=[%s]%n", name);
    }

    @Override
    public void startCDATA() {
        out.println("CDATA开始");
    }

    @Override
    public void startDocument() {
        out.println("XML文档开始");
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) {
        out.print("DTD开始 ");
        out.printf("name=[%s], ", name);
        printPublicSystemId(publicId, systemId);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        out.print("元素开始 ");
        out.printf("uri=[%s], ", uri);
        out.printf("localName=[%s], ", localName);
        out.printf("qName=[%s]%n", qName);
        for (int i = 0; i < attributes.getLength(); i++)
            out.printf("  属性: %s, %s%n", attributes.getLocalName(i), attributes.getValue(i));
        out.printf("Column number=[%d]%n", locator.getColumnNumber());
        out.printf("Line number=[%d]%n", locator.getLineNumber());
    }

    @Override
    public void startEntity(String name) {
        out.print("实体开始 ");
        out.printf("name=[%s]%n", name);
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        out.print("startPrefixMapping() ");
        out.printf("prefix=[%s], ", prefix);
        out.printf("uri=[%s]%n", uri);
    }

    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) {
        out.print("未分析的实体定义 ");
        out.printf("name=[%s], ", name);
        printPublicSystemId(publicId, systemId);
        out.printf("notationName=[%s]%n", notationName);
    }

    @Override
    public void warning(SAXParseException saxpe) {
        out.printf("警告 %s%n", saxpe.toString());
    }
    
    private void printPublicSystemId(String publicId, String systemId) {
        out.printf("publicId=[%s], ", publicId);
        out.printf("systemId=[%s], ", systemId);
    }
}