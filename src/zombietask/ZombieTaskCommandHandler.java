package zombietask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;


//import storage.Storage;
import storage.StorageAPI;
import task.Task;
import task.TaskUIFormat;
import ui.FORMAT;
import ui.GUI;
import ui.TaskPrinter;
import ui.UI;
import interpreter.Command;
import interpreter.CommandAdd;
import interpreter.CommandDelete;
import interpreter.CommandDeleteLocation;
import interpreter.CommandDeleteName;
import interpreter.CommandDeleteTag;
import interpreter.CommandDeleteTime;
import interpreter.CommandDone;
import interpreter.CommandSearchLocation;
import interpreter.CommandHelp;
import interpreter.CommandSearchName;
import interpreter.CommandSearchTag;
import interpreter.CommandSearchTime;
import interpreter.CommandUpdate;
import interpreter.CommandView;

public class ZombieTaskCommandHandler {
	
	/*
	 * Class constants
	 */
	
	private final static String COMMAND_ADD = Command.ADD; //edit out
	private final static String COMMAND_DELETE = Command.DELETE;
	private final static String COMMAND_DELETE_NAME = Command.DELETE_NAME;
	private final static String COMMAND_DELETE_TAG = Command.DELETE_TAG;
	private final static String COMMAND_DELETE_TIME = Command.DELETE_TIME;
	private final static String COMMAND_DELETE_LOCATION = Command.DELETE_LOCATION;
	private final static String COMMAND_UPDATE = Command.UPDATE;
	private final static String COMMAND_VIEW = Command.VIEW;
	private final static String COMMAND_UNDO = Command.UNDO;
	private final static String COMMAND_REDO = Command.REDO;
	private final static String COMMAND_HELP = Command.HELP;
	private final static String COMMAND_DONE = Command.DONE;
	private final static String COMMAND_SEARCH = Command.SEARCH;
	private final static String COMMAND_SEARCH_NAME = Command.SEARCH_NAME;
	private final static String COMMAND_SEARCH_TIME = Command.SEARCH_TIME;
	private final static String COMMAND_SEARCH_TAG = Command.SEARCH_TAG;
	private final static String COMMAND_SEARCH_LOCATION = Command.SEARCH_LOCATION;
	private final static String COMMAND_EXIT = Command.EXIT;
	private final static String COMMAND_INVALID = "invalid command %s";
	
	private final static boolean SUCCESS = true;
	//private final static boolean FAILURE = false;
	
	// for testing
	private static String commandCalled = null;
	
	/*
	 * Messages
	 */

	public static final String TAB = "&nbsp&nbsp&nbsp&nbsp&nbsp";
	private final static String MESSAGE_INVALID_COMMAND = "Invalid Command:\n%s";
	private final static String MESSAGE_HELP_ADD = TaskPrinter.ORANGE.concat("Add:<br>").concat(TAB)
			.concat(TaskPrinter.MAGENTA).concat("add different kinds of tasks to the task list including floating tasks,<br>")
			.concat(TAB).concat(" deadline tasks and timed tasks<br>").concat(TAB)
			.concat(TaskPrinter.CYAN).concat("add ").concat(TaskPrinter.RED).concat("taskname<br>").concat(TAB)
			.concat(TaskPrinter.CYAN).concat("add ").concat(TaskPrinter.RED).concat("taskname ")
			.concat(TaskPrinter.GREEN).concat("end time<br>").concat(TAB)
			.concat(TaskPrinter.CYAN).concat("add ").concat(TaskPrinter.RED).concat("taskname ")
			.concat(TaskPrinter.GREEN).concat("start time to end time<br>");
	
	private final static String MESSAGE_HELP_DELETE = TaskPrinter.ORANGE.concat("Delete:<br>").concat(TAB)
			.concat(TaskPrinter.MAGENTA).concat("delete a task with its index <br>").concat(TAB)
			.concat(TaskPrinter.CYAN).concat("delete ").concat(TaskPrinter.RED)
			.concat("task number<br>");
	
	private final static String MESSAGE_HELP_SEARCH = TaskPrinter.ORANGE.concat("Search:<br>").concat(TAB)
			.concat(TaskPrinter.MAGENTA).concat("search tasks with task name, time or location <br>")
			.concat(TAB).concat(TaskPrinter.CYAN).concat("search-name ").concat(TaskPrinter.RED)
			.concat("task name <br>").concat(TAB).concat(TaskPrinter.CYAN).concat("search-tag ")
			.concat(TaskPrinter.RED).concat("tag <br>").concat(TAB).concat(TaskPrinter.CYAN)
			.concat("search-location ").concat(TaskPrinter.RED).concat("location <br>").concat(TAB)
			.concat(TaskPrinter.CYAN).concat("search-time ").concat(TaskPrinter.RED)
			.concat("start time ").concat(TaskPrinter.GREEN).concat("to").concat(TaskPrinter.RED)
			.concat(" end time <br>");
	
	private final static String MESSAGE_HELP_UPDATE = TaskPrinter.ORANGE.concat("Update:<br>").concat(TAB)
			.concat(TaskPrinter.MAGENTA)
			.concat("update the information of the task with its index <br>").concat(TAB)
			.concat(TaskPrinter.CYAN).concat("add ").concat(TaskPrinter.RED).concat("task number ")
			.concat(TaskPrinter.RED).concat("start time ").concat(TaskPrinter.GREEN).concat("to")
			.concat(TaskPrinter.RED).concat(" end time<br>");
	
	private final static String MESSAGE_HELP_VIEW = TaskPrinter.ORANGE.concat("View:<br>").concat(TAB).concat(TaskPrinter.MAGENTA)
			.concat("different kinds of views for tasks including agenda, daily and weekly <br>")
			.concat(TAB).concat(TaskPrinter.CYAN).concat("view ").concat(TaskPrinter.RED)
			.concat("agenda<br>").concat(TAB).concat(TaskPrinter.CYAN).concat("view ")
			.concat(TaskPrinter.RED).concat("daily <br>").concat(TAB).concat(TaskPrinter.CYAN)
			.concat("view ").concat(TaskPrinter.RED).concat("weekly <br>");
	
	private final static String MESSAGE_HELP_UNDO = TaskPrinter.ORANGE.concat("Undo:<br>").concat(TAB).concat(TaskPrinter.MAGENTA)
			.concat("reverse the action to the previous state <br>").concat(TAB)
			.concat(TaskPrinter.CYAN).concat("undo<br>");
	
	private final static String MESSAGE_HELP_REDO = TaskPrinter.ORANGE.concat("Redo:<br>").concat(TAB)
			.concat(TaskPrinter.MAGENTA).concat("go the the state before the undo action <br>")
			.concat(TAB).concat(TaskPrinter.CYAN).concat("redo<br>");
	
	private final static String MESSAGE_HELP_DONE = TaskPrinter.ORANGE.concat("Done:<br>")
			.concat(TAB).concat(TaskPrinter.MAGENTA).concat("mark a task as completed or uncompleted <br>")
			.concat(TAB).concat(TaskPrinter.CYAN).concat("done<br>");
	
	private final static String MESSAGE_HELP_EXIT = TaskPrinter.ORANGE.concat("Exit:<br>").concat(TAB)
			.concat(TaskPrinter.MAGENTA).concat("exit the program <br>").concat(TAB)
			.concat(TaskPrinter.CYAN).concat("exit ");
	
	private final static String MESSAGE_ADD = "Added %s to database";
	private final static String MESSAGE_DELETE = "Deleted %s from database";
	private final static String MESSAGE_DELETE_MULTIPLE = "Deleted %d tasks from database";
	private final static String MESSAGE_UPDATE = "Updated %s to %s from database";
	//private final static String MESSAGE_OUTOFBOUNDS = "Warning: input %s is out of bounds";
	private final static String MESSAGE_DONE = "Marked %s as done";
	private final static String MESSAGE_UNDONE = "Marked %s as undone";
	private final static String MESSAGE_CLASH_WARNING = "Warning<br>&nbsp&nbsp&nbsp&nbsp&nbsp Tasks"
			+ " %s and %d other task(s) clashes<br>Clashed Tasks:<br>"; //"Warning:\n\tTasks %s and %s clashes";
	//private final static String MESSAGE_CLASH_MORE_THAN_ONE = "Warning\n\tTasks %s and %d other task(s) clashes";
	
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
	private static TaskUIFormat deleteList = null;
	private static TaskUIFormat currentList = null;
	
	private static ArrayList<String> futureCommandDescriptorList = new ArrayList<String>();
	private static ArrayList<Command> futureCommandList = new ArrayList<Command>();
	private static ArrayList<TaskUIFormat> futureTaskList = new ArrayList<TaskUIFormat>();
	private static ArrayList<String> pastCommandDescriptorList = new ArrayList<String>();
	private static ArrayList<Command> pastCommandList = new ArrayList<Command>();
	private static ArrayList<TaskUIFormat> pastTaskList = new ArrayList<TaskUIFormat>();
	
	private static StorageAPI storage;
	
	private static GUI window;
	
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
		commandCalled = currentCommandDescriptor;
		switch(currentCommandDescriptor){
		case COMMAND_ADD:
			addCommand(currentCommand);
			break;
		case COMMAND_DELETE:
			deleteCommand(currentCommand);
			break;
		case COMMAND_DELETE_NAME:
			deleteName(currentCommand);
			break;
		case COMMAND_DELETE_TAG:
			deleteTag(currentCommand);
			break;
		case COMMAND_DELETE_TIME:
			deleteTime(currentCommand);
			break;
		case COMMAND_DELETE_LOCATION:
			deleteLocation(currentCommand);
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
			help(currentCommand);
			break;
		case COMMAND_DONE:
			doneCommand(currentCommand);
			break;
		case COMMAND_SEARCH_NAME:
			searchName(currentCommand);
			break;
		case COMMAND_SEARCH_TIME:
			searchTime(currentCommand);
			break;
		case COMMAND_SEARCH_TAG:
			searchTag(currentCommand);
			break;
		case COMMAND_SEARCH_LOCATION:
			searchLocation(currentCommand);
			break;
		case COMMAND_EXIT:
			exit();
			break;
		default:
		case COMMAND_INVALID:
			invalidCommand(currentCommandString);
			break;
		}
		if(currentCommandDescriptor != COMMAND_VIEW && !currentCommandDescriptor.contains(COMMAND_SEARCH) 
				&& currentCommandDescriptor != COMMAND_HELP)
			ZombieTask.userInput("view agenda");
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
		currentList = null;
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
			
			currentList = new TaskUIFormat().addTask(currentTask);
			recordCommand();
			
			TaskUIFormat clashedTasks = storage.taskClash(currentTask);
			
			String reply = "";
			
			if(clashedTasks.size() >= 1){
				
				reply += String.format(MESSAGE_CLASH_WARNING, currentTask.getTaskName(), clashedTasks.size());
				
				for(int i = 0; i < clashedTasks.size(); i++){
					reply += TaskPrinter.printTask(clashedTasks.nextTask(),1,0);
					reply += "<br>";
				}
			}
			
			reply += String.format(MESSAGE_ADD, currentTask.getTaskName());
			reply += "<br>";
			reply += UI.printTask(currentTask, 1, 0);
			showToUser(reply);
			
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
			ArrayList<String> lineCodes = currentDeleteCommand.getLineCode();
			currentList = new TaskUIFormat();
			for(String lineCode : lineCodes){
				currentTask = storage.search(lineCode);
				currentList.addTask(currentTask);
			}
			
			storage.delete(currentList);
			if (currentList.size() == 1){
				showToUser(String.format(MESSAGE_DELETE, currentTask.getTaskName()));
			}else{
				showToUser(String.format(MESSAGE_DELETE_MULTIPLE, currentList.size()));
			}
			
			recordCommand();
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
				Calendar startDay = Calendar.getInstance();
				/*
				setMinimumCalendarField(startDay, Calendar.HOUR_OF_DAY);
				setMinimumCalendarField(startDay, Calendar.MINUTE);
				setMinimumCalendarField(startDay, Calendar.SECOND);
				*/
				Calendar endDay = Calendar.getInstance();
				/*
				setMaximumCalendarField(endDay, Calendar.HOUR_OF_DAY);
				setMaximumCalendarField(endDay, Calendar.MINUTE);
				setMaximumCalendarField(endDay, Calendar.SECOND);
				*/
				endDay.add(Calendar.DAY_OF_YEAR, 1);
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
		String location = currentAddCommand.getLocation();
		ArrayList<String> tags = currentAddCommand.getTags();

		// Get old task
		/*
		 * Note old method depriciated.
		 */
		oldTask = storage.search(currentUpdateCommand.getLineCode());
		String oldTaskName = oldTask.getTaskName();
		Calendar oldStartTime = oldTask.getStartTime();
		Calendar oldEndTime = oldTask.getEndTime();
		ArrayList<String> oldTags = oldTask.getTags();

		if (taskName == null) {
			taskName = oldTaskName;
		}
		if (startTime == null) {
			startTime = oldStartTime;
		}
		if (endTime == null) {
			endTime = oldEndTime;
		}
		if (tags.isEmpty()) {
			tags = oldTags;
		}
		if (location == null) {
			location = oldTask.getLocation();
		}

		/*
		 * Temp fix for SP's code
		 */

		if (startTime != null && endTime == null) {
			Calendar tempTime = startTime;
			startTime = endTime;
			endTime = tempTime;
		} else if (startTime != null && endTime != null) {
			if (startTime.after(endTime)) {
				Calendar tempTime = startTime;
				startTime = endTime;
				endTime = tempTime;
			}
		}

		if (startTime == null && endTime == null) {
			currentTask = new Task(taskName);
		} else if (startTime == null) {
			currentTask = new Task(taskName, endTime);
		} else {
			currentTask = new Task(taskName, endTime, startTime);
		}

		// Add Tags
		for (String tag : tags) {
			currentTask.addTag(tag);
		}
		
		// Add location
		if (location != null){
			currentTask.setLocation(location);
		}

		// delete old task
		try {
			currentList = new TaskUIFormat().setOldTask(oldTask).setNewTask(
					currentTask);
			storage.delete(oldTask);
			storage.add(currentTask);

		} catch (Exception err) {
			showToUser(err.getMessage());
			err.printStackTrace();
		}
		
		TaskUIFormat clashedTasks = storage.taskClash(currentTask);
		
		String reply = "";
		
		if(clashedTasks.size() >= 1){
			
			reply += String.format(MESSAGE_CLASH_WARNING, currentTask.getTaskName(), clashedTasks.size());
			
			for(int i = 0; i < clashedTasks.size(); i++){
				reply += TaskPrinter.printTask(clashedTasks.nextTask(),1,0);
				reply += "<br>";
			}
		}
		
		reply += String.format(MESSAGE_UPDATE, oldTask.getTaskName(),
				currentTask.getTaskName());
		showToUser(reply);
		
		recordCommand();

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
			currentList = pastTaskList.remove(pastTaskList.size() - 1);
			
			// Execute undo
			
			switch (currentCommandDescriptor){
			case COMMAND_ADD:
				currentList = storage.delete(currentList);
				break;
			case COMMAND_DELETE:
			case COMMAND_DELETE_NAME:
			case COMMAND_DELETE_TAG:
			case COMMAND_DELETE_TIME:
			case COMMAND_DELETE_LOCATION:
				storage.add(currentList);
				break;
			case COMMAND_UPDATE:
				storage.delete(currentList.getNewTask());
				storage.add(currentList.getOldTask());
				break;
			case COMMAND_DONE:
				storage.toggleComplete(currentList);
				break;
			default:
			case COMMAND_INVALID:
				logger.log(Level.INFO, ERROR_INVALID_UNDO_REDO);
				showToUser(ERROR_INVALID_UNDO_REDO);
			}
			
			// Push items into futureLists
			futureCommandDescriptorList.add(currentCommandDescriptor);
			futureCommandList.add(currentCommand);
			futureTaskList.add(currentList);
			
			
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
			currentList = futureTaskList.remove(futureTaskList.size() - 1);
			
			
			// Execute redo
			
			switch (currentCommandDescriptor){
			case COMMAND_ADD:
				storage.add(currentList);
				break;
			case COMMAND_DELETE:
			case COMMAND_DELETE_NAME:
			case COMMAND_DELETE_TAG:
			case COMMAND_DELETE_TIME:
			case COMMAND_DELETE_LOCATION:
				storage.delete(currentList);
				break;
			case COMMAND_UPDATE:
				storage.delete(currentList.getOldTask());
				storage.add(currentList.getNewTask());
				break;
			case COMMAND_DONE:
				storage.toggleComplete(currentList);
				break;
			default:
			case COMMAND_INVALID:
				logger.log(Level.INFO, ERROR_INVALID_UNDO_REDO);
				showToUser(ERROR_INVALID_UNDO_REDO);
			}
			
			// Push items into pastLists
			
			pastCommandDescriptorList.add(currentCommandDescriptor);
			pastCommandList.add(currentCommand);
			pastTaskList.add(currentList);
			
		} catch (Exception err) {
			showToUser(err.getMessage());
			err.printStackTrace();
		}
		
	}
	
	protected static void help(Command command) {
		CommandHelp helpCommand = (CommandHelp) command;
		String userInput = helpCommand.getHelpCommand();
		
		String response = "";
		
		if(userInput == null){
			response += MESSAGE_HELP_ADD + "\n" + MESSAGE_HELP_DELETE + "\n" + MESSAGE_HELP_UPDATE + "\n" + MESSAGE_HELP_SEARCH + "\n" + MESSAGE_HELP_VIEW+ "\n" + MESSAGE_HELP_UNDO+ "\n" + MESSAGE_HELP_REDO+ "\n" + MESSAGE_HELP_DONE + "\n" + MESSAGE_HELP_EXIT;
			window.modifyLabelText(String.format(UI.LABEL_FORMAT, response));
			return;
		}
		switch(userInput){
		case COMMAND_ADD:
			response += (MESSAGE_HELP_ADD);
			break;
		case COMMAND_DELETE:
			response += (MESSAGE_HELP_DELETE);
			break;
		case COMMAND_UPDATE:
			response += (MESSAGE_HELP_UPDATE);
			break;
		case COMMAND_SEARCH:
			response += (MESSAGE_HELP_SEARCH);
			break;
		case COMMAND_VIEW:
			response += (MESSAGE_HELP_VIEW);
			break;
		case COMMAND_UNDO:
			response += (MESSAGE_HELP_UNDO);
			break;
		case COMMAND_REDO:
			response += (MESSAGE_HELP_REDO);
			break;
		case COMMAND_DONE:
			response += (MESSAGE_HELP_DONE);
			break;
		case COMMAND_EXIT:
			response += (MESSAGE_HELP_EXIT);
			break;	
		default:
			logger.log(Level.INFO,String.format(MESSAGE_INVALID_COMMAND, userInput) );
			showToUser(String.format(MESSAGE_INVALID_COMMAND, userInput));
			return;
			
		}
		
		window.modifyLabelText(String.format(UI.LABEL_FORMAT, response));
	}
	
	protected static void searchName(Command command) throws Exception{
		CommandSearchName searchCommand = (CommandSearchName) command;
		UI.printPerspective(FORMAT.AGENDA, storage.searchName(searchCommand.getSearchString()));
	}
	
	protected static void deleteName(Command command) throws Exception{
		CommandDeleteName deleteCommand = (CommandDeleteName) command;
		currentList = storage.searchName(deleteCommand.getSearchString());
		storage.delete(currentList);
		recordCommand();
	}
	
	protected static void searchTime(Command command) throws Exception{
		CommandSearchTime searchCommand = (CommandSearchTime) command;
		UI.printPerspective(FORMAT.AGENDA, storage.search(searchCommand.getTimeStart(), searchCommand.getTimeEnd()));
	}
	
	protected static void deleteTime(Command command) throws Exception{
		CommandDeleteTime deleteCommand = (CommandDeleteTime) command;
		currentList = storage.search(deleteCommand.getTimeStart(), deleteCommand.getTimeEnd());
		storage.delete(currentList);
		recordCommand();
		/*
		 * To be implemented UNDO and REDO
		 */
	}
	
	protected static void searchTag(Command command) throws Exception{
		CommandSearchTag searchCommand = (CommandSearchTag) command;
		UI.printPerspective(FORMAT.AGENDA, storage.searchTag(searchCommand.getTag()));
	}
	
	protected static void deleteTag(Command command) throws Exception{
		CommandDeleteTag deleteCommand = (CommandDeleteTag) command;
		currentList = storage.searchTag(deleteCommand.getTag());
		storage.delete(currentList);
		recordCommand();
		/*
		 * To be implemented UNDO and REDO
		 */
	}
	
	protected static void searchLocation(Command command) throws Exception{
		CommandSearchLocation searchCommand = (CommandSearchLocation) command;
		UI.printPerspective(FORMAT.AGENDA, storage.searchLocation(searchCommand.getLocation()));
	}
	
	protected static void deleteLocation(Command command) throws Exception{
		CommandDeleteLocation deleteCommand = (CommandDeleteLocation) command;
		deleteList = storage.searchLocation(deleteCommand.getLocation());
		storage.delete(deleteList);
		recordCommand();
		/*
		 * To be implemented UNDO and REDO
		 */
	}
	
	protected static void doneCommand(Command command) throws Exception{
		CommandDone doneCommand = (CommandDone) command;
		ArrayList<String> lineCodes = doneCommand.getLineCode();
		currentList = new TaskUIFormat();
		for(String lineCode : lineCodes){
			currentTask = storage.search(lineCode);			
			currentList.addTask(currentTask);
			storage.toggleComplete(currentTask);
			if (currentTask.isCompleted()){
				showToUser(String.format(MESSAGE_DONE, currentTask.getTaskName()));
			}else{
				showToUser(String.format(MESSAGE_UNDONE, currentTask.getTaskName()));
			}
		}
		
		recordCommand();
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
		pastTaskList.add(currentList);
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
	/*
	private static void clearPastLists() {
		if (pastCommandList.size() > 0){
			pastCommandDescriptorList.clear();
			pastCommandList.clear();
			pastTaskList.clear();
		}
	}*/
	
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
	
	static boolean setPastTaskList(ArrayList<TaskUIFormat> newTaskList){
		pastTaskList = newTaskList;
		return SUCCESS;
	}
	
	static ArrayList<TaskUIFormat> getPastTaskList(){
		return pastTaskList;
	}
	
	static boolean setFutureTaskList(ArrayList<TaskUIFormat> newTaskList){
		futureTaskList = newTaskList;
		return SUCCESS;
	}
	
	static ArrayList<TaskUIFormat> getFutureTaskList(){
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

	public static GUI getWindow() {
		return window;
	}

	public static void setWindow(GUI window) {
		ZombieTaskCommandHandler.window = window;
	}

}
