//*****************************************************************
//*  Copyright (c) 2008 - 2009 Smith Micro Software, Inc.
//*  All rights reserved. Proprietary and Confidential.
//*
//*  $Id: //depot/devices/dmPackageBuilder/src/com/smithmicro/dmPackageBuilder/utils/Logger.java#1 $
//*
//*****************************************************************
package se.xalan.utils;

public class Logger {
    private final org.apache.log4j.Logger logger;

    public Logger(Class<?> c) {
        logger = org.apache.log4j.Logger.getLogger(c);
    }

    public void info(String arg) {
        logger.info(arg);
    }

    public void warn(String arg) {
        logger.warn(arg);
    }

    public void error(String arg) {
        logger.error(arg);
    }

    public void debug(String arg) {
        logger.debug(arg);
    }

}
