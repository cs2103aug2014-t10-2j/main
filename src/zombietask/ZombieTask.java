package zombietask;

import interpreter.Command;
import interpreter.CommandAdd;
import interpreter.CommandDelete;
import interpreter.CommandUpdate;
import interpreter.CommandView;
import interpreter.Interpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.Calendar;

import storage.StorageAPI;
import task.Task;
import ui.FORMAT;
import ui.UI;

import java.util.logging.Level;
import java.util.logging.Logger;

import logger.ZombieLogger;

/**
 * 
 * Whoa hold on there, please wait till this is up first before making
 * any comments on how good or bad this project is.
 * 
 * @author ngsp94
 * @author ljiy369
 * @author nil
 * @author jellymac
 * 
 * @version 0.0.0a
 * 
 */

public class ZombieTask {

	/*
	 * Instantiation of External Interfaces and Static Classes
	 */
	
	private static StorageAPI storage = new StorageAPI();
	private static Scanner sc = new Scanner(System.in);
	private static Logger logger = ZombieLogger.getLogger();
	
	/*
	 * Instantiation of ZombieTask data attributes
	 */
	
	private static String currentCommandString = null;
	private static Command currentCommand = null;
	
	
	/*
	 * Trick to obtain last day of month/year 
	 * http://stackoverflow.com/questions/19488658/get-last-day-of-month
	 */
	
	/*
	 * Standard Messages
	 */
	
	private final static String MESSAGE_WELCOME = "Welcome to Zombie Task!";
	private final static String MESSAGE_FILE_OPENED = "%s is ready for use";
	private final static String MESSAGE_MISSING_ARGUMENTS = "Command Missing Arguments:\n%s";
	private final static String MESSAGE_INVALID_FILENAME = "Invalid FileName: %s";
	private final static boolean SUCCESS = true;
	private final static boolean FAILURE = false;
	
	private static boolean setExit = false;
	
	/**
	 * Method that will be invoked when ZombieTask is called.
	 * 
	 * @param args
	 * @throws Exception 
	 */
	
	public static void main(String[] args) throws Exception {
		
		showToUser(MESSAGE_WELCOME);
		initStorage(args);
		
		while(sc.hasNext() && !setExit){
			try {
				reinitializeCurrentVariables();
				currentCommandString = sc.nextLine();
				currentCommand = Interpreter.getCommand(currentCommandString);
				if (currentCommand.hasMissingArgs()){
					logger.log(Level.INFO, String.format(MESSAGE_MISSING_ARGUMENTS, currentCommandString));
					continue;
				}
				logger.log(Level.FINER, currentCommandString);
				ZombieTaskCommandHandler.execute(currentCommand, currentCommandString);
			} catch (Exception err){
				err.printStackTrace();
				showToUser(err.toString());
			}
		}
	}

	

	



	/**
	 * Initialises storage with first args
	 * 
	 * @param args optional first element of args[] will be set as file accessed
	 */
	
	public static void initStorage(String[] args) {
		try {
			if (args.length > 0){
				storage.setFile(args[0]);
			}
			storage.createFile();
		} catch (Exception err){
			showToUser(String.format(MESSAGE_INVALID_FILENAME, args[0]));
		} finally {
			ZombieTaskCommandHandler.setStorage(storage);
			showToUser(String.format(MESSAGE_FILE_OPENED, storage.getFileName()));
		}	
	}
	
	
	
	
	
	/*
	 * Stubs for Storage
	 */
	
	public static StorageAPI getStorage(){
		return storage;
	}
	
	static boolean setStorage(StorageAPI newStorage){
		storage = newStorage;
		return SUCCESS;
	}
	
	/*
	 * Stubs for currentCommand
	 */
	
	public static Command getCurrentCommand() {
		return currentCommand;
	}

	public static void setCurrentCommand(Command currentCommand) {
		ZombieTask.currentCommand = currentCommand;
	}
	
	/*
	 * Miscellaneous Helper Functions
	 */
	private static void reinitializeCurrentVariables() {
		currentCommandString = null;
		currentCommand = null;
	}
	
	
	
	/**
	 * 
	 * Stub for logger
	 * 
	 * Use this for logging. Do not use ZombieLogger.getLogger();
	 * 
	 * @return
	 */
	
	public static Logger getLogger(){
		return logger;
	}
	
	/**
	 * Stub for logger. Only for ZombieTask debug.
	 * 
	 * @param newLogger
	 */
	
	public static void setLogger(Logger newLogger){
		logger = newLogger;
	}
	
	/**
	 * Displays formatted string to users, separate from UI interface
	 * 
	 * @param displayString String to be displayed
	 */
	
	private static void showToUser(String displayString) {
		System.out.println(displayString);
		//UI.printResponse(displayString);
	}
	
	public static void exitProgram(){
		System.exit(0);
	}

}
