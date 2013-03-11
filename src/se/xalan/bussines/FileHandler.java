package se.xalan.bussines;

import au.com.bytecode.opencsv.CSVReader;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.xalan.domain.DataContainer;
import se.xalan.utils.Loader;
import se.xalan.utils.StringUtils;
//import javax.xml.transform.TransformerException;
//import se.xalan.main.MainController;
//import se.xalan.template.ElementTemplate;
//import se.xalan.utils.Translator;

public class FileHandler {

//    private static ArrayList<ElementTemplate> elementList = new ArrayList<>();

    private String FILEDIRECTORY = "C:\\";
    private String firstInfileName = null;
    private String secondInfileName = null;
    private char DELIMITER = ';';
    private int columnNumberFirstFile = 0;
    private int columnNumberSecondFile = 0;
    private List<Long> firstList = Collections.synchronizedList(new ArrayList<Long>());
    private List<Long> secondList = Collections.synchronizedList(new ArrayList<Long>());

    public FileHandler() {
    }

    public boolean execute(String task) {

        boolean boo = true;
    	String delimiter = "\t";
        String nextrow = "";
        BufferedReader br = null;
        DataContainer cont = new DataContainer();
        Loader loader = new Loader(delimiter);
        try {
            br = new BufferedReader(new FileReader(firstInfileName));
            while ((nextrow = br.readLine()) != null) {
                cont = loader.loadStringArrays(nextrow, cont); //TODO: simplify if possible
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            boo = !boo;
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            boo = !boo;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable t) {
                    Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, t);
                }
            }
        }

//        Translator tl = new Translator(elementList);
   //     try {
            //tl.translate(inputTxt, outputXml);
     //   } catch (TransformerException | FileNotFoundException ex) {
     //       Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            boo = !boo;
 //       }
        return boo;
    }

    public boolean execute() {
        readFirstFile();
        readSecondFile();

        sortList(firstList, "first " );
        sortList(secondList, "second ");

        return firstList.equals(secondList);

    }

    /**
     * Sorts the specified list into ascending order, according to the
     * {@linkplain Comparable natural ordering} of its elements.
     *
     * @param list
     */
    public void sortList(List<Long> list, String prefix) {
        Collections.sort(list);
        System.out.println("Sorting " + prefix + "list" + FileHandler.class.getSimpleName());
    }

    public void readFirstFile() {
        CSVReader opencvsReader1 = null;
        String[] nextRow = null;
        int rowcount = 0;
        // read the first input file
        try {
            opencvsReader1 = new CSVReader(new FileReader(getFirstInputFile()), DELIMITER);
            // iterate through the first input file one row at a time
            // read and save all useful data in the DataContainer class
            while ((nextRow = opencvsReader1.readNext()) != null) {
                rowcount++;
                String[] st = readRow(nextRow, columnNumberFirstFile);
                ArrayList<Long> arr = readFile(st);
                firstList.addAll(arr);
            }
            System.out.println(FileHandler.class.getName() + ": Rowcount: " + rowcount);
        } catch (FileNotFoundException e) {
            System.out.println(FileHandler.class.getName() + ": \n    "
                    + StringUtils.getStackTrace(e, false));
        } catch (IOException ex) {
            System.out.println(FileHandler.class.getName() + ": \n    "
                    + StringUtils.getStackTrace(ex, false));
        } finally {
            if (opencvsReader1 != null) {
                try {
                    opencvsReader1.close();
                } catch (Throwable t) {
                    System.out.println(FileHandler.class.getName() + ": \n    "
                            + StringUtils.getStackTrace(t, false));
                }
            }
        }
        opencvsReader1 = null;

    }

    public void readSecondFile() {
        CSVReader opencvsReader2 = null;
        String[] nextRow = null;
        int counter = 0;
        // read the second input file
        try {
            opencvsReader2 = new CSVReader(new FileReader(getSecondInputFile()), DELIMITER);
            // iterate through the second input file one row at a time
            // read and save all useful data in the DataContainer class
            while ((nextRow = opencvsReader2.readNext()) != null) {
                counter++;
                String[] st = readRow(nextRow, columnNumberSecondFile);
                ArrayList<Long> arr = readFile(st);
                secondList.addAll(arr);
            }
            System.out.println(FileHandler.class.getName() + ": Rowcount: " + counter);
        } catch (FileNotFoundException e) {
            System.out.println(FileHandler.class.getName() + ": \n    "
                    + StringUtils.getStackTrace(e, false));
        } catch (IOException ex) {
            System.out.println(FileHandler.class.getName() + ": \n    "
                    + StringUtils.getStackTrace(ex, false));
        } finally {
            if (opencvsReader2 != null) {
                try {
                    opencvsReader2.close();
                } catch (Throwable t) {
                    System.out.println(FileHandler.class.getName() + ": \n    "
                            + StringUtils.getStackTrace(t, false));
                }
            }
        }
        opencvsReader2 = null;
    }

    private String[] readRow(String[] columns, int columnNumber) {
        String[] result = new String[1];
        result[0] = columns[columnNumber];
        return result;
    }

    private ArrayList<Long> readFile(String[] columns) {
        ArrayList<Long> result = new ArrayList<>();
        int counter = columns.length;
        while (counter-- != 0) {
            try {
                String line = columns[counter];
                if (line != null) {
                    if (StringUtils.isNotEmpty(line)) {
                        long traId = Long.parseLong(line.trim());
                        Long l = new Long(traId);
                        result.add(l);
                    }
                }
            } catch (Throwable t) {
                System.out.println(FileHandler.class.getName() + ": \n    "
                        + StringUtils.getStackTrace(t, false));
                System.out.println();
                return null;
            }
        }
        return result;
    }

    public void setColumnNumberFirstFile(int columnNumber) {
        this.columnNumberFirstFile = columnNumber - 1;
    }

    public void setColumnNumberSecondFile(int columnNumber) {
        this.columnNumberSecondFile = columnNumber - 1;
    }

    public void setDELIMITER(char DELIMITER) {
        this.DELIMITER = DELIMITER;
    }

    public void setFirstInputFile(String firstInfileName) {
        this.firstInfileName = firstInfileName;
    }

    public void setSecondInputFile(String secondInfileName) {
        this.secondInfileName = secondInfileName;
    }

    public int getColumnFirstFile() {
        return columnNumberFirstFile+1;
    }

    public int getColumnSecondFile() {
        return columnNumberSecondFile+1;
    }

    public char getDELIMITER() {
        return DELIMITER;
    }

    public String getFirstInputFile() {
        return FILEDIRECTORY + firstInfileName;
    }

    public String getSecondInputFile() {
        return FILEDIRECTORY + secondInfileName;
    }

}
