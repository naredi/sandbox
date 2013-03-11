package se.xalan.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of file mappings of the form - local file system source path -
 * destination location in zip
 */
public class FileMappings {

    private static final List<String[]> fileMappingPairs = new ArrayList<>();

    public static List<String[]> getFileMappingPairs() {
        return fileMappingPairs;
    }

    public static void clear() {
        fileMappingPairs.clear();
    }
}
