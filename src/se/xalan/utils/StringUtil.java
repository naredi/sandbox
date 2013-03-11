//*****************************************************************
//*  Copyright (c) 2009 Smith Micro Software, Inc.
//*  All rights reserved. Proprietary and Confidential.
//*
//*  $Id: $
//*
//*****************************************************************
package se.xalan.utils;

import java.io.File;

public class StringUtil {

    public static boolean isSpecified(String arg) {
        return arg != null && arg.trim().length() > 0;
    }

    /**
     * Build a path by concatenating together the elements of a path with the
     * File.separator character
     *
     * @param elements the elements of the path to concatenate together
     * @return the concatenated String
     */
    public static String buildPath(String[] elements) {
        StringBuilder sb = new StringBuilder();
        if (elements != null && elements.length > 0) {
            for (String s : elements) {
                sb.append(s).append(File.separatorChar);
            }
            sb.setLength(sb.length() - 1); // Drop the last separator char
        }
        return sb.toString();
    }
}
