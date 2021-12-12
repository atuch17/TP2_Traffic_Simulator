package simulator.model;

import java.util.ArrayList;
import java.util.List;

import exceptions.NegativeException;
import exceptions.StatusException;

public class NewVehicleEvent extends Event {
	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;

	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = new ArrayList<String>(itinerary);
	}

	@Override
	void execute(RoadMap map) throws NegativeException, StatusException {

		List<Junction> j_aux = new ArrayList<Junction>();
		for (String ss : itinerary)
			j_aux.add(map.getJunction(ss));

		Vehicle veh = new Vehicle(id, maxSpeed, contClass, j_aux);
		map.addVehicle(veh);
		veh.moveToNextRoad();
	}

	@Override
	public String toString() {
		return "New Vehicle '" + id + "'";
	}

}
