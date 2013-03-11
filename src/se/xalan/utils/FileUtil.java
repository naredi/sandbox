package se.xalan.utils;

import java.io.*;
import java.util.*;
import se.xalan.domain.FileMapping;
import se.xalan.domain.FileMappings;

public class FileUtil {

    private static final Logger log = new Logger(FileUtil.class);

    /**
     * Read the specified file
     *
     * @param filename the file to read
     * @return a Map of names, keyed to owner location
     */
    public static Map<String, List<String>> readOpsFile(String filename) {
        Map<String, List<String>> map = new HashMap<>();
        BufferedReader br = null;
        File tmp = new File(filename);
        if (!tmp.exists() || !tmp.isFile()) {
            log.error("Invalid file specified: " + filename);
        } else {
            try {
                br = new BufferedReader(new FileReader(filename));
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("#")) {
                        continue; // Lines starting with # are commented out
                    }
                    String[] manAndModel = new String[2];
                    if (line.startsWith("\"")) {
                        int manEnd = line.indexOf("\"", 1);
                        manAndModel[0] = line.substring(1, manEnd);

                        int modelStart = line.indexOf("\"", manEnd + 1);
                        int modelEnd = line.indexOf("\"", modelStart + 1);
                        manAndModel[1] = line.substring(modelStart + 1, modelEnd);
                    }
                    List<String> modelsForMan = map.get(manAndModel[0]); // Find manufacturers list of models
                    if (modelsForMan == null) {
                        modelsForMan = new ArrayList<>();
                        map.put(manAndModel[0], modelsForMan);
                    }
                    modelsForMan.add(manAndModel[1]);
                }

            } catch (IOException ioe) {
                log.error("Failed to read list of models: " + ioe);
                // Log error and quit
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ignore) {
                }
            }
        }
        return map;
    }

    /**
     * Reads a file containing names of directories that must be excluded from
     * the build. These names are returned as a Set.
     *
     * @param rootDir the directory where to find the <code>excludes.lst</code>
     * file
     * @return a Set of directory names
     * @throws IOException exception is thrown to the calling object
     */
    public static Set<String> findExclusions(String rootDir) throws IOException {
        Set<String> exclusions = new TreeSet<>();
        File excludes = new File(rootDir + File.separator + "excludes.lst");
        if (excludes.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(excludes))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 0) {
                        exclusions.add(line);
                    }
                }
            }
        }
        return exclusions;
    }

    /**
     * Get all the files in the root directory and its sub-directories. Replace
     * any file separators with '/' for uniformity. All paths will be relative
     * to the path rootDir
     *
     * @param root directory the directory from where files should be listed
     * @param path to the file relative to the rootDir
     * @return a List of all files in the rootDir and its sub-directories
     */
    public static List<String> getFiles(String rootDir, String path) {
        List<String> files = new ArrayList<>();
        File[] dirFiles = new File(path).listFiles();
        if (null != dirFiles && dirFiles.length > 0) {
            for (File f : dirFiles) {
                if (f.isFile()) {
                    log.info("The path to the FILE is: [" + f.getPath() + "]");
                    files.add(f.getPath().substring(rootDir.length() + 1).replace('\\', '/'));
                } else if (f.isDirectory()) {
                    log.info("The path to the DIRECTORY is: [" + f.getPath() + "]");
                    files.addAll(getFiles(rootDir, f.getPath()));
                }
            }
        }
        return files;
    }

    /**
     * Get all the files in the directory specified with the path and
     * sub-directories to this. Replace any file separators with '/' for
     * uniformity. All paths will be set relative to the rootDir
     *
     * @param listedFiles a List with paths to append new file-paths to
     * @param rootDir the directory (and eventual sub-directories) where to get
     * file-paths from
     * @param path to the file relative to the root directory
     * @return a List with paths to files
     */
    public static List<String> addFiles(List<String> listedFiles, String rootDir, String path) {
        File[] dirFiles = new File(path).listFiles();
        if (dirFiles != null && dirFiles.length > 0) {
            for (File f : dirFiles) {
                if (f.isFile()) {
                    log.info("The path to the FILE is: [" + f.getPath() + "]");
                    listedFiles.add(f.getPath().substring(rootDir.length() + 1).replace('\\', '/'));
                } else if (f.isDirectory()) {
                    log.info("The path to the DIRECTORY is: [" + f.getPath() + "]");
                    listedFiles.addAll(getFiles(rootDir, f.getPath()));
                }
            }
        }
        return listedFiles;
    }

    /**
     * If there are more than one file in the rootDir only add the model info
     * file. Replace any file separators with '/' for uniformity. All paths will
     * be set relative to the rootDir
     *
     * @param rootDir the directory where the file is located
     * @param path to the file relative to the rootDir
     * @return a List with paths to files
     */
    public static List<String> addModelFile(String rootDir, String path) {
        List<String> files = new ArrayList<>();
        File[] dirFiles = new File(path).listFiles();
        if (dirFiles != null && dirFiles.length > 0) {
            for (File f : dirFiles) {
                if (f.isFile() && f.getName().equalsIgnoreCase("model.xml")) {
                    log.info("There are more files in the models directory, but only the [" + f.getPath() + "] is taken care of.");
                    files.add(f.getPath().substring(rootDir.length() + 1).replace('\\', '/'));
                }
            }
        }
        return files;
    }

    /**
     * This method adds String[] objects built up by the local file system
     * source path and the destination location in zip file, to the List of
     * String[] objects in this Class.
     *
     * @param src the local file system source path
     * @param dst destination location in zip
     */
    public static void map(String src, String dst) {
        FileMappings.getFileMappingPairs().add(new String[]{src.replace('/', File.separatorChar), dst});
    }

    /**
     * This method converts FileMapping objects in a List to String[] objects
     * built up by the source path and the destination location in zip file and
     * adds these objects to the List of String[] objects in this Class.
     *
     * @param fileMappings a List with FileMapping objects
     */
    public static void map(List<FileMapping> fileMappings) {
        if (fileMappings != null && fileMappings.size() > 0) {
            for (FileMapping fm : fileMappings) {
                FileMappings.getFileMappingPairs().add(new String[]{fm.getSrc(), fm.getDst()});
            }
        }
    }

    /**
     * Getter method for the created list of String[]. This contains an array of
     * String objects of the form local file system source path and destination
     * location in zip.
     *
     * @return a list of file mappings as a list of String[]
     */
    public static List<String[]> getFileMappingPairs() {
        return FileMappings.getFileMappingPairs();
    }

    /**
     * Compare a name against all filenames in a directory, and return the name.
     * If more than one filename matches the name or if no filename matches,
     * return null.
     *
     * @param name a String that should be compared to all filenames in a
     * directory
     * @param files a List of filenames in a directory
     * @return the name of the file or null if more, or less, than one match.
     */
    public static String matchup(String name, List<String> files) {
        List<String> matches = new ArrayList<>();
        if (files.isEmpty()) {
            return null;
        }
        name = name.replace('\\', '/');
        for (String filename : files) {
            if (name.equalsIgnoreCase(filename)) {
                matches.add(filename);
            }
        }
        if (matches.size() > 1) {
            log.error("More than one file matches " + name);
            for (String s : matches) {
                log.debug("Matches to " + s);
            }
        } else if (matches.size() == 1) {
            return matches.get(0); // Return first and only match
        }

        // No direct matches. Try for just name
        for (String filename : files) {
            int lastPathSep = filename.indexOf('/');
            if (-1 == lastPathSep) {
                continue; // No path sep. Would have matched previously
            }
            if (name.equalsIgnoreCase(filename.substring(lastPathSep + 1))) {
                matches.add(filename);
            }
        }
        if (matches.size() > 1) {
            log.error("More than one file matches " + name);
            for (String s : matches) {
                log.debug("Matches to " + s);
            }
        } else if (matches.size() == 1) {
            return matches.get(0); // Return first and only match
        } else {
            log.error("No match found for " + name);
        }
        return null;
    }

//    public static boolean appendFileContents(String filename, StringBuffer buffer) {
//        boolean ok = true;
//        StringBuffer tmp = null;
//        BufferedReader br = null;
//        try {
//            tmp = new StringBuffer();
//            br = new BufferedReader(new FileReader(filename));
//            String line;
//            while ((line = br.readLine()) != null) {
//                tmp.append(line).append('\n');
//            }
//        } catch (IOException ioe) {
//            ok = false;
//        } finally {
//            try {
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException ignore) {
//            }
//        }
//        if (ok) {
//            buffer.append(tmp);
//        }
//        return ok;
//    }

}
