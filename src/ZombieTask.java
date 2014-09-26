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
	
	private static String lastCommandString = null;
	private static Command lastCommand = null;
	private static ArrayList<Command> commandList = new ArrayList<Command>();
	
	/*
	 * Constants for Command Types
	 */
	
	private static String COMMAND_ADD = "ADD";
	private static String COMMAND_DELETE = "DELETE";
	private static String COMMAND_UPDATE = "UPDATE";
	private static String COMMAND_VIEW = "VIEW";
	
	/*
	 * Constants for Calendar Views
	 */
	
	private static String VIEW_AGENDA = "AGENDA";
	private static String VIEW_WEEK = "WEEK";
	private static String VIEW_MONTH = "MONTH";
	private static String VIEW_CALENDAR = "CALENDAR";
	
	/*
	 * Standard Messages
	 */
	
	private static String MESSAGE_WELCOME = "Welcome to Zombie Task!";
	private static String MESSAGE_FILE_OPENED = "%s is ready for use";
	private static String MESSAGE_MISSING_ARGUMENTS = "Command Missing Arguments:\n%s";
	private static String MESSAGE_INVALID_COMMAND = "Invalid Command:\n%s";
	
	/**
	 * Method that will be invoked when ZombieTask is called.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		showToUser(MESSAGE_WELCOME);
		initStorage(args);
		
		while(sc.hasNext()){
			lastCommandString = sc.nextLine();
			lastCommand = parser.getCommand(lastCommandString);
			if(lastCommand.hasMissingArgs()){
				showToUser(String.format(MESSAGE_MISSING_ARGUMENTS, lastCommandString));
				continue;
			}
			switch(lastCommand.getCommandType()){
			
			default:
				invalidCommand();
				break;
			}
			
		}
	}

	

	
	
	/*
	 * storage mutators
	 */
	
	/**
	 * Initialises storage with first args
	 * 
	 * @param args optional first element of args[] will be set as file accessed
	 */
	
	private static void initStorage(String[] args) {
		if (args.length > 0){
			storage.setFileName(args[0]);
		}
		storage.createFile();
		showToUser(String.format(MESSAGE_FILE_OPENED, storage.getFileName));
	}
	
	/*
	 * commandList mutators
	 */
	
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
