package se.xalan.main;

import se.xalan.bussines.FileHandler;
import se.xalan.utils.StringUtils;

public class MainController {

    private final static String CONVERT = "CONVERT";
    private final static String COMPARE = "COMPARE";
    private static String task = COMPARE;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainController controller = new MainController();
        FileHandler fileHandler = new FileHandler();
        controller.handleArgs(args, fileHandler);

        if (CONVERT.equals(task)) {
            fileHandler.execute("sss");
        } else {
            if (fileHandler.execute()) {
                System.out.println("\n\n  Files are identical!");
            } else {
                System.out.println(ListComparator.class.getName() + ": \n\n  Different files!");
            }
        }
    }

    private void handleArgs(String[] args, FileHandler fileHandler) {

        if (args.length < 2) {
            help("Arguments missing");
        } else {
            int i = 0;
            while (args[i] != null) {
                if (args[i].startsWith("-")) {
                    if (args[i].length() == 2 && args[i].charAt(1) == 'c') {
                        //Do nothing. Default handling is to compare.
                    } else if (args[i].charAt(1) == 'd') {
                        String del = args[++i];
                        if (del.equals(",")) {
                            fileHandler.setDELIMITER(',');
                        }
                        if (del.equals(";")) {
                            fileHandler.setDELIMITER(';');
                        }
                        if (del.equals(":")) {
                            fileHandler.setDELIMITER(':');
                        }
                        if (del.equals("t") || del.equals("tab")) {
                            fileHandler.setDELIMITER('\t');
                        }
                    } else if (args[i].charAt(1) == 'f') {
                        try {
                            fileHandler.setColumnNumberFirstFile(Integer.parseInt(args[++i]));
                        } catch (NumberFormatException nfe) {
                            help(ListComparator.class.getName() + ": \n    "
                                    + StringUtils.getStackTrace(nfe, false));
                        }
                    } else if (args[i].charAt(1) == 's') {
                        try {
                            fileHandler.setColumnNumberSecondFile(Integer.parseInt(args[++i]));
                        } catch (NumberFormatException nfe) {
                            help(ListComparator.class.getName() + ": \n    "
                                    + StringUtils.getStackTrace(nfe, false));
                        }
                    } else if (args[i].equals("-conv")) {
                        task = CONVERT;
                    } else if (args[i].charAt(1) == 'h') {
                        help(null);
                    }
                    i += 1;
                } else if (i != args.length - 2) {
                    help(null);
                } else {
                    fileHandler.setFirstInputFile(args[args.length - 2]);
                    fileHandler.setSecondInputFile(args[args.length - 1]);
                    break;
                }
            }
        }
/*
        System.out.println("\n");
        System.out.println(FileHandler.class.getName() + ": Column number first file: " + fileHandler.getColumnFirstFile());
        System.out.println(FileHandler.class.getName() + ": Column number second file: " + fileHandler.getColumnSecondFile());
        System.out.println(FileHandler.class.getName() + ": Delimiter: " + fileHandler.getDELIMITER());
        System.out.println(FileHandler.class.getName() + ": First input file: " + fileHandler.getFirstInputFile());
        System.out.println(FileHandler.class.getName() + ": Second input file: " + fileHandler.getSecondInputFile());
*/
    }
    private static String usage = "Usage: java -jar mainController.jar [OPTIONAL: -c -d -f -s -h] <firstInputfile> <secondInputfile> \n"
            + "\n"
            + "  -c     - compare (default)\n"
            + "\n"
            + "  -d     - delimiter [OPTIONS: , ; : t ] (Default is ;)\n"
            + "\n"
            + "  -f     - column number of first file to be compared\n"
            + "\n"
            + "  -s     - column number of second file to be compared\n"
            + "\n"
            + "  -h     - help\n"
            + "\n"
            + "\n"
            + "  The expected outcom is:\n"
            + "\n"
            + "\n"
            + " Example Usage:\n"
            + "\n"
            + "  The following command will compare two files to see if content "
            + "  of column [f] in first file matches content of column [s] in second file :\n"
            + "\n"
            + "    java -jar listComparator.jar -f 2 -s 1 testfileOne.cvs testfileTwo.cvs\n";

    private static void help(String errMsg) {
        if (errMsg != null) {
            System.out.println(errMsg);
            System.out.println("");
        }
        System.out.println(usage);
        System.exit(0);
    }
}
