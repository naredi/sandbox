package se.xalan.utils;

import java.io.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.*;
import se.xalan.template.*;

public class Translator {

    ArrayList<ElementTemplate> elementList;
    XSDReader xsdReader;

    public Translator(ArrayList<ElementTemplate> al) {
        elementList = al;
        xsdReader = new XSDReader(elementList);
    }

    public void translate(File in, File out) throws TransformerException, TransformerConfigurationException, FileNotFoundException {

            //Create a Transformer for output
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            FileReader fr = new FileReader(in);
            BufferedReader br = new BufferedReader(fr);
            InputSource inputSource = new InputSource(br);

            SAXSource saxSource = new SAXSource(xsdReader, inputSource);
            StreamResult streamResult = new StreamResult(out);
            transformer.transform(saxSource, streamResult);

    }
}
