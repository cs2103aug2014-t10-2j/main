package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logging module for ZombieTask
 * 
 * Refer to http://tutorials.jenkov.com/java-logging/handlers.html
 * 
 * @author jellymac
 *
 */

public class ZombieLogger {
	
	/*
	 * Debug Messages
	 */
	
	private static final String DEBUG_SECURITY = "LOG FILE CANNOT BE ACCESSED - SECURITY";
	private static final String DEBUG_IO_FILE = "LOG FILE CANNOT BE OPENED - I/O";
	
	/*
	 * Logger Contants
	 */
	
	private static final int MAX_FILES = 20;
	private static final int MAX_LOGS = 1024*1024*4;
	private static final String DEFAULT_FILENAME = "ZombieLog.%u.%g.txt";
	private static final Level DEFAULT_LEVEL = Level.ALL;
	/*
	 * Class variable
	 */
	
	private static Logger logger;
	private static String FILENAME = DEFAULT_FILENAME;
	private static Level level = DEFAULT_LEVEL;
	
	/**
	 * Initializes logger
	 */
	
	public static void initLogger(){
		logger = Logger.getGlobal();
		setLevel(level);
		setFileHandler();
	}
	
	/**
	 * 
	 * Returns logger object for logging
	 * 
	 * 
	 * 
	 * @return logger object
	 */

	public static Logger getLogger(){
		return logger;
	}
	
	/**
	 * Sets filename for logger
	 * 
	 * @param fileName
	 */
	
	public static void setFileName(String fileName){
		FILENAME = fileName;
		setFileHandler();
	}
	
	/**
	 * Determines verbosity/ Sets logging level for ZombieLogger
	 * 
	 * Accepts Level.*
	 * 
	 * @param newLevel
	 */
	
	public static void setLevel(Level newLevel){
		level = newLevel;
		logger.setLevel(level);
	}

	/**
	 * 
	 * Sets file handler for logger class
	 * 
	 * @param logger
	 * 
	 */
	
	private static void setFileHandler(){
		FileHandler fh;
		try {
			fh = new FileHandler(FILENAME, MAX_LOGS, MAX_FILES);
			logger.addHandler(fh);
			fh.setFormatter(new SimpleFormatter());
		}catch (SecurityException e) {
			logger.log(Level.SEVERE, DEBUG_SECURITY, e);
		}catch (IOException e){
			logger.log(Level.SEVERE, DEBUG_IO_FILE, e);
		}
	}

}
