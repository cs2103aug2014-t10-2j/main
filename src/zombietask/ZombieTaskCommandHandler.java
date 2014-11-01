package zombietask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
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
import interpreter.CommandHelp;
import interpreter.CommandSearchName;
import interpreter.CommandSearchTime;
import interpreter.CommandUpdate;
import interpreter.CommandView;
import ext.jansi.Ansi;
import ext.jansi.AnsiConsole;
import static ext.jansi.Ansi.ansi;

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
	private final static String COMMAND_SEARCH = Command.SEARCH;
	private final static String COMMAND_SEARCH_NAME = Command.SEARCH_NAME;
	private final static String COMMAND_SEARCH_TIME = Command.SEARCH_TIME;
	//private final static String COMMAND_SEARCH_TAG = Command.SEARCH_TAG;
	private final static String COMMAND_EXIT = Command.EXIT;
	private final static String COMMAND_INVALID = "invalid command %s";
	
	private final static boolean SUCCESS = true;
	private final static boolean FAILURE = false;
	
	// for testing
	private static String commandCalled = null;
	
	/*
	 * Messages
	 */
	
	private final static String MESSAGE_INVALID_COMMAND = "Invalid Command:\n%s";
	private final static String MESSAGE_HELP_ADD = ansi()
			.fg(Ansi.Color.DEFAULT).a("Add:\n\t")
			.fg(Ansi.Color.MAGENTA).a("add different kinds of tasks to the task list including floating tasks, deadline tasks and timed tasks \n\t")
			.fg(Ansi.Color.CYAN).a("add ").fg(Ansi.Color.RED).a("taskname\n\t")
			.fg(Ansi.Color.CYAN).a("add ").fg(Ansi.Color.RED).a("taskname ").fg(Ansi.Color.GREEN).a("end time\n\t")
			.fg(Ansi.Color.CYAN).a("add ").fg(Ansi.Color.RED).a("taskname ").fg(Ansi.Color.GREEN).a("start time to end time\n").reset().toString();
	private final static String MESSAGE_HELP_DELETE = ansi()	
			.fg(Ansi.Color.DEFAULT).a("Delete:\n\t")
			.fg(Ansi.Color.MAGENTA).a("delete a task with its index \n\t")
			.fg(Ansi.Color.CYAN).a("delete ").fg(Ansi.Color.RED).a("task number\n").reset().toString();
	private final static String MESSAGE_HELP_SEARCH = ansi()
			.fg(Ansi.Color.DEFAULT).a("Search:\n\t")
			.fg(Ansi.Color.MAGENTA).a("search tasks with task name or time \n\t")
			.fg(Ansi.Color.CYAN).a("search-name ").fg(Ansi.Color.RED).a("task name \n\t")
			.fg(Ansi.Color.CYAN).a("search-time ").fg(Ansi.Color.RED).a("start time ").fg(Ansi.Color.GREEN).a("to").fg(Ansi.Color.RED).a(" end time\n").reset().toString();
	private final static String MESSAGE_HELP_UPDATE = ansi()
			.fg(Ansi.Color.DEFAULT).a("Update:\n\t")
			.fg(Ansi.Color.MAGENTA).a("update the information of the task with its index \n\t")
			.fg(Ansi.Color.CYAN).a("add ").fg(Ansi.Color.RED).a("task number ").fg(Ansi.Color.RED).a("start time ").fg(Ansi.Color.GREEN).a("to").fg(Ansi.Color.RED).a(" end time\n").reset().toString();
	private final static String MESSAGE_HELP_VIEW = ansi()
			.fg(Ansi.Color.DEFAULT).a("View:\n\t")
			.fg(Ansi.Color.MAGENTA).a("different kinds of views for tasks including agenda, daily and weekly \n\t")
			.fg(Ansi.Color.CYAN).a("view ").fg(Ansi.Color.RED).a("agenda\n\t")
			.fg(Ansi.Color.CYAN).a("view ").fg(Ansi.Color.RED).a("daily \n\t")
			.fg(Ansi.Color.CYAN).a("view ").fg(Ansi.Color.RED).a("weekly \n\t").reset().toString();
	private final static String MESSAGE_HELP_UNDO = ansi()	
			.fg(Ansi.Color.DEFAULT).a("Undo:\n\t")
			.fg(Ansi.Color.MAGENTA).a("reverse the action to the previous state \n\t")
			.fg(Ansi.Color.CYAN).a("undo ").reset().toString();
	private final static String MESSAGE_HELP_REDO = ansi()	
			.fg(Ansi.Color.DEFAULT).a("Rndo:\n\t")
			.fg(Ansi.Color.MAGENTA).a("go the the state before the undo action \n\t")
			.fg(Ansi.Color.CYAN).a("Rndo ").reset().toString();
	private final static String MESSAGE_HELP_EXIT = ansi()	
			.fg(Ansi.Color.DEFAULT).a("Exit:\n\t")
			.fg(Ansi.Color.MAGENTA).a("exit the program \n\t")
			.fg(Ansi.Color.CYAN).a("exit ").reset().toString();
	
	
	private final static String MESSAGE_ADD = "Added %s to database";
	private final static String MESSAGE_DELETE = "Deleted %s from database";
	private final static String MESSAGE_UPDATE = "Updated %s to %s from database";
	private final static String MESSAGE_OUTOFBOUNDS = "Warning: input %s is out of bounds";
	private final static String MESSAGE_CLASH_WARNING = "Warning:\n\tTasks %s and %s clashes";
	private final static String MESSAGE_CLASH_MORE_THAN_ONE = "Warning\n\tTasks %s and %d other task(s) clashes";
	
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
	private static Task oldTask = null;
	
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
			commandCalled = Command.ADD;
			addCommand(currentCommand);
			break;
		case COMMAND_DELETE:
			commandCalled = Command.DELETE;
			deleteCommand(currentCommand);
			break;
		case COMMAND_VIEW:
			commandCalled = Command.VIEW;
			viewCommand(currentCommand);
			break;
		case COMMAND_UPDATE:
			commandCalled = Command.UPDATE;
			updateCommand(currentCommand);
			break;
		case COMMAND_UNDO:
			commandCalled = Command.UNDO;
			undo();
			break;
		case COMMAND_REDO:
			commandCalled = Command.REDO;
			redo();
			break;
		case COMMAND_HELP:
			commandCalled = Command.HELP;
			help(((CommandHelp)currentCommand).getHelpCommand());
			break;
		case COMMAND_SEARCH_NAME:
			commandCalled = Command.SEARCH_NAME;
			searchName(currentCommand);
			break;
		case COMMAND_SEARCH_TIME:
			commandCalled = Command.SEARCH_TIME;
			searchTime(currentCommand);
			break;
		case COMMAND_EXIT:
			commandCalled = Command.EXIT;
			exit();
			break;
		default:
		case COMMAND_INVALID:
			commandCalled = null;
			invalidCommand(currentCommandString);
			break;
		}
	}
	
	/**
	 * For testing
	 */
	public static String getCommandCalled() {
		return commandCalled;
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
			String location = currentAddCommand.getLocation();
			ArrayList<String> tags = currentAddCommand.getTags();
			
			/* 
			 * Temp fix
			 * Start time and end time has issues. At interpreter end.
			 * 
			 * Case 1: starttime is not null
			 * 
			 * Case 2: starttime is before endtime
			 */
			
			if (startTime != null && endTime == null){
				Calendar tempTime = startTime;
				startTime = endTime;
				endTime = tempTime;
			} else if (startTime != null && endTime != null){
				if (startTime.after(endTime)){
					Calendar tempTime = startTime;
					startTime = endTime;
					endTime = tempTime;
				}
			}
			
			/*
			 * End of fix
			 */
			
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
			
			//Add location
			if (location != null){
				currentTask.setLocation(location);
			}
			
			//Store Task
			storage.add(currentTask);
			recordCommand();
			
			ArrayList<Task> clashedTasks = storage.taskClash(currentTask);
			if(clashedTasks.size() == 1){
				showToUser(String.format(MESSAGE_CLASH_WARNING, currentTask.getTaskName(), clashedTasks.get(0).getTaskName()));
			}else if (clashedTasks.size() > 1){
				showToUser(String.format(MESSAGE_CLASH_MORE_THAN_ONE, currentTask.getTaskName(), clashedTasks.size()));
			}
			showToUser(String.format(MESSAGE_ADD, currentTask.getTaskName()));
			showToUser(UI.printTask(currentTask, 1, 0));
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
		Calendar startTime = currentAddCommand.getStartDate();
		Calendar endTime = currentAddCommand.getEndDate();
		ArrayList<String> tags = currentAddCommand.getTags();
		
		/*
		 * Temp fix for SP's code
		 */
		
		if (startTime != null && endTime == null){
			Calendar tempTime = startTime;
			startTime = endTime;
			endTime = tempTime;
		} else if (startTime != null && endTime != null){
			if (startTime.after(endTime)){
				Calendar tempTime = startTime;
				startTime = endTime;
				endTime = tempTime;
			}
		}
		
		currentTask = null;
		if (startTime == null){
			currentTask = new Task(taskName);
		}else if((startTime != null) && (endTime != null)){
			currentTask = new Task(taskName,endTime,startTime);
		}else {
			currentTask = new Task(taskName,startTime);
		}
		
		//Add Tags
		for (String tag : tags){
			currentTask.addTag(tag);
		}
		
		//Get old task
		/*
		 * Note old method depriciated.
		 */
		oldTask = storage.search(currentUpdateCommand.getLineCode());
		
		//delete old task
		try {
			storage.delete(oldTask);
			storage.add(currentTask);
			recordCommand();
		} catch (Exception err){
			showToUser(err.getMessage());
			err.printStackTrace();
		}
		
		showToUser(String.format(MESSAGE_UPDATE, oldTask.getTaskName(), currentTask.getTaskName()));
		
		
	}

	protected static void invalidCommand(String commandString) {
		showToUser(String.format(MESSAGE_INVALID_COMMAND, commandString));
	}

	
	protected static void undo() {
		try{
			// Pop items from pastLists
			if (pastCommandList.size() == 0){
				logger.log(Level.INFO,ERROR_EMPTY_UNDO_STACK );
				showToUser(ERROR_EMPTY_UNDO_STACK);
				return;
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
			case COMMAND_UPDATE:
				currentTask = storage.delete(currentTask);
				storage.add(oldTask);
				break;
			default:
			case COMMAND_INVALID:
				logger.log(Level.INFO, ERROR_INVALID_UNDO_REDO);
				showToUser(ERROR_INVALID_UNDO_REDO);
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
			if (futureCommandDescriptorList.size() == 0){
				logger.log(Level.INFO,ERROR_EMPTY_REDO_STACK );
				showToUser(ERROR_EMPTY_REDO_STACK);
				return;
			}
			currentCommandDescriptor = futureCommandDescriptorList.remove(futureCommandDescriptorList.size() - 1);
			currentCommand = futureCommandList.remove(futureCommandList.size() - 1);
			currentTask = futureTaskList.remove(futureTaskList.size() - 1);
			
			
			// Execute redo
			
			switch (currentCommandDescriptor){
			case COMMAND_ADD:
				storage.add(currentTask);
				break;
			case COMMAND_DELETE:
				storage.delete(currentTask);
				break;
			case COMMAND_UPDATE:
				storage.add(currentTask);
				storage.delete(oldTask);
				break;
				
			default:
			case COMMAND_INVALID:
				logger.log(Level.INFO, ERROR_INVALID_UNDO_REDO);
				showToUser(ERROR_INVALID_UNDO_REDO);
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
	
	protected static void help(String userInput) {
		
		if(userInput == null){
			showToUser(MESSAGE_HELP_ADD + "\n" + MESSAGE_HELP_DELETE + "\n" + MESSAGE_HELP_UPDATE + "\n" + MESSAGE_HELP_SEARCH + "\n" + MESSAGE_HELP_VIEW+ "\n" + MESSAGE_HELP_UNDO+ "\n" + MESSAGE_HELP_REDO+ "\n" + MESSAGE_HELP_EXIT);
			return;
		}
		switch(userInput){
		case COMMAND_ADD:
			showToUser(MESSAGE_HELP_ADD);
			break;
		case COMMAND_DELETE:
			showToUser(MESSAGE_HELP_DELETE);
			break;
		case COMMAND_UPDATE:
			showToUser(MESSAGE_HELP_UPDATE);
			break;
		case COMMAND_SEARCH:
			showToUser(MESSAGE_HELP_SEARCH);
			break;
		case COMMAND_VIEW:
			showToUser(MESSAGE_HELP_VIEW);
			break;
		case COMMAND_UNDO:
			showToUser(MESSAGE_HELP_UNDO);
			break;
		case COMMAND_REDO:
			showToUser(MESSAGE_HELP_REDO);
			break;
		case COMMAND_EXIT:
			showToUser(MESSAGE_HELP_EXIT);
			break;	
		default:
			logger.log(Level.INFO,String.format(MESSAGE_INVALID_COMMAND, userInput) );
			showToUser(String.format(MESSAGE_INVALID_COMMAND, userInput));
			return;
			
		}
	}
	
	protected static void searchName(Command command) throws Exception{
		CommandSearchName searchCommand = (CommandSearchName) command;
		UI.printPerspective(FORMAT.AGENDA, storage.searchName(searchCommand.getSearchString()));
	}
	
	protected static void searchTime(Command command) throws Exception{
		CommandSearchTime searchCommand = (CommandSearchTime) command;
		UI.printPerspective(FORMAT.AGENDA, storage.search(searchCommand.getTimeStart(), searchCommand.getTimeEnd()));
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
		//System.out.println(displayString);
		UI.printResponse(displayString);
	}

}
