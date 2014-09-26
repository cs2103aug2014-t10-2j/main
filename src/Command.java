/**
 * Class to create command objects with command type and arguments. Super-class
 * of CommandAdd, CommandDelete, CommandUpdate, CommandView and CommandUndo.
 * @author SP
 * 
 */
public class Command {
	String commandType = null;
	
	/**
	 * Constructor for command object. It is initialised with a string
	 * indicating the command type
	 * @param newCommandType the type of command
	 */
	public Command(String newCommandType) {
		commandType = newCommandType;
	}
	
	/**
	 * Returns a string which represents the type of command, which can be Add,
	 * Delete, Update, View or Undo.
	 * @return commandType the type of command
	 */
	public String getCommandType() {
		return commandType;
	}

}
