package se.xalan.misc;

import javax.swing.*;
import java.io.*;
import java.util.*;

//Tar hand om den bildade xml filen mellan konvertering och validering
//import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;


public class PilotController{

   private Pilot pilot;
   private ArrayList<PilotElementTemplate> elementList = new ArrayList<PilotElementTemplate>();

   // Text file for input
   File textInFile = null;

   // Result file in the XML-format
   File xmlOutFile = null;

   // Template file
   File templateFile;

   public PilotController(Pilot pilot){
      this.pilot = pilot;
   }


   /**
    * Opens the in-file
    */
   public void doIt(String fileExtension, String fileDesciption){
      textInFile = getInFile(fileExtension, fileDesciption);
      if (textInFile != null)
	 	if (confirmSave())
	    	convert();
   }


   /**
    * Opens a text file for reading.
    */
   private File getInFile(String fileExtension, String fileDesciption){
      File file = null;
      JFileChooser fileChooser = new JFileChooser("C:/");
      fileChooser.setFileFilter(new PilotFileFilter(fileExtension, fileDesciption));
      int returnVal = fileChooser.showDialog(fileChooser.getParent(), "Open");
      if (returnVal == JFileChooser.APPROVE_OPTION){
	 	try{
	    	file = fileChooser.getSelectedFile();
	    	pilot.setFileText(file.getPath());
	 	}catch(Exception e){
	    	JOptionPane.showMessageDialog(pilot, "Error: The infile could not be opened.", "File Error", JOptionPane.WARNING_MESSAGE);
	 	}
      }
      return file;
   }



   /**
    * Öppnar en fil genom en JFileChooser för att spara det konverterade reultatet.
    */
 private File getOutFile(String fileExtension, String fileDescription){
  File file = null;
  String fileName = "";
  JFileChooser fileChooser = new JFileChooser("C:/");
  fileChooser.setFileFilter(new PilotFileFilter(fileExtension, fileDescription));
  int returnVal = fileChooser.showDialog(fileChooser.getParent(), "Spara");
  if (returnVal == JFileChooser.APPROVE_OPTION){
   try{
    file = fileChooser.getSelectedFile();
    if (fileExtension.equals("xml")){
     if (!(getExtension(file).equals("xml"))){
      fileName = file.getPath() + ".xml";
      file = new File(fileName);
     }
    }else if (fileExtension.equals("ptf")){
     if (!(getExtension(file).equals("ptf"))){
      fileName = file.getPath() + ".ptf";
      file = new File(fileName);
     }
    }
   }catch(Exception e){
    JOptionPane.showMessageDialog(pilot, "Fel: Filen kunde inte öppnas", "File Error", JOptionPane.WARNING_MESSAGE);
   }
  }
  return file;
 }
 

   public String getExtension(File f) {
	if(f != null) {
	    String filename = f.getName();
	    int i = filename.lastIndexOf('.');
	    if(i>0 && i<filename.length()-1) {
		return filename.substring(i+1).toLowerCase();
	    };
	}
	return "foo";
    }


  private boolean confirmSave(){
      int value = JOptionPane.showConfirmDialog(pilot,"Infilen har laddats. \n" +
					   "Vill du slutföra konverteringen?\n" +
					   "(Befintlig fil förblir ofändrad)","Bekräfta konvertering",
					   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (value == JOptionPane.YES_OPTION)
	 	return true;
      else
	 	return false;

   }
 
 
 private void convert(){
  xmlOutFile = getOutFile("xml","");
  PilotTranslator tl = new PilotTranslator(elementList);
  tl.translate(textInFile, xmlOutFile);
  if (xmlOutFile != null){
   java.awt.Toolkit.getDefaultToolkit().beep();
   // Har lagt in fråga om vill validera
   if(confirmValidate()){
    //   StreamSource streamSource = new StreamSource(xmlOutFile);
    //    InputSource inputSource = new InputSource(streamSource.getInputStream()); 
    InputSource inputSource = new InputSource(xmlOutFile.getAbsolutePath()); 
    String result = validate(inputSource);
    JOptionPane.showMessageDialog(pilot, "Convertion and validation successfully ended. \n" +
				  "Message: \n" + 
				  result);
   }else {
    JOptionPane.showMessageDialog(pilot, "Convertion successfully ended. \n" + 
				  "No validation.");
   }
  }
 }
  

   PilotElementTemplate et;
   public void addElementTemplate(){
       et = new PilotElementTemplate(pilot.getTagName(), pilot.getTypeText(), pilot.getDescription(), pilot.getStart(), pilot.getEnd());
      elementList.add(et);
      pilot.updateList((PilotElementTemplate[])elementList.toArray());
   }


   public void remove(int i){
      if(i == -1) return;
      elementList.remove(i);
      pilot.updateList((PilotElementTemplate[])elementList.toArray());
   }


   public void saveTemplate(String fileExtension, String fileDescription){
      templateFile = getOutFile(fileExtension, fileDescription);
      try{
	 ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(templateFile));
	 out.writeObject(elementList);
      }catch (Exception e){
	 e.printStackTrace();
      }
   }

 
 @SuppressWarnings("unchecked")
public void openTemplate(String fileExtension, String fileDescription){
  templateFile = getInFile(fileExtension, fileDescription);
  try{
   ObjectInputStream in = new ObjectInputStream(new FileInputStream(templateFile));
   elementList = (ArrayList<PilotElementTemplate>) in.readObject();
  }catch (Exception e){
   e.printStackTrace();
  }
      pilot.updateList((PilotElementTemplate[])elementList.toArray());
   }
 
 public String validate (InputSource inputSource){
  String validationResults = "";
  PilotValidator validator = new PilotValidator();
  try {
    validationResults = validator.process(inputSource);
  }catch (Exception e) {
   e.printStackTrace();
  }
  return validationResults;
 }


  private boolean confirmValidate(){
      int value = JOptionPane.showConfirmDialog(pilot,"Konverteringen lyckades. \n" +
					   "Vill du validera dokumentet?\n" +
					   "(Sker mot xsd-fil i denna mapp.)","Bekräfta konvertering",
					   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (value == JOptionPane.YES_OPTION)
	 	return true;
      else
	 	return false;

   }

}	










