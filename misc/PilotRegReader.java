package se.xalan.misc;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;


public class PilotRegReader extends DefaultHandler
   implements XMLReader{

   AttributesImpl a = new AttributesImpl();
   ContentHandler contentHandler;
   String indent = "\n                                                      ";
   int indentTimes = 0;
   ArrayList<PilotElementTemplate> elementList;
   String line;

   public PilotRegReader(ArrayList<PilotElementTemplate> al){
      elementList = al;
   }


   /**
    *Parsar infilen
    */
   public void parse(InputSource input)
      throws IOException, SAXException{
      try{
	 Reader r = input.getCharacterStream();
	 BufferedReader br = new BufferedReader(r);

	 contentHandler.startDocument();
	 
	 a.addAttribute("","","xmlns:xsi","CDATA","http://www.w3.org/2001/XMLSchema-instance\n");
	 a.addAttribute("","","\t\txmlns","CDATA","http://www.dsv.su.se/~folke-na/MEP/schema\n");
	 a.addAttribute("","","     xsi:schemaLocation","CDATA","http://www.dsv.su.se/~folke-na/MEP/schema cancerreg.xsd\n");
	 contentHandler.startElement("", "forskningsdata", "forskningsdata", a);
	 a.clear();
	 indentTimes++;
	 while ((line = br.readLine()) != null){
	    contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes*4);
	    contentHandler.startElement("", "studieobjekt", "studieobjekt", a);
	    indentTimes++;

	    outputAll();

	    indentTimes--;
	    contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes*4);
	    contentHandler.endElement("", "studieobjekt", "studieobjekt");
	 }
	 contentHandler.ignorableWhitespace("\n".toCharArray(), 0, 1);
	 indentTimes--;
	 contentHandler.endElement("", "forskningsdata", "forskningsdata");
	 contentHandler.endDocument();
      }catch(Exception e){
	 e.printStackTrace();
      }
   }


   private void outputAll(){
      PilotElementTemplate et;
      for(Iterator<PilotElementTemplate> iter = elementList.iterator(); iter.hasNext(); ){
	 et = (PilotElementTemplate)iter.next();
	 if (et.getType().equals("Text")){
	    output(et.getName(), et.getStart(), et.getEnd());
	 }
	 else if (et.getType().equals("Datum")){
	    outputDate(et);
	 }
	 else if (et.getType().equals("Dödsorsakskod")){
	    outputCODCode(et);
	 }
      }
   }

   public void outputDate(PilotElementTemplate et){
      StringBuffer sb;
      String date = line.substring(et.getStart()-1, et.getEnd());
      
      /* Tar ut tecknen för årtalet och kontrollerar dem
       * med avseende på att årtalet finns och att det
       * motsvarar ett årtal mellan 1850 och 2002.
       * Om årtalet är korrupt sätts flagga som hindrar
       * att programmet bearbetar datumet på ett
       * felaktigt sätt.
       */      
       boolean isYear = true;
       String yearString = date.substring(0, 4);
       String yearTrimed = yearString.trim();
       int yearInt = -1;
       try{
       yearInt = Integer.parseInt(yearTrimed);
       }catch(Exception e){;}
       if ((yearTrimed.length() != 4) || (1850 > yearInt) ||(yearInt > 2002)){
       isYear = false;	  
       }
      
      String temp = date.trim();
      
      if (isYear && temp.length() == 8){
	  String day = temp.substring(6); 
	 String month = temp.substring(4, 6);
	 sb = new StringBuffer(temp);
	 sb.insert(4, '-');
	 sb.insert(7, '-');
	 if (day.equals("00"))
	    sb.replace(8, 10, "15");   
	 if (month.equals("00"))
	    sb.replace(5, 7, "07");
	 output(et.getName(), sb.toString());
      }
      else if (isYear && temp.length() == 6){
	 a.addAttribute("","","orig","CDATA",temp);
	 temp += "15";
	 sb = new StringBuffer(temp);
	 sb.insert(4, '-');
	 sb.insert(7, '-');
	 //date = temp;
	 output(et.getName(), sb.toString());
	 a.clear();
      }
      else if (isYear && temp.length() == 4){
	 a.addAttribute("","","orig","CDATA",temp);
	 temp += "0701";
	 sb = new StringBuffer(temp);
	 sb.insert(4, '-');
	 sb.insert(7, '-');
	 output(et.getName(), sb.toString());
	 a.clear();
      }
      else if (temp.length() == 0){
	 a.addAttribute("","","orig","CDATA",temp);
	 a.addAttribute("","","xsi:nil","CDATA","true");
	 output(et.getName(),"");
	 a.clear();
      }
      else {
	 a.addAttribute("","","xsi:nil","CDATA","true");
	 output(et.getName(), "");
	 a.clear();
      }
   }

   public void outputCODCode(PilotElementTemplate et){
      String icd;
      String content = line.substring(et.getStart()-1, et.getEnd());
      try{
	 int year = Integer.parseInt(line.substring(et.getDateStart()-1,et.getDateStart()+3));
	 if (year>=1958 && year<=1968){
	    icd = "ICD-7";
	 }
	 else if (year>=1969 && year<=1986){
	    icd = "ICD-8";
	 }
	 else if (year>=1987 && year<=1996){
	    icd = "ICD-9";
	 }
	 else if (year>=1997 && year<=2002){
	    icd = "ICD-10";
	 }
	 else if (year<1958){
	    icd = "Dödsdatum tidigare än 1958";
	 }
	 else icd = "Felaktigt datum. ICD-version obestämbar";
      } catch (NumberFormatException nfe){
     	 icd = "Felaktigt ifyllt datum";
      }
      a.addAttribute("","","icd_ver","CDATA",icd);
      output(et.getName(),content);
      a.clear();
   }


   public void output(String name, int start, int end){
      try{
	 contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes*4);
	 contentHandler.startElement("", name, name, a);
	 String content = line.substring(start-1, end);
	 contentHandler.characters(content.toCharArray(), 0, content.length());
	 contentHandler.endElement("", name, name);
      }catch(Exception e){
	 e.printStackTrace();
      }
   }

   public void output(String name, String content){
      try{
	 contentHandler.ignorableWhitespace(indent.toCharArray(), 0, indentTimes*4);
	 contentHandler.startElement("", name, name, a);
	 contentHandler.characters(content.toCharArray(), 0, content.length());
	 contentHandler.endElement("", name, name);
      }catch(Exception e){
	 e.printStackTrace();
      }
   }


   public void setContentHandler(ContentHandler handler){
      contentHandler = handler;
   }

   public ContentHandler getContentHandler(){
      return contentHandler;
   }



   /*************************************************
    * IGNORERA RESTEN
    ************************************************/

   public void setDTDHandler(DTDHandler handler){
   }

   public DTDHandler getDTDHandler(){
      return null;
   }

   public void setEntityResolver(EntityResolver resolver){
   }

   public EntityResolver getEntityResolver(){
      return null;
   }

   public void setErrorHandler(ErrorHandler handler){
   }

   public ErrorHandler getErrorHandler(){
      return null;
   }

   public void setFeature(String name, boolean value){
   }

   public boolean getFeature(String name){
      return false;
   }

   public void setProperty(String name, Object value){
   }

   public Object getProperty(String name){
      return null;
   }

   public void parse(String input)
      throws IOException, SAXException{
   }
}



