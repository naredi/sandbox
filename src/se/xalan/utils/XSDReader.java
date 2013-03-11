package se.xalan.utils;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import se.xalan.template.*;

public class XSDReader extends DefaultHandler implements XMLReader {

    AttributesImpl attributes = new AttributesImpl();
    ContentHandler contentHandler;
    String indent = "\n                                                      ";
    int indentTimes = 0;
    ArrayList<ElementTemplate> elementList;
    String line;

    public XSDReader(ArrayList<ElementTemplate> al) {
        elementList = al;
    }

    /**
     * Parses the in-file
     */
    @Override
    public void parse(InputSource input) throws IOException, SAXException {
        try {
            Reader r = input.getCharacterStream();
            BufferedReader br = new BufferedReader(r);

            contentHandler.startDocument();
            contentHandler.ignorableWhitespace("\n".toCharArray(), 0, 1);
            contentHandler.startElement("", "xs:element", "xs:element", attributes);
            indentTimes++;
            contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
            contentHandler.startElement("", "xs:complexType", "xs:complexType", attributes);
            indentTimes++;
            while ((line = br.readLine()) != null) {

                outputAll();

            }
            indentTimes--;
            contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
            contentHandler.endElement("", "xs:complexType", "xs:complexType");
            indentTimes--;
            contentHandler.ignorableWhitespace("\n".toCharArray(), 0, 1);
            contentHandler.endElement("", "xs:element", "xs:element");
            contentHandler.endDocument();
        } catch (SAXException | IOException e) {
        }
    }

    private void outputAll() {
        try {
            contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
            contentHandler.startElement("", "xs:sequence", "xs:sequence", attributes);
            indentTimes++;
            ElementTemplate et;
            for (Iterator<ElementTemplate> iter = elementList.iterator(); iter.hasNext();) {
                et = (ElementTemplate) iter.next();
                // if (et.getType().equals("Text")) {
                // output(et.getName(), et.getStart(), et.getEnd());
                // } else
                contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
                if (et.getType().equals("Date")) {
                    outputElement(et);
                }
                // else if (et.getType().equals("Code")) {
                // outputCODCode(et);
                // }
            }
            indentTimes--;
            contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
            contentHandler.endElement("", "xs:sequence", "xs:sequence");
        } catch (Exception e) {
        }
    }

    public void outputElement(ElementTemplate et) {
        String value = line.substring(et.getStart() - 1, et.getEnd()).trim();
        attributes.addAttribute("", "", "name", "CDATA", value);
        attributes.addAttribute("", "", "type", "CDATA", "xs:string");
        output();
        attributes.clear();
    }

    public void output() {
        try {
            contentHandler.startElement("", "xs:element", "xs:element", attributes);
            contentHandler.endElement("", "xs:element", "xs:element");
        } catch (Exception e) {
        }
    }

    @Override
    public void setContentHandler(ContentHandler handler) {
        contentHandler = handler;
    }

    @Override
    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    /**
     * ***********************************************
     * IGNORERA RESTEN **********************************************
     */
    @Override
    public void setDTDHandler(DTDHandler handler) {
    }

    @Override
    public DTDHandler getDTDHandler() {
        return null;
    }

    @Override
    public void setEntityResolver(EntityResolver resolver) {
    }

    @Override
    public EntityResolver getEntityResolver() {
        return null;
    }

    @Override
    public void setErrorHandler(ErrorHandler handler) {
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public void setFeature(String name, boolean value) {
    }

    @Override
    public boolean getFeature(String name) {
        return false;
    }

    @Override
    public void setProperty(String name, Object value) {
    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public void parse(String input) throws IOException, SAXException {
    }
}
