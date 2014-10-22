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
	
	/**
	 * Method that will be invoked when ZombieTask is called.
	 * 
	 * @param args
	 * @throws Exception 
	 */
	
	public static void main(String[] args) throws Exception {
		
		showToUser(MESSAGE_WELCOME);
		initStorage(args);
		
		while(sc.hasNext()){
			try {
				reinitializeCurrentVariables();
				currentCommandString = sc.nextLine();
				currentCommand = Interpreter.getCommand(currentCommandString);
				if(currentCommand.hasMissingArgs()){
					logger.log(Level.INFO, String.format(MESSAGE_MISSING_ARGUMENTS, currentCommandString));
					continue;
				}
				ZombieTaskCommandHandler.execute(currentCommand, currentCommandString);
			} catch (Exception err){
				showToUser(err.getMessage());
			}

		}
	}

	

	
	
<<<<<<< HEAD
=======
	private static void addCommand(Command command) {
		
		try{
			//Get Details from Command Object
			CommandAdd currentAddCommand = (CommandAdd) command;
			String taskName = currentAddCommand.getTaskName();
			Calendar taskTime = currentAddCommand.getDateTime();
			ArrayList<String> tags = currentAddCommand.getTags();
			
			//Create Task
			currentTask = null;
			if (taskTime != null){
				currentTask = new Task(taskName, taskTime);
			}else{
				currentTask = new Task(taskName);
			}
			
			//Add Tags
			for (String tag : tags){
				currentTask.addTag(tag);
			}
			
			//Store Task
			storage.add(currentTask);
			recordCommand();
			showToUser(String.format(MESSAGE_ADD, currentTask.getTaskName()));
			
		} catch (Exception err){
			showToUser(err.getMessage());
		}
	}

	private static void deleteCommand(Command command) {
		
		try{
			//Get details from Command Object
			CommandDelete currentDeleteCommand = (CommandDelete) command;
			int lineNumber = currentDeleteCommand.getLineNo();
			ArrayList<Task> allTasks = storage.getAllTasks();
			
			if (lineNumber < allTasks.size() && lineNumber >= 0){
				currentTask = allTasks.remove(lineNumber);
				storage.delete(currentTask);
				recordCommand();
				showToUser(String.format(MESSAGE_DELETE, currentTask.getTaskName()));
			}else{
				showToUser(String.format(MESSAGE_OUTOFBOUNDS, lineNumber));
				throw new IndexOutOfBoundsException("Index is too large or small : " + lineNumber);
			}
			
		} catch (Exception err){
			showToUser(err.getMessage());
			err.printStackTrace();
		}
		
	}

	private static void viewCommand(Command command) {
		try{
			CommandView currentViewCommand = (CommandView) command;
			FORMAT viewFormat = currentViewCommand.getViewType();
			ArrayList<Task> allTasks = storage.getAllTasks();
			
			switch (viewFormat){
			case AGENDA:
				UI.printPerspective(viewFormat, allTasks);
				break;
			case DAILY:
				Calendar startDay = new GregorianCalendar();
				setMinimumCalendarField(startDay, Calendar.HOUR_OF_DAY);
				setMinimumCalendarField(startDay, Calendar.MINUTE);
				setMinimumCalendarField(startDay, Calendar.SECOND);
				Calendar endDay = new GregorianCalendar();
				setMaximumCalendarField(endDay, Calendar.HOUR_OF_DAY);
				setMaximumCalendarField(endDay, Calendar.MINUTE);
				setMaximumCalendarField(endDay, Calendar.SECOND);
				UI.printPerspective(viewFormat, storage.search(startDay, endDay));
				break;
			case WEEKLY:
				Calendar startWeek = new GregorianCalendar();
				setMinimumCalendarField(startWeek, Calendar.DAY_OF_WEEK);
				setMinimumCalendarField(startWeek, Calendar.HOUR_OF_DAY);
				setMinimumCalendarField(startWeek, Calendar.MINUTE);
				setMinimumCalendarField(startWeek, Calendar.SECOND);
				Calendar endWeek = new GregorianCalendar();
				setMaximumCalendarField(endWeek, Calendar.DAY_OF_WEEK);
				setMaximumCalendarField(endWeek, Calendar.HOUR_OF_DAY);
				setMaximumCalendarField(endWeek, Calendar.MINUTE);
				setMaximumCalendarField(endWeek, Calendar.SECOND);
				UI.printPerspective(viewFormat, storage.search(startWeek, endWeek));
				break;
			case MONTHLY:
				Calendar startMonth = new GregorianCalendar();
				setMinimumCalendarField(startMonth, Calendar.DAY_OF_MONTH);
				setMinimumCalendarField(startMonth, Calendar.HOUR_OF_DAY);
				setMinimumCalendarField(startMonth, Calendar.MINUTE);
				setMinimumCalendarField(startMonth, Calendar.SECOND);
				Calendar endMonth = new GregorianCalendar();
				setMaximumCalendarField(endMonth, Calendar.DAY_OF_MONTH);
				setMaximumCalendarField(endMonth, Calendar.HOUR_OF_DAY);
				setMaximumCalendarField(endMonth, Calendar.MINUTE);
				setMaximumCalendarField(endMonth, Calendar.SECOND);
				UI.printPerspective(viewFormat, storage.search(startMonth, endMonth));
				break;
			case ANNUAL:
				Calendar startYear = new GregorianCalendar();
				setMinimumCalendarField(startYear, Calendar.DAY_OF_YEAR);
				setMinimumCalendarField(startYear, Calendar.HOUR_OF_DAY);
				setMinimumCalendarField(startYear, Calendar.MINUTE);
				setMinimumCalendarField(startYear, Calendar.SECOND);
				Calendar endYear = new GregorianCalendar();
				setMaximumCalendarField(endYear, Calendar.DAY_OF_YEAR);
				setMaximumCalendarField(endYear, Calendar.HOUR_OF_DAY);
				setMaximumCalendarField(endYear, Calendar.MINUTE);
				setMaximumCalendarField(endYear, Calendar.SECOND);
				UI.printPerspective(viewFormat, storage.search(startYear, endYear));
				break;
			case CALENDAR:
				Calendar startCalendar = null;
				Calendar endCalendar = null;
				UI.printPerspective(viewFormat, storage.search(startCalendar, endCalendar));
				break;
			default:
			case INVALID:
				showToUser(String.format(COMMAND_INVALID, currentViewCommand.getViewType()));
				break;
			}
		} catch (Exception err){
			showToUser(err.getMessage());
			err.printStackTrace();
		}
	}


	private static void updateCommand(Command command) throws Exception {
		
		CommandUpdate currentUpdateCommand = (CommandUpdate) command;
		
		// Create New Task
		
		CommandAdd currentAddCommand = currentUpdateCommand.getUpdatedTask();
		String taskName = currentAddCommand.getTaskName();
		Calendar taskTime = currentAddCommand.getDateTime();
		ArrayList<String> tags = currentAddCommand.getTags();
		
		currentTask = null;
		if (taskTime != null){
			currentTask = new Task(taskName, taskTime);
		}else{
			currentTask = new Task(taskName);
		}
		
		//Add Tags
		for (String tag : tags){
			currentTask.addTag(tag);
		}
		
		//Get old task
		Task oldTask = storage.search(currentUpdateCommand.getLineNo());
		
		//delete old task
		try {
			storage.delete(oldTask);
			storage.add(currentTask);
		} catch (Exception err){
			showToUser(err.getMessage());
			err.printStackTrace();
		}
		
		
		
	}

	private static void invalidCommand(String commandString) {
		showToUser(String.format(MESSAGE_INVALID_COMMAND, commandString));
	}

	private static void undo() {
		try{
			// Pop items from pastLists
			if (pastCommandList.size() == 0){
				throw new Exception(ERROR_EMPTY_UNDO_STACK);
			}
			currentCommandDescriptor = pastCommandDescriptorList.remove(pastCommandDescriptorList.size() - 1);
			currentCommand = pastCommandList.remove(pastCommandList.size() - 1);
			currentTask = pastTaskList.remove(pastTaskList.size() - 1);
			
			// Execute undo
			
			switch (currentCommandDescriptor){
			case COMMAND_ADD:
				currentTask = storage.delete(currentTask);
				break;
			case COMMAND_DELETE:
				storage.add(currentTask);
				break;
			default:
			case COMMAND_INVALID:
				throw new Exception(ERROR_INVALID_UNDO_REDO);
			}
			
			// Push items into futureLists
			futureCommandDescriptorList.add(currentCommandDescriptor);
			futureCommandList.add(currentCommand);
			futureTaskList.add(currentTask);
			
			
		} catch (Exception err) {
			showToUser(err.getMessage());
			err.printStackTrace();
		}
	}
	
	private static void redo() {
		try{
			//Pop items from futureLists
			if (pastCommandList.size() == 0){
				throw new Exception(ERROR_EMPTY_REDO_STACK);
			}
			currentCommandDescriptor = futureCommandDescriptorList.remove(futureCommandDescriptorList.size() - 1);
			currentCommand = futureCommandList.remove(futureCommandList.size() - 1);
			currentTask = futureTaskList.remove(futureTaskList.size() - 1);
			
			
			// Execute redo
			
			switch (currentCommandDescriptor){
			case COMMAND_ADD:
				currentTask = storage.delete(currentTask);
				break;
			case COMMAND_DELETE:
				storage.add(currentTask);
				break;
			default:
			case COMMAND_INVALID:
				throw new Exception(ERROR_INVALID_UNDO_REDO);
			}
			
			// Push items into pastLists
			
			pastCommandDescriptorList.add(currentCommandDescriptor);
			pastCommandList.add(currentCommand);
			pastTaskList.add(currentTask);
			
		} catch (Exception err) {
			showToUser(err.getMessage());
			err.printStackTrace();
		}
		
	}
	
	private static void help() {
		showToUser(MESSAGE_HELP);
	}
>>>>>>> refs/heads/UI


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
		UI.printResponse(displayString);
	}

}
