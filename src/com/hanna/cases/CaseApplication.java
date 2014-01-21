package com.hanna.cases;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Log;

public class CaseApplication extends Application {
	private ArrayList<JSONObject> jArray;
	private ArrayList<JSONObject> jArraySortedByDN;
	private ArrayList<JSONObject> jArraySortedByTime;
	
	@Override
    public void onCreate() {
	    super.onCreate();
	    //creates an array of objects from matters.json and sort them by their display_Numer by default
	    jArray = this.getList();
	    this.sortByDN();
    }
	
	public ArrayList<JSONObject> getJArray(){
		return this.jArray;
	}

	//Load matter.json into a string
	private String loadJSONFromAsset() {
		String json = null;
		try {

			InputStream is = getAssets().open("matters.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

	//parse the string read from the file into array if JSONObjects, sorted by DN
	private ArrayList<JSONObject> getList() {
		ArrayList<JSONObject> jsonValues = new ArrayList<JSONObject>();
		try {
			JSONObject jObject = new JSONObject(this.loadJSONFromAsset());
			JSONArray jArray = jObject.optJSONArray("matters");

			for (int i = 0; i < jArray.length(); i++) {
				jsonValues.add(jArray.getJSONObject(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Collections.sort(jsonValues, new DisplayNumComparator());
		return jsonValues;
	}

	//create the arrayList sorted by their display_number if it doesn't exist already
	//then pass the reference to jArray
	public void sortByDN() {
		if(this.jArraySortedByDN == null){
			jArraySortedByDN = new ArrayList<JSONObject>(jArray);
			Collections.sort(jArraySortedByDN, new DisplayNumComparator());
		}

		jArray = jArraySortedByDN;
	}

	//create the arrayList sorted by their creation time if it doesn't exist already
	//then pass the reference to jArray
	public void sortByTime() {
		if(this.jArraySortedByTime == null){
			jArraySortedByTime = new ArrayList<JSONObject>(jArray);
			Collections.sort(jArraySortedByTime, new CreatedAtComparator());
		}

		jArray = jArraySortedByTime;
	}
	

	class DisplayNumComparator implements Comparator<JSONObject> {

		@Override
		public int compare(JSONObject a, JSONObject b) {
			String displayNumA = "";
			String displayNumB = "";

			try {
				displayNumA = a.getString("display_number");
				displayNumB = b.getString("display_number");
			} catch (JSONException e) {
				Log.e("Matter", "JSONException thrown in jArray sort", e);
			}

			int comp = displayNumA.compareTo(displayNumB);

			if (comp > 0)
				return 1;
			if (comp < 0)
				return -1;

			return 0;
		}
	}

	class CreatedAtComparator implements Comparator<JSONObject> {

		@Override
	    public int compare(JSONObject a, JSONObject b) {
			Date dateA = null;
			Date dateB = null;
			String creationTimeA, creationTimeB;
			
			try {
				 creationTimeA = a.getString("created_at");
				 creationTimeB = b.getString("created_at");
				 
				 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				 
				 dateA = format.parse(creationTimeA);
				 dateB = format.parse(creationTimeB);
				 
	        } catch (JSONException e) {
	        	Log.e("Matter", "JSONException thrown in jArray sort", e);
	        } catch (ParseException e) {
	        	Log.e("Matter", "ParseException thrown in jArray sort", e);
	        }

			int comp = dateA.compareTo(dateB);

			//
			if (comp > 0)
				return -1;
			if (comp < 0)
				return 1;

		    return 0;
	    }
	}
}
