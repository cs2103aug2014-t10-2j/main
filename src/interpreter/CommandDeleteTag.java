package interpreter;

/**
 * Class for Search Tag command, which contains a String tag and a
 * boolean variable to indicate missing arguments. 
 * @author a0066754w
 *
 */
public class CommandDeleteTag extends Command {
	String tag = null;

	public CommandDeleteTag(String newTag, 
			String userInput, boolean hasMissingArgs) {
		super(Command.DELETE_TAG, userInput, hasMissingArgs);
		tag = newTag;
	}
	/**
	 * Accessor for tag
	 * @return String representation of tag
	 */
	public String getTag() {
		return tag;
	}
}
