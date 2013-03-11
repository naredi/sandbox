package se.xalan.domain;

import java.io.File;
import se.xalan.utils.StringUtil;

/**
 * A holder of source and destination path references to a file.
 */
public class FileMapping {

    private final String src;
    private final String dst;

    public FileMapping(String src, String dst) {
        this.src = src.replace('/', File.separatorChar);
        this.dst = dst;
    }

    public FileMapping(String[] src, String dst) {
        this(StringUtil.buildPath(src), dst);
    }

    public String getSrc() {
        return src;
    }

    public String getDst() {
        return dst;
    }
}
