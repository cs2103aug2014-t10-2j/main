import java.util.Arrays;
import java.util.List;

/**
 * Parses the command and returns a command object identifying the correct
 * command and arguments.
 * @author SP
 *
 */
public class Interpreter {
	
	// valid command types
	public static final String ADD = "add";
	public static final String DELETE = "delete";
	public static final String UPDATE = "update";
	public static final String VIEW = "view";
	public static final String UNDO = "undo";
	
	static String getCommandType(String userInput) {
		
		String firstWord = userInput.split(" ")[0];
		firstWord = firstWord.toLowerCase();
		List<String> validCommands = Arrays.asList(ADD, DELETE, UPDATE, VIEW, UNDO);
		if (validCommands.contains(firstWord)) {
				return firstWord;
		}
		else {
			return ADD;
		}	
	}
	
	static Command getCommand(String userInput) {
		return new Command("test");
	}
}
