package storage;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

/**
 * 
 * Handles Task JSONObject parsing and serialization of Task to JSON.
 * 
 * Based on GSON.
 * 
 * Refer to http://www.studytrails.com/java/json/java-google-json-introduction.jsp
 * 
//@author A0119480
//@author jellymac
 *
 */

public class StorageJSONHandler {
	
	/**
	 * Converts Task Object to JSON Format. Serializes null values and pretty printing is turned on.
	 * 
	 * @param task Task object as defined by ZombieTask package
	 * @return JSON object represented as string
	 */
	
	
	public static String convertToJSON(Storage taskStorage){
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls().setPrettyPrinting();
		Gson gson = builder.create();
		return gson.toJson(taskStorage);
	}
	
	/**
	 * Converts JSON string to Storage object
	 * @param inputString
	 * @return
	 */
	
	public static Storage convertToStorage(String inputString){
		Gson gson = new Gson();
		return gson.fromJson(inputString, Storage.class);
	}
}
