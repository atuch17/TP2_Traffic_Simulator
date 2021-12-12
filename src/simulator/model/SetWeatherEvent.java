package simulator.model;

import java.util.Iterator;
import java.util.List;

import exceptions.NegativeException;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	private List<Pair<String, Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) throws NegativeException {
		super(time);
		if(ws==null) {
			throw new NegativeException("La lista cs de SetWeatherEvent es null!");
		}
		this.ws=ws; 
	}

	@Override
	void execute(RoadMap map) throws NegativeException { 
		Iterator<Pair<String, Weather>> it = ws.iterator();
		
		while(it.hasNext()) {
			Pair<String, Weather> p = it.next();
			Road road = map.getRoad(p.getFirst());
			if(road==null)
				throw new NegativeException("La carretera no existe en el mapa de carreteras!");
			road.setWeather(p.getSecond());				
		}		
		
	}
	
	@Override
	public String toString() {
		return "New Weather '" + ws.toString() + "'";// su id //TODO SI?
	}
}
