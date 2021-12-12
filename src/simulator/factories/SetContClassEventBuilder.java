package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import exceptions.NegativeException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws JSONException, NegativeException {

		List <Pair<String, Integer>> ws = new ArrayList<>();
		
		JSONArray ja = data.getJSONArray("info");
		
		for (int i =0; i< ja.length();i++) {
			JSONObject jo = ja.getJSONObject(i);
			ws.add(new Pair<String, Integer>(jo.getString("vehicle"), jo.getInt("class")));
		}
		
		return new NewSetContClassEvent(data.getInt("time"), ws);
	}

}
