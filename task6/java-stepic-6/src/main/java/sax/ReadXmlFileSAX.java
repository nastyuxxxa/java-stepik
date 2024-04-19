package sax;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ReadXmlFileSAX {
    public static Object readXml(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SAXHandler handler = new SAXHandler();
            saxParser.parse(xmlFile, handler);

            return handler.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
