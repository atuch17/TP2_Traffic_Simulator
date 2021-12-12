package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.NegativeException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	public SetWeatherEventBuilder() {
		super( "set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) throws NegativeException {
		
		List <Pair<String, Weather>> ws = new ArrayList<>();//falta inicializar?
		
		JSONArray ja = data.getJSONArray("info");
		
		for (int i =0; i< ja.length();i++) {
			JSONObject jo = ja.getJSONObject(i);
			ws.add(new Pair<String, Weather>(jo.getString("road"), Weather.valueOf(jo.getString("weather").toUpperCase())));
		}
		
		return new SetWeatherEvent(data.getInt("time"), ws); 
	}

}
