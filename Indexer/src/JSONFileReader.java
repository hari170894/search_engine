import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class JSONFileReader {
	/**
	 * Method to parse JSON.
	 * @return
	 */
	public static JSONArray getData(){
		 JSONParser parser = new JSONParser();

	        try {

	            Object obj = parser.parse(new FileReader("data/shakespeare-scenes.json"));

	            JSONObject jsonObject = (JSONObject) obj;
	            JSONArray jsonArray= (JSONArray)jsonObject.get("corpus");
	            return jsonArray;
	           
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
			return null;
	}
	 
}
