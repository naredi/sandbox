package se.xalan.utils;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import se.xalan.template.*;

public class RegReader extends DefaultHandler implements XMLReader {

    AttributesImpl attributes = new AttributesImpl();
    ContentHandler contentHandler;
    String indent = "\n                                                      ";
    int indentTimes = 0;
    ArrayList<ElementTemplate> elementList;
    String line;

    public RegReader(ArrayList<ElementTemplate> al) {
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
            contentHandler.startElement("", "cancerreg", "cancerreg", attributes);
            indentTimes++;
            while ((line = br.readLine()) != null) {
                contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
                contentHandler.startElement("", "person", "person", attributes);
                indentTimes++;

                outputAll();

                indentTimes--;
                contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
                contentHandler.endElement("", "person", "person");
            }
            contentHandler.ignorableWhitespace("\n".toCharArray(), 0, 1);
            indentTimes--;
            contentHandler.endElement("", "cancerreg", "cancerreg");
            contentHandler.endDocument();
        } catch (SAXException | IOException e) {
        }
    }

    private void outputAll() {
        for (ElementTemplate et : elementList) {
            switch (et.getType()) {
                case "Text":
                    output(et.getName(), et.getStart(), et.getEnd());
                    break;
                case "Date":
                    outputDate(et);
                    break;
                case "Code":
                    outputCODCode(et);
                    break;
            }
        }
    }

    public void outputDate(ElementTemplate et) {
        String date = line.substring(et.getStart() - 1, et.getEnd());
        String temp = date.trim();
        if (temp.length() == 8) {
            output(et.getName(), et.getStart(), et.getEnd());
        } else if (temp.length() == 6) {
            attributes.addAttribute("", "", "old", "CDATA", temp);
            temp += "15";
            date = temp;
            output(et.getName(), date);
            attributes.clear();
        } else if (temp.length() == 4) {
            attributes.addAttribute("", "", "old", "CDATA", temp);
            temp += "0701";
            date = temp;
            output(et.getName(), date);
            attributes.clear();
        } else if (temp.length() == 0) {
            output(et.getName(), "Datum saknas");
        } else {
            attributes.addAttribute("", "", "old", "CDATA", temp);
            output(et.getName(), "Felaktigt ifyllt datum");
            attributes.clear();
        }
    }

    public void outputCODCode(ElementTemplate et) {
        String icd;
        String content = line.substring(et.getStart() - 1, et.getEnd());
        try {
            int year = Integer.parseInt(line.substring(et.getDateStart() - 1, et.getDateStart() + 3));
            if (year >= 1958 && year <= 1968) {
                icd = "ICD-7";
            } else if (year >= 1969 && year <= 1986) {
                icd = "ICD-8";
            } else if (year >= 1987 && year <= 1996) {
                icd = "ICD-9";
            } else if (year >= 1997 && year <= 2002) {
                icd = "ICD-10";
            } else if (year < 1958) {
                icd = "Ddsdatum tidigare an 1958";
            } else {
                icd = "Wrong date. ICD-version indeterminable";
            }
        } catch (NumberFormatException nfe) {
            icd = "Felaktigt ifyllt datum";
        }
        attributes.addAttribute("", "", "icd_ver", "CDATA", icd);
        output(et.getName(), content);
        attributes.clear();
    }

    public void output(String name, int start, int end) {
        try {
            contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
            contentHandler.startElement("", name, name, attributes);
            String content = line.substring(start - 1, end);
            contentHandler.characters(content.toCharArray(), 0, content.length());
            contentHandler.endElement("", name, name);
        } catch (Exception e) {
        }
    }

    public void output(String name, String content) {
        try {
            contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes * 4);
            contentHandler.startElement("", name, name, attributes);
            contentHandler.characters(content.toCharArray(), 0, content.length());
            contentHandler.endElement("", name, name);
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
     * IGNORERA RESTEN
	 ***********************************************
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
