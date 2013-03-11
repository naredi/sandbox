package se.xalan.misc;

import java.io.*;
import java.util.*;

import org.xml.sax.*;


import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

public class PilotTranslator{

   ArrayList<PilotElementTemplate> elementList;
   PilotRegReader regReader;

   public PilotTranslator(ArrayList<PilotElementTemplate> al){
      elementList = al;
      regReader = new PilotRegReader(elementList);
   }
   
   public void translate(File in, File out){
      try{
	 //Create a Transformer for output
	 TransformerFactory tFactory = TransformerFactory.newInstance();
	 Transformer transformer = tFactory.newTransformer();
	 
	 FileReader fr = new FileReader(in);
	 BufferedReader br = new BufferedReader(fr);
	 InputSource inputSource = new InputSource(br);
	 
	 SAXSource saxSource = new SAXSource(regReader, inputSource);
	 StreamResult streamResult = new StreamResult(out);
	 transformer.transform(saxSource, streamResult);
      }catch(Exception e){
	 e.printStackTrace();
      }
   }
}
