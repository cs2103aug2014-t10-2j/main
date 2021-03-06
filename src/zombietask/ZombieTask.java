package zombietask;

import interpreter.Command;
//import interpreter.CommandAdd;
//import interpreter.CommandDelete;
//import interpreter.CommandUpdate;
//import interpreter.CommandView;
import interpreter.Interpreter;

//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.GregorianCalendar;
//import java.util.Scanner;
//import java.util.Calendar;

import storage.StorageAPI;
//import task.Task;
//import ui.FORMAT;
import ui.GUI;
import ui.UI;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import logger.ZombieLogger;

/**
 * 
 * Whoa hold on there, please wait till this is up first before making any
 * comments on how good or bad this project is.
 * 
//@author a0066754w
 * 
 * @version 0.0.0a
 * 
 */

public class ZombieTask {

	/*
	 * Instantiation of External Interfaces and Static Classes
	 */

	private static StorageAPI storage = new StorageAPI();
	//private static Scanner sc = new Scanner(System.in);
	private static Logger logger = ZombieLogger.getLogger();

	/*
	 * Instantiation of ZombieTask data attributes
	 */

	private static String currentCommandString = null;
	private static Command currentCommand = null;
	private static GUI window = null;
	
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
	private final static String DEFAULT_FILENAME = "ZombieStorage.txt";
	private final static boolean SUCCESS = true;
	//private final static boolean FAILURE = false;
	//private static boolean setExit = false;

	/**
	 * Method that will be invoked when ZombieTask is called.
	 * 
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String[] args) throws Exception {
		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				try {
					window = new GUI();
					window.getFrmZombietask().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		ZombieTaskCommandHandler.setWindow(window);
		
		showToUser(MESSAGE_WELCOME);
		if (args.length > 2){
			initStorage(args[1]);
		}else{
			initStorage(DEFAULT_FILENAME);
		}
		UI.initUIOnce();
		userInput("view agenda");
	}
	
	
	public static void userInput(String str) {
		try {
			reinitializeCurrentVariables();
			currentCommandString = str;
			currentCommand = Interpreter.getCommand(currentCommandString);
			if (currentCommand.hasMissingArgs() && !currentCommand.getCommandType().equals(Command.HELP)) {
				logger.log(Level.INFO, String.format(
						MESSAGE_MISSING_ARGUMENTS, currentCommandString));
				//continue;
			}
			
			logger.log(Level.FINER, currentCommandString);
			ZombieTaskCommandHandler.execute(currentCommand,
					currentCommandString);
		} catch (Exception err) {
			err.printStackTrace();
			showToUser(err.toString());
		}
	}

	/**
	 * Helper method to test 1 command
	 * 
	 * @param command command to test
	 */
	public static void testCommand(String commandStr) {
		initStorage("ZombieTest");
		UI.initUIOnce();
		try {
			Command command = Interpreter.getCommand(commandStr);
			ZombieTaskCommandHandler.execute(command, commandStr);
		} catch (Exception err) {
			err.printStackTrace();
			showToUser(err.toString());
		}
	}
	
	public static GUI getGUI(){
		return window;
	}

	/**
	 * Initialises storage with first args
	 * 
	 * @param args optional first element of args[] will be set as file accessed
	 */

	public static void initStorage(String args) {
		try {
			if (args.length() > 0) {
				storage.setFile(args);
			}
			StorageAPI.createFile();
		} catch (Exception err) {
			showToUser(String.format(MESSAGE_INVALID_FILENAME, args));
		} finally {
			ZombieTaskCommandHandler.setStorage(storage);
			showToUser(String
					.format(MESSAGE_FILE_OPENED, storage.getFileName()));
		}
	}

	/*
	 * Stubs for Storage
	 */

	public static StorageAPI getStorage() {
		return storage;
	}

	static boolean setStorage(StorageAPI newStorage) {
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

	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Stub for logger. Only for ZombieTask debug.
	 * 
	 * @param newLogger
	 */

	public static void setLogger(Logger newLogger) {
		logger = newLogger;
	}

	/**
	 * Displays formatted string to users, separate from UI interface
	 * 
	 * @param displayString String to be displayed
	 */

	private static void showToUser(String displayString) {
		window.modifyUpperLabel(displayString);
	}

	public static void exitProgram() {
		window.closeWindow();
	}

}
