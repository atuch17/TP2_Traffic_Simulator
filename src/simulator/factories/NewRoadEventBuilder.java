package simulator.factories;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event>{

	public NewRoadEventBuilder(String _type) {
		super(_type);
	}

	/*@Override
	protected Event createTheInstance(JSONObject data) {
		// No hace falta, se encargan las clases hijas
		return null;
	}*/

	abstract Event createTheRoad(int time, String id, String srcJun, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather);
}
