package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder{

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	Event createTheRoad(int time, String id, String srcJun, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather) {		
		return new NewInterCityRoadEvent(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		NewInterCityRoadEvent event = (NewInterCityRoadEvent) createTheRoad(data.getInt("time"), data.getString("id"), data.getString("src"), 
                data.getString("dest"),  data.getInt("length"), data.getInt("co2limit"), 
                data.getInt("maxspeed"), Weather.valueOf(data.getString("weather")));

		return event;
	}	
}
