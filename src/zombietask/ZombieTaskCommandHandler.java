package zombietask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import storage.Storage;
import storage.StorageAPI;
import task.Task;
import task.TaskUIFormat;
import ui.FORMAT;
import ui.UI;
import interpreter.Command;
import interpreter.CommandAdd;
import interpreter.CommandDelete;
import interpreter.CommandSearchName;
import interpreter.CommandSearchTime;
import interpreter.CommandUpdate;
import interpreter.CommandView;

public class ZombieTaskCommandHandler {
	
	/*
	 * Class constants
	 */
	
	private final static String COMMAND_ADD = Command.ADD; //edit out
	private final static String COMMAND_DELETE = Command.DELETE;
	private final static String COMMAND_UPDATE = Command.UPDATE;
	private final static String COMMAND_VIEW = Command.VIEW;
	private final static String COMMAND_UNDO = Command.UNDO;
	private final static String COMMAND_REDO = Command.REDO;
	private final static String COMMAND_HELP = Command.HELP;
	private final static String COMMAND_SEARCH_NAME = Command.SEARCH_NAME;
	private final static String COMMAND_SEARCH_TIME = Command.SEARCH_TIME;
	//private final static String COMMAND_SEARCH_TAG = Command.SEARCH_TAG;
	private final static String COMMAND_EXIT = Command.EXIT;
	private final static String COMMAND_INVALID = "invalid command %s";
	
	private final static boolean SUCCESS = true;
	private final static boolean FAILURE = false;
	
	
	/*
	 * Messages
	 */
	
	private final static String MESSAGE_INVALID_COMMAND = "Invalid Command:\n%s";
	private final static String MESSAGE_HELP = "THIS HELP NEEDS A BIT OF HELP!";
	private final static String MESSAGE_ADD = "Added %s to database";
	private final static String MESSAGE_DELETE = "Deleted %s from database";
	private final static String MESSAGE_OUTOFBOUNDS = "Warning: input %s is out of bounds,";
	
	private final static String ERROR_EMPTY_UNDO_STACK = "There is nothing to undo!";
	private final static String ERROR_EMPTY_REDO_STACK = "There is nothing to redo!";
	private final static String ERROR_INVALID_UNDO_REDO = "Invalid command on undo stack";
	
	
	/*
	 * Class variables
	 */
	
	private static Logger logger = ZombieTask.getLogger();
	
	private static String currentCommandDescriptor = null;
	private static String currentCommandString = null;
	private static Command currentCommand = null;
	private static Task currentTask = null;
	
	private static ArrayList<String> futureCommandDescriptorList = new ArrayList<String>();
	private static ArrayList<Command> futureCommandList = new ArrayList<Command>();
	private static ArrayList<Task> futureTaskList = new ArrayList<Task>();
	private static ArrayList<String> pastCommandDescriptorList = new ArrayList<String>();
	private static ArrayList<Command> pastCommandList = new ArrayList<Command>();
	private static ArrayList<Task> pastTaskList = new ArrayList<Task>();
	
	private static StorageAPI storage;
	
	/*
	 * Command Handlers
	 */
	
	public static void execute(Command newCommand, String newCommandString) throws Exception {
		reinitializeCurrentVariables();
		currentCommand = newCommand;
		currentCommandString = newCommandString;
		execute();
	}
	
	/**
	 * Executes Command
	 * 
	 * @throws Exception
	 */
	
	public static void execute() throws Exception {
		currentCommandDescriptor = currentCommand.getCommandType();
		switch(currentCommandDescriptor){
		case COMMAND_ADD:
			addCommand(currentCommand);
			break;
		case COMMAND_DELETE:
			deleteCommand(currentCommand);
			break;
		case COMMAND_VIEW:
			viewCommand(currentCommand);
			break;
		case COMMAND_UPDATE:
			updateCommand(currentCommand);
			break;
		case COMMAND_UNDO:
			undo();
			break;
		case COMMAND_REDO:
			redo();
			break;
		case COMMAND_HELP:
			help();
			break;
		case COMMAND_SEARCH_NAME:
			searchName(currentCommand);
			break;
		case COMMAND_SEARCH_TIME:
			searchTime(currentCommand);
		case COMMAND_EXIT:
			exit();
			break;
		default:
		case COMMAND_INVALID:
			invalidCommand(currentCommandString);
			break;
		}
	}
	
	private static void reinitializeCurrentVariables() {
		currentCommandDescriptor = null;
		currentCommandString = null;
		currentCommand = null;
		currentTask = null;
	}
	
	protected static void addCommand(Command command) {
		
		try{
			//Get Details from Command Object
			CommandAdd currentAddCommand = (CommandAdd) command;
			String taskName = currentAddCommand.getTaskName();
			Calendar startTime = currentAddCommand.getStartDate();
			Calendar endTime = currentAddCommand.getEndDate();
			ArrayList<String> tags = currentAddCommand.getTags();
			
			//Create Task
			currentTask = null;
			if (startTime == null && endTime == null){
				currentTask = new Task(taskName);
			}else if(startTime == null){
				currentTask = new Task(taskName, endTime);
			}else{
				currentTask = new Task(taskName, endTime, startTime);
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
			err.printStackTrace();
			showToUser(err.getMessage());
		}
	}

	protected static void deleteCommand(Command command) {
		// TODO Auto-generated method stub
		
		try{
			//Get details from Command Object
			CommandDelete currentDeleteCommand = (CommandDelete) command;
			String lineCode = currentDeleteCommand.getLineCode();
			currentTask = storage.search(lineCode);
			storage.delete(currentTask);
			recordCommand();
			showToUser(String.format(MESSAGE_DELETE, currentTask.getTaskName()));
			/*
			 * ArrayList<Task> allTasks = storage.getAllTasks();
			 * 
			if (lineNumber < allTasks.size() && lineNumber >= 0){
				currentTask = allTasks.remove(lineNumber);
				storage.delete(currentTask);
				recordCommand();
				showToUser(String.format(MESSAGE_DELETE, currentTask.getTaskName()));
			}else{
				showToUser(String.format(MESSAGE_OUTOFBOUNDS, lineNumber));
				throw new IndexOutOfBoundsException("Index is too large or small : " + lineNumber);
			}
			*/
			
		} catch (Exception err){
			showToUser(err.getMessage());
			err.printStackTrace();
		}
		
	}

	protected static void viewCommand(Command command) {
		// TODO Auto-generated method stub
		try{
			CommandView currentViewCommand = (CommandView) command;
			FORMAT viewFormat = currentViewCommand.getViewType();
			TaskUIFormat allTasks = storage.getAllTasks();
			
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


	protected static void updateCommand(Command command) throws Exception {
		// TODO Auto-generated method stub
		
		CommandUpdate currentUpdateCommand = (CommandUpdate) command;
		
		// Create New Task
		
		CommandAdd currentAddCommand = currentUpdateCommand.getUpdatedTask();
		String taskName = currentAddCommand.getTaskName();
		Calendar taskTime = currentAddCommand.getStartDate();
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
		/*
		 * Note old method depriciated.
		 */
		//Task oldTask = storage.search(currentUpdateCommand.getLineCode());
		Task oldTask = null;
		
		//delete old task
		try {
			storage.delete(oldTask);
			storage.add(currentTask);
		} catch (Exception err){
			showToUser(err.getMessage());
			err.printStackTrace();
		}
		
		
		
	}

	protected static void invalidCommand(String commandString) {
		showToUser(String.format(MESSAGE_INVALID_COMMAND, commandString));
	}

	protected static void undo() {
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
	
	protected static void redo() {
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
	
	protected static void help() {
		showToUser(MESSAGE_HELP);
	}
	
	protected static void searchName(Command command) throws Exception{
		CommandSearchName searchCommand = (CommandSearchName) command;
		storage.searchName(searchCommand.getSearchString());
	}
	
	protected static void searchTime(Command command) throws Exception{
		CommandSearchTime searchCommand = (CommandSearchTime) command;
		storage.search(searchCommand.getTimeStart(), searchCommand.getTimeEnd());
	}
	
	protected static void exit(){
		ZombieTask.exitProgram();
	}
	
	/*
	 * commandList mutators
	 */
	
	private static boolean recordCommand(){
		pastCommandDescriptorList.add(currentCommandDescriptor);
		pastCommandList.add(currentCommand);
		pastTaskList.add(currentTask);
		clearFutureLists();
		return true;
	}


	private static void clearFutureLists() {
		if (futureCommandList.size() > 0){
			futureCommandDescriptorList.clear();
			futureCommandList.clear();
			futureTaskList.clear();
		}
	}
	
	private static void clearPastLists() {
		if (pastCommandList.size() > 0){
			pastCommandDescriptorList.clear();
			pastCommandList.clear();
			pastTaskList.clear();
		}
	}
	
	/*
	 * Stub method for Commands
	 */
	
	static Command getCommand(){
		return currentCommand;
	}
	
	static boolean setCommand(Command newCommand)
	{
		currentCommand = newCommand;
		return SUCCESS;
	}
	
	/*
	 * Stub methods for CommandLists, CommandDescriptors and TaskLists
	 */
	
	static boolean setPastCommandList(ArrayList<Command> newCommandList){
		pastCommandList = newCommandList;
		return SUCCESS;
	}
	
	static ArrayList<Command> getPastCommandList(){
		return pastCommandList;
	}
	
	static boolean setFutureCommandList(ArrayList<Command> newCommandList){
		futureCommandList = newCommandList;
		return SUCCESS;
	}
	
	static ArrayList<Command> getFutureCommandList(){
		return futureCommandList;
	}
	
	static boolean setPastCommandDescriptorList(ArrayList<String> newCommandDescriptorList){
		pastCommandDescriptorList = newCommandDescriptorList;
		return SUCCESS;
	}
	
	static ArrayList<String> getPastCommandDescriptorList(){
		return pastCommandDescriptorList;
	}
	
	static boolean setFutureCommandDescriptorList(ArrayList<String> newCommandDescriptorList){
		futureCommandDescriptorList = newCommandDescriptorList;
		return SUCCESS;
	}
	
	static ArrayList<String> getFutureCommandDescriptorList(){
		return futureCommandDescriptorList;
	}
	
	static boolean setPastTaskList(ArrayList<Task> newTaskList){
		pastTaskList = newTaskList;
		return SUCCESS;
	}
	
	static ArrayList<Task> getPastTaskList(){
		return pastTaskList;
	}
	
	static boolean setFutureTaskList(ArrayList<Task> newTaskList){
		futureTaskList = newTaskList;
		return SUCCESS;
	}
	
	static ArrayList<Task> getFutureTaskList(){
		return futureTaskList;
	}
	
	static void setStorage(StorageAPI newStorage){
		storage = newStorage;
	}
	
	private static void setMinimumCalendarField(Calendar time, int calendarField) {
		time.set(calendarField, time.getActualMinimum(calendarField));
	}
	
	private static void setMaximumCalendarField(Calendar time, int calendarField) {
		time.set(calendarField, time.getActualMaximum(calendarField));
	}
	
	private static void showToUser(String displayString) {
		System.out.println(displayString);
		//UI.printResponse(displayString);
	}

}
