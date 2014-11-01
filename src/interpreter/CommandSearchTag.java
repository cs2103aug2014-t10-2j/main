package interpreter;
import java.util.Calendar;

/**
 * Class for Search Tag command, which contains a String tag and a
 * boolean variable to indicate missing arguments. 
 * @author SP
 *
 */
public class CommandSearchTag extends Command {
	String tag = null;

	public CommandSearchTag(String newTag, 
			String userInput, boolean hasMissingArgs) {
		super(Command.SEARCH_TAG, userInput, hasMissingArgs);
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
