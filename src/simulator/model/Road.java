package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.NegativeException;

public abstract class Road extends SimulatedObject {

	private Junction source;
	private Junction destination;
	private int length; // la longitud de la carretera
	protected int maximumSpeed;
	protected int SpeedLimit; // el limite de velocidad en esa carretera, Su valor inicial = a la velocidad
	protected int contaminationAlarmLimit; // una vez superado ese limite impone restricciones al tr��fico para reducir
											// la contaminaci��n
	protected Weather weatherConditions;
	protected int totalContamination; // co2
	protected List<Vehicle> vehicles; // ordenada descendientemente
	private ComparatorVehicle compV;

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather)
			throws NegativeException {
		super(id);

		if (maxSpeed < 0 || contLimit < 0 || length < 0 || srcJunc == null || destJunc == null || weather == null) {
			throw new NegativeException("Algun argumento de Road  esta mal");
		}

		vehicles = new ArrayList<Vehicle>();
		this.source = srcJunc;
		this.destination = destJunc;
		this.source.addOutGoingRoad(this);
		this.destination.addIncommingRoad(this);
		this.maximumSpeed = maxSpeed;
		this.contaminationAlarmLimit = contLimit;
		this.length = length;
		this.weatherConditions = weather;
		this.compV = new ComparatorVehicle();

		this.SpeedLimit = maximumSpeed;
		this.totalContamination = 0;
	}

	void enter(Vehicle v) throws NegativeException {
		if (v.getCurrentSpeed() == 0 && v.getLocation() == 0) {
			vehicles.add(v);
		} else {
			throw new NegativeException("La velocidad o la localizacion deberia ser 0");
		}
	}

	void exit(Vehicle v) {
		vehicles.remove(v);
	}

	void setWeather(Weather w) throws NegativeException {
		if (w == null) {
			throw new NegativeException("El weather no debe ser null");
		} else {
			weatherConditions = w;
		}
	}

	void addContamination(int c) throws NegativeException {
		if (c >= 0) {
			this.totalContamination += c;// ATUCH
		} else {
			throw new NegativeException("La contaminzacion no debe ser negativo");
		}
	}

	abstract void reduceTotalContamination();

	abstract void updateSpeedLimit();

	abstract int calculateVehicleSpeed(Vehicle v) throws NegativeException;

	@Override
	void advance(int time) throws NegativeException {
		reduceTotalContamination();
		updateSpeedLimit();

		for (Vehicle elements : vehicles) {
			if (elements.getStatus() == VehicleStatus.TRAVELING) {
				elements.setSpeed(calculateVehicleSpeed(elements));
				elements.advance(time);
			}
		}
		vehicles.sort(compV);
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", getId());
		jo.put("speedlimit", SpeedLimit);
		jo.put("weather", weatherConditions);
		jo.put("co2", totalContamination);
		
		JSONArray jo_aux = new JSONArray();
		for (Vehicle veh: vehicles) {
			jo_aux.put(veh.getId());	
		}	
		
		jo.put("vehicles", jo_aux);

		return jo;
	}

	public Junction getSource() {
		return this.source;
	}

	public Junction getDestination() {
		return this.destination;
	}

	public int getLength() {
		return this.length;
	}

	public double getTotalCO2() {
		
		return totalContamination;
	}

	public double getCO2Limit() {
		return contaminationAlarmLimit;
	}

	public int getMaximumSpeed() {
		return maximumSpeed;
	}
	
	public int getSpeedLimit() {
		return SpeedLimit;
	}	

	public Weather getWeatherConditions() {
		return weatherConditions;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

}
