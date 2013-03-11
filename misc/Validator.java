package se.xalan.misc;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
//import org.xml.sax.AttributeList;

import org.xml.sax.Attributes;

import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import java.io.IOException;


public class Validator extends DefaultHandler {
   final String parserClass = "org.apache.xerces.parsers.SAXParser";
   StringBuffer result = new StringBuffer("");
   
 //parsemetoden i XMLReader vill helst ha en InputSource {s.45 och 47}
   public String process(InputSource inputSource){
    String message = "";
       XMLReader reader = null;
      // Create instances needed for parsing
      try {
	 reader = XMLReaderFactory.createXMLReader(parserClass);
      } catch (SAXException e) {
	 System.err.println(e.getMessage());
      }
      
      // Register content handler      
      reader.setContentHandler(this);
      
      // Register error handler
      reader.setErrorHandler(this);
      
      // Turn on validation
      try{
	 reader.setFeature("http://xml.org/sax/features/validation", true);
	 reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
      }catch (SAXNotRecognizedException e) {
	 System.err.println(e.getMessage());
      }catch (SAXNotSupportedException e) {
	 System.err.println(e.getMessage());
      }
      
      // Parse
      try{
	 reader.parse(inputSource);
	 message =  this.result.toString();
      }catch (IOException e) {
 System.err.println(e.getMessage());
      }catch (SAXException e) {
System.err.println(e.getMessage());
      }
      if (message.equals(""))
       message = "Dokumentet är validerat korrekt.";
      else message = "De val som gjorts följer inte cancerregistrets XMLSchema.\n" + 
	    "Gör en ny mall eller försök tolka följande:\n" +
	    message;
      return message;
   }
 
 
 /*
   public static void main(String[] args) {
   if (args.length != 1) {
   System.out.println("Usage: java validate <XML file>");
   return;
   }
   try {
   String validationResults = process(args[0]);
   System.out.println(validationResults);
   }catch (Exception e) { e.printStackTrace(); }
   }
 */

 
 /** Warning. */
 public void warning(SAXParseException spex) {
  
  if(result.length()>800)return;
  result.append("[Warning] "+
		getLocationString(spex)+": "+
		spex.getMessage() + "\n");
 }
 
 /** Error. */
 public void error(SAXParseException spex) {
  
  if(result.length()>800)return;
  result.append("[Error] "+
		getLocationString(spex)+": "+
		spex.getMessage() + "\n");
 }
 
 /** Fatal error. */
 public void fatalError(SAXParseException spex) throws SAXException {
  
  if(result.length()>800)return;  
  result.append("[Fatal Error] "+
		getLocationString(spex)+": "+
		spex.getMessage() + "\n");
 }
 
 /** Returns a string of the location. */
 private String getLocationString(SAXParseException spex) {
  StringBuffer str = new StringBuffer();
  
  String systemId = spex.getSystemId();
  if (systemId != null) {
   int index = systemId.lastIndexOf('/');
   if (index != -1)
    systemId = systemId.substring(index + 1);
   str.append(systemId);
  }
  str.append(':');
  str.append(spex.getLineNumber());
  str.append(':');
  str.append(spex.getColumnNumber());
  
  return str.toString();
 } // getLocationString(SAXParseException):String
 
}//
