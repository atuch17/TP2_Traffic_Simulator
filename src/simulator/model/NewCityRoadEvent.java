package simulator.model;

import exceptions.NegativeException;

public class NewCityRoadEvent extends NewRoadEvent {

	public NewCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	Road createRoadObject() throws NegativeException {
		Road road = new CityRoad(id, this.j_srcJun, this.j_destJunc, maxSpeed, co2Limit, length, weather);
		return road;
	}

	@Override
	void execute(RoadMap map) throws NegativeException {

		this.j_srcJun = map.getJunction(srcJun);
		this.j_destJunc = map.getJunction(destJunc);
		map.addRoad(this.createRoadObject());

	}

	@Override
	public String toString() {
		return "New City Road '" + id + "'";
	}

}
