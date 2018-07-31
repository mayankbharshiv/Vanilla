package com.vanilla.afour.afour.autolib.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by siddharth.p on 12/21/2016.
 */
public class Log4J implements Reporting {
    public static Logger logger;

    public Log4J() {
      setLogger();
    }

    @Override
    public void log(String stepDescription, LogLevel logLevel) {
        switch (logLevel) {
            case INFO:
                logger.info(stepDescription);
                break;
            case ERROR:
                logger.error(stepDescription);
                break;
            case DEBUG:
                logger.debug(stepDescription);
                break;
            case TRACE:
                logger.trace(stepDescription);
                break;
            case WARN:
                logger.warn(stepDescription);
                break;
            case FATAL:
                logger.fatal(stepDescription);
                break;
            default:
                logger.info(stepDescription);
                // logger.info(message, t);
                break;
        }
    }

	public Logger getLogger() {
		return logger;
	}

	public void setLogger() {
		  logger = Logger.getLogger(Log4J.class);
	        if (logFile.exists()) {
	            logFile.delete();
	        }
	        
	                
	            try {
	                    Properties logProperties = new Properties();
	                    // load log4j properties configuration file
	                    logProperties.load(new FileInputStream("log4j.properties"));
	                    PropertyConfigurator.configure(logProperties);
	                    logger.info("Logging initialized.");
	                } catch (IOException e) {
	                    logger.error("Unable to load logging property :", e);
	                }
	            Log4J.logger = logger;
	}
}
