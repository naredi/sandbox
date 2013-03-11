package se.xalan.main;

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import se.xalan.bussines.FileHandler;
import se.xalan.utils.StringUtils;

public class SimpleConverter {

    private char DELIMITER = ';';
    private List<String> firstList = Collections.synchronizedList(new ArrayList<String>());

    /**
     * @param args
     */
    public static void main(String[] args) {
        SimpleConverter converter = new SimpleConverter();
        if (args.length != 2) {
            System.exit(0);
        }
        converter.readInputFile(args[0]);
    }

    public void readInputFile(String file) {
        CSVReader opencvsReader1 = null;
        String[] nextRow = null;
        int rowcount = 0;
        // read the first input file
        try {
            opencvsReader1 = new CSVReader(new FileReader(file), DELIMITER);
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
}
