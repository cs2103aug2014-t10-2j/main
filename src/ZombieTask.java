import java.util.ArrayList;
import java.util.Scanner;

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
	
	private static UI ui = new UI();
	private static Interpreter parser = new Interpreter();
	private static StorageAPI storage = new StorageAPI();
	private static Scanner sc = new Scanner(System.in);
	
	/*
	 * Instantiation of ZombieTask data attributes
	 */
	
	private static String currentCommandString = null;
	private static Command currentCommand = null;
	private static Task currentTask = null;
	private static ArrayList<Command> futureCommandList = new ArrayList<Command>();
	private static ArrayList<Task> futureTaskList = new ArrayList<Task>();
	private static ArrayList<Command> pastCommandList = new ArrayList<Command>();
	private static ArrayList<Task> pastTaskList = new ArrayList<Task>();
	
	/*
	 * Constants for Command Types
	 */
	
	private final static String COMMAND_ADD = "add";
	private final static String COMMAND_DELETE = "delete";
	private final static String COMMAND_UPDATE = "update";
	private final static String COMMAND_VIEW = "view";
	private final static String COMMAND_UNDO = "undo";
	private final static String COMMAND_REDO = "redo";
	private final static String COMMAND_HELP = "help";
	private final static String COMMAND_INVALID = "invalid";
	
	/*
	 * Constants for Calendar Views
	 */
	
	private final static String VIEW_AGENDA = "AGENDA";
	private final static String VIEW_WEEK = "WEEK";
	private final static String VIEW_MONTH = "MONTH";
	private final static String VIEW_CALENDAR = "CALENDAR";
	
	/*
	 * Standard Messages
	 */
	
	private final static String MESSAGE_WELCOME = "Welcome to Zombie Task!";
	private final static String MESSAGE_FILE_OPENED = "%s is ready for use";
	private final static String MESSAGE_MISSING_ARGUMENTS = "Command Missing Arguments:\n%s";
	private final static String MESSAGE_INVALID_COMMAND = "Invalid Command:\n%s";
	private final static String MESSAGE_INVALID_FILENAME = "Invalid FileName: %s";
	private final static String MESSAGE_HELP = "THIS HELP NEEDS A BIT OF HELP!";
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
			currentCommandString = sc.nextLine();
			currentCommand = Interpreter.getCommand(currentCommandString);
			if(currentCommand.hasMissingArgs()){
				showToUser(String.format(MESSAGE_MISSING_ARGUMENTS, currentCommandString));
				continue;
			}
			execute();
			
		}
	}





	private static void execute() {
		switch(currentCommand.getCommandType()){
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
		case COMMAND_INVALID:
			invalidCommand(currentCommandString);
			break;
		default:
			invalidCommand(currentCommandString);
			break;
		}
	}

	

	
	
	/*
	 * Command Handlers
	 */
	
	private static void addCommand(Command command) {
		// TODO Auto-generated method stub
		
	}

	private static void deleteCommand(Command command) {
		// TODO Auto-generated method stub
		
	}

	private static void viewCommand(Command command) {
		// TODO Auto-generated method stub
		
	}
	
	private static void updateCommand(Command command) {
		// TODO Auto-generated method stub
		
	}

	private static void invalidCommand(String commandString) {
		showToUser(String.format(MESSAGE_INVALID_COMMAND, commandString));
	}

	private static void undo() {
		try{
			currentCommand = removeLastCommandFromList(pastCommandList);
		} catch(Exception err) {
			showToUser(err.getMessage());
		}
	}
	
	private static void redo() {
		try{
			currentCommand = removeLastCommandFromList(futureCommandList);
		} catch(Exception err) {
			showToUser(err.getMessage());
		}
		
	}
	
	private static void help() {
		showToUser(MESSAGE_HELP);
	}


	/**
	 * Initialises storage with first args
	 * 
	 * @param args optional first element of args[] will be set as file accessed
	 */
	
	static void initStorage(String[] args) {
		try {
			if (args.length > 0){
				storage.setFileName(args[0]);
			}
			storage.createFile();
		} catch (Exception err){
			showToUser(String.format(MESSAGE_INVALID_FILENAME, args[0]));
		} finally {
			showToUser(String.format(MESSAGE_FILE_OPENED, storage.getFileName()));
		}	
	}
	
	/*
	 * commandList mutators
	 */
	
	private static boolean addCommandToList(Command command, Task task){
		pastCommandList.add(command);
		pastTaskList.add(task);
		if (futureCommandList.size() > 0){
			futureCommandList.clear();
			futureTaskList.clear();
		}
		return true;
	}
	
	private static Command removeLastCommandFromList(ArrayList<Command> commandList) throws Exception{
		return null;
	}
	
	/*
	 * Polymorphic method for Commands
	 */
	
	static Command getCommand(){
		return currentCommand;
	}
	
	static boolean setCommand(Command newCommand)
	{
		currentCommand = newCommand;
		return SUCCESS;
	}
	/**
	 * 
	 * Polymorphic method for CommandLists
	 * 
	 * @param newCommandList new CommandList
	 * @return boolean value indicating success of method
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
	
	/*
	 * Polymorphic Methods for Storage
	 */
	
	static StorageAPI getStorage(){
		return storage;
	}
	
	static boolean setStorage(StorageAPI newStorage){
		storage = newStorage;
		return SUCCESS;
	}
	
	/*
	 * Miscellaneous Helper Functions
	 */
	
	
	
	/**
	 * Displays formatted string to users, separate from UI interface
	 * 
	 * @param displayString String to be displayed
	 */
	
	private static void showToUser(String displayString) {
		System.out.println(displayString);
	}

}
