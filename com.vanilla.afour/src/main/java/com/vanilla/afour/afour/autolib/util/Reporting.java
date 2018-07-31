package com.vanilla.afour.afour.autolib.util;

import java.io.File;

/**
 * Created by siddharth.p on 12/21/2016.
 */
public interface Reporting {

    public static String currentDir = System.getProperty("user.dir");
    public static File logFile = new File(currentDir + "/AutomationTestNG.log");;

    void log(String stepDescription, LogLevel logLevel);

}
