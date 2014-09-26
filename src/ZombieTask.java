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
	 * Instantiation of External Objects
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
	private static String fileName = null;
	private static ArrayList<Command> commandList = new ArrayList<Command>();
	
	
	/*
	 * Standard Messages
	 */
	
	private static String MESSAGE_WELCOME = "Welcome to Zombie Task!";
	private static String MESSAGE_FILE_OPENED = "%s is ready for use";
	
	/**
	 * 
	 * Method that will be invoked when ZombieTask is called.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		System.out.println(WELCOME_MESSAGE);
		initStorage(args);
		
		while(sc.hasNext()){
			lastCommand = sc.nextLine();
			
		}
	}

	private static void initStorage(String[] args) {
		if (args.length > 0){
			storage.setFile(args[0]);
		}
		storage.createFile();
	}
	
	/*
	 * filename mutators
	 */
	
	/*
	 * commandList mutators
	 */

}
