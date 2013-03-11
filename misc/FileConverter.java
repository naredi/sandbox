package se.xalan.misc;
import javax.swing.*;


import java.io.*;
import java.util.ArrayList;

class FileConverter
 {
    private GuiClass gui;

    private String FILEDIRECTORY = "C:\\Documents and Settings\\folke\\IdeaProjects\\Fileconversion\\fileDir";

    //Textfilen som ska läsas in
    File textInFile = null;

    //Fil för att spara resultatet i XML-format
    File resultFile = null;




    private String DELIMITER = "\t";

    private Randomizer rand = new Randomizer();
    private DataContainer cont = new DataContainer();
    private Loader loader = new Loader(DELIMITER);

    private String nextrow = "";
    private int rowcount = 0;

     public FileConverter(GuiClass gui){
         this.gui = gui;
     }

     public FileConverter(){
     }

     public static void main (String[] args) {

         FileConverter fc = new FileConverter();

         if (args.length == 0) {
             help("No arguments supplied");
         }
         String cmd = args[0];
         String inputTxt;
         String outputXml;

         if (cmd.equals("-conv")) {
             if (args.length < 3) {
                 help("Too few arguments supplied");
             }
             if (args.length > 3) {
                 help("Too many arguments supplied");
             }
             inputTxt = args[1];
             outputXml = args[2];
             fc.execute(new File(inputTxt),new File(outputXml));
         } else {
             help(null);
         }
     }

    /**
     * Öppnar infil
     */
    public void openInfile(String fileExtension, String fileDesciption){
        JOptionPane.showMessageDialog(gui,
                "Börja med att markera den fil " +
                "som du valt att hämta " +
                "inmatningsdata från.");
        textInFile = getInFile(fileExtension, fileDesciption);
        if (textInFile != null)
            if (confirmSave())
                convert();
    }

    private boolean confirmSave(){
        int value = JOptionPane.showConfirmDialog(gui,"Infilen har laddats.\n" +
                "Välja nu ett namn och plats på resultatfilen.\n" +
                "Generera sedan genom att trycka på [Spara].","Slutför åtgärd",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (value == JOptionPane.YES_OPTION)
            return true;
        else
            return false;

    }

    private void convert(){
        resultFile = getOutFile("txt","Resultatfil");

        if (execute(textInFile, resultFile)){
            java.awt.Toolkit.getDefaultToolkit().beep();
            // Konverteringen gick bra
            JOptionPane.showMessageDialog(gui,
                    "Konverteringen lyckades.\n" +
                    "Den genererade filen heter " +
                    resultFile.getName() +
                    " och går att finna i\n" +
                    FILEDIRECTORY + ".");
        }
        else{
            java.awt.Toolkit.getDefaultToolkit().beep();
            // Konverteringen gick bra
            JOptionPane.showMessageDialog(gui,
                    "Konverteringen misslyckades.");

        }
    }


    /**
     * Öppnar en textfil för enbart läsning genom en JFileChooser.
     */
    private File getInFile(String fileExtension, String fileDesciption){
        File file = null;
        JFileChooser fileChooser = new JFileChooser(FILEDIRECTORY);
        fileChooser.setFileFilter(new PilotFileFilter(fileExtension, fileDesciption));
        int returnVal = fileChooser.showDialog(fileChooser.getParent(), "Öppna");
        if (returnVal == JFileChooser.APPROVE_OPTION){
            try{
                file = fileChooser.getSelectedFile();
                gui.setFileText(file.getPath());
            }catch(Exception e){
                JOptionPane.showMessageDialog(gui, "Fel: Filen kunde inte öppnas", "File Error", JOptionPane.WARNING_MESSAGE);
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


    /**
     * Öppnar en fil genom en JFileChooser för att spara det konverterade reultatet.
     */
    private File getOutFile(String fileExtension, String fileDescription){
        File file = null;
        String fileName = "";
        JFileChooser fileChooser = new JFileChooser(FILEDIRECTORY);
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
                }else if (fileExtension.equals("txt")){
                    if (!(getExtension(file).equals("txt"))){
                        fileName = file.getPath() + ".txt";
                        file = new File(fileName);
                    }
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(gui, "Fel: Resultatfilen kunde inte öppnas", "File Error", JOptionPane.WARNING_MESSAGE);
            }
        }
        return file;
    }


    private ArrayList elementList = new ArrayList();
    //Fil för att spara eller öppna en mall
    File templateFile;


    public void openTemplate(String fileExtension, String fileDescription){
        templateFile = getInFile(fileExtension, fileDescription);
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(templateFile));
            elementList = (ArrayList)in.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        gui.updateList(elementList.toArray());
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

        private boolean execute(File infile, File outfile){

            //read the infile
            //load all Lists with uniqe data
            //remember the number of rows
            try{

                BufferedReader br = new BufferedReader(new FileReader(infile));

                // iterate through the infile one row at a time
                // read and save all usefull data in the DataContainer class
                while((nextrow = br.readLine()) != null) {
                    rowcount++;
                    cont = loader.loadStringArrays(nextrow, cont);
                }

            }catch(Exception e){System.out.println("e = " + e);}

            // load the randomizer class with the same number of
            // newpairId's as there where pairs in the infile
            rand.setPairIdList(rowcount);

            //populate the resultfile
            try{
                PrintWriter pw = new PrintWriter(new FileWriter(outfile));

                //Check if there are more pairs to load
                while(rand.areMorePairs()){
                    String[] twinInPair = createTwinPair();
                    pw.write(
                            twinInPair[0] + "\r\n" +
                            twinInPair[1] + "\r\n");
                }
    //            pw.println("\r\n" + rowcount);
                pw.close();
                return true;
            }catch(Exception e){
                return false;
            }
        }



    private String[] createTwinPair(){
        //get the surname at birth of the twinpair
        int numbersOfSurnames = cont.getSurnameArray().size();
        long indexOfRandomSurname = Math.round(Math.random()*(numbersOfSurnames-1));
        String surname = (String)cont.getSurnameArray().get(new Long(indexOfRandomSurname).intValue());


        String[] twinpair = new String[2];
        String newPairId = rand.getPairId();
        String dateOfBirth = rand.getBirthdate();
        twinpair[0] = getTwin(newPairId, "1", dateOfBirth, surname);
        twinpair[1] = getTwin(newPairId, "2", dateOfBirth, surname);
        return twinpair;
    }

    private String getTwin(String pairid, String twinid, String dateOfBirth, String surname){

        // [1 = male, 2 = female]
        String sex = rand.getRandomSex();

        // set civil_stat on all twins
        // [0 eller 1 = ogift, 2 = gift, 7 = ?, 9 = okänd]
        String civil_stat = rand.getRandomCivilStatus();

        // if twin is female and civil status is maried this person should get a new surname
        if(sex.equals("2") && civil_stat.equals("2")){
            int numbersOfSurnames = cont.getSurnameArray().size();
            long indexOfRandomSurname = Math.round(Math.random()*(numbersOfSurnames-1));
            surname = (String)cont.getSurnameArray().get(new Long(indexOfRandomSurname).intValue());
        }

        //insert column 0-2 (twinnr)(pairid)(tvab)  (OK)
        String row = pairid + twinid + "\t" + pairid + "\t" + twinid;

        //insert column 3-4 (personnr)(birthday)    (OK)
        row = row + "\t" + dateOfBirth + rand.getUniqePrnNum(sex) + "\t" + dateOfBirth;

        //insert column 5-8 (last_first_name)(christian_name)(middle_name)(surname) (OK)
        String givenName = "";
        if(sex.equals("1")){
            int numbersOfMaleGivenNames = cont.getMaleGivenNameArray().size();
            long indexOfRandomMaleGivenName = Math.round(Math.random()*(numbersOfMaleGivenNames-1));
            givenName = (String)cont.getMaleGivenNameArray().get(new Long(indexOfRandomMaleGivenName).intValue());
        }
        else{
            int numbersOfFemaleGivenNames = cont.getFemaleGivenNameArray().size();
            long indexOfRandomFemaleGivenName = Math.round(Math.random()*(numbersOfFemaleGivenNames-1));
            givenName = (String)cont.getFemaleGivenNameArray().get(new Long(indexOfRandomFemaleGivenName).intValue());
        }
        row = row + "\t" + surname + ", " + givenName + "\t" + givenName + "\t" + "" + "\t" + surname;

        //insert column 9-10 (co_address)(fo_address)  (OK)
        row = row + "\t" + "" + "\t" + "";

        //insert column 11 (address)    (OK)
        int numbersOfAddresses = cont.getAddressArray().size();
        long indexOfRandomAddress = Math.round(Math.random()*(numbersOfAddresses-1));
        String address = (String)cont.getAddressArray().get(new Long(indexOfRandomAddress).intValue());
        row = row + "\t" + address;

        //insert column 12-17 (restAdress)  (OK)
        int numbersOfRestAddresses = cont.getRestAddressArray().size();
        long indexOfRandomRestAddress = Math.round(Math.random()*(numbersOfRestAddresses-1));
        String restAddress = (String)cont.getRestAddressArray().get(new Long(indexOfRandomRestAddress).intValue());
        row = row + "\t" + restAddress;

        //insert column 18 (address_source)  (OK)
        row = row + "\t" + "generated dummies";

        //insert column 19-21 (telephone)(dead)(death_date)  (OK)
        row = row + "\t" + "" + "\t" + "" + "\t" + "";

        //insert column 22 (sex)    (OK)
        row = row + "\t" + sex;

        //insert column 23-30 (zygosity)(ptvab)(rantvab)(prantvab)(kinship_no1)
        //                    (kinship_relation1)(kinship_no2)(kinship_relation2)  (OK)
        row = row + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "";

        //insert column 31 (civilstatus)    (OK)
        row = row + "\t" + civil_stat;

        //insert column 32-35 (latest_match_code)(eff_date)(xpnr)(nottwin)  (OK)
        row = row + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "";

        return row;
    }

/*
ElementTemplate et;
public void addElementTemplate(){
et = new ElementTemplate(gui.getTagName(), gui.getType(), gui.getDescription(), gui.getStart());
elementList.add(et);
gui.updateList(elementList.toArray());
}
*/

    private static String usage =
            "Usage: java -jar tmmbParser.jar [-conv <inputfile(.txt)> <outputfile(.xml)>]\n" +
                    "                                      [-h]\n"+
                    "\n"+
                    "  -conv  - Generate the xml output file. This option takes two arguments.\n" +
                    "           The first is a text file listing TACs, models, manufacturers \n" +
                    "           and supported band (one TAC per line), the second is the resuling\n" +
                    "           output xml file.\n"+
                    "\n"+
                    "  -h     - Show this information\n"+
                    "\n"+
                    "\n"+
                    "  The expected layout for the out put xml file is:\n"+
                    "\n"+
                    "         document root\n"+
                    "               |\n"+
                    "               |-  manufacturerNameX\n"+
                    "               |        |\n"+
                    "               |        |-  modelName1\n"+
                    "               |        |       |\n"+
                    "               |        |        -  TAC1\n"+
                    "               |        |             |\n"+
                    "               |        |              -  Band1\n"+
                    "               |        |\n"+
                    "               |         -  modelName2\n"+
                    "               |                |\n"+
                    "               |                |-  TAC1\n"+
                    "               |                |     |\n"+
                    "               |                |     |-  Band1\n"+
                    "               |                |      -  Band2\n"+
                    "               |                |     \n"+
                    "               |                |-  TAC2\n"+
                    "               |                |     |\n"+
                    "               |                |      -  Band1\n"+
                    "               |                |\n"+
                    "               |                 -  TAC3\n"+
                    "               |                      |\n"+
                    "               |                      |-  Band1\n"+
                    "               |                      |-  Band2\n"+
                    "               |                       -  Band3\n"+
                    "                ...\n"+
                    "\n"+
                    "\n"+
                    " Example Usage:\n"+
                    "\n"+
                    "  The following command will generate an xml document file from an input textfile:\n"+
                    "\n"+
                    "    java -jar dmPackageBuilder.jar -conv models.lst depot/devices\n"+
                    "\n"+
                    "  The resulting xml file will be named tmmb.xml\n";

     private static void help(String errMsg) {
         if (errMsg != null) {
             System.out.println(errMsg);
             System.out.println("");
         }
         System.out.println(usage);
         System.exit(0);
     }

}





