package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.NegativeException;

public class RoadMap {

	private List<Junction> listaCruces;
	private List<Road> listaCarreteras;
	private List<Vehicle> listaVehiculos;
	private Map<String, Junction> mapaCruces;
	private Map<String, Road> mapaCarreteras;
	private Map<String, Vehicle> mapaVehiculos;

	RoadMap() {
		listaCruces = new ArrayList<Junction>();
		listaCarreteras = new ArrayList<Road>();
		listaVehiculos = new ArrayList<Vehicle>();

		mapaCruces = new HashMap<String, Junction>();
		mapaCarreteras = new HashMap<String, Road>();
		mapaVehiculos = new HashMap<String, Vehicle>();

	}

	void addJuction(Junction j) throws NegativeException {
		if (!mapaCruces.containsKey(j.getId())) {
			listaCruces.add(j);
			mapaCruces.put(j.getId(), j);
		} else {

			throw new NegativeException("El cruce no debe ser null");
		}
	}

	void addRoad(Road r) throws NegativeException {

		if (!mapaCarreteras.containsKey(r.getId()) && this.mapaCruces.containsKey(r.getDestination().getId())
				&& this.mapaCruces.containsKey(r.getSource().getId())) {
			listaCarreteras.add(r);
			mapaCarreteras.put(r.getId(), r);
		} else {
			throw new NegativeException("El mapa Carreteras no tiene ese road");
		}

	}

	void addVehicle(Vehicle v) throws NegativeException { // contain
		if (mapaVehiculos.containsKey(v.getId())) {
			throw new NegativeException("El Vehiculo no debe ser null");
		}
		List<Junction> intineraio = new ArrayList<Junction>();
		int i = 0;
		while (i < intineraio.size() - 1) {
			if (intineraio.get(i).roadTo(intineraio.get(i + 1)) == null) {
				throw new NegativeException("Excepcion al añadir vehiculo en RoadMap");
			}
			i++;
		}
		listaVehiculos.add(v);
		mapaVehiculos.put(v.getId(), v);

	}

	public Junction getJunction(String id) {

		return mapaCruces.get(id);

	}

	public Road getRoad(String id) {
		if (mapaCarreteras.containsKey(id))
			return mapaCarreteras.get(id);
		else
			return null;
	}

	public Vehicle getVehicle(String id) {
		if (mapaVehiculos.containsKey(id))
			return mapaVehiculos.get(id);
		else
			return null;
	}

	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(listaCruces);
	}

	public List<Road> getRoads() {
		return Collections.unmodifiableList(listaCarreteras);
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(listaVehiculos);
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray ja1 = new JSONArray();
		JSONArray ja2 = new JSONArray();
		JSONArray ja3 = new JSONArray();

		// 2
		for (int i = 0; i < listaCarreteras.size(); i++) {
			ja2.put(listaCarreteras.get(i).report());
		}
		jo.put("roads", ja2);

		// 3
		for (int i = 0; i < listaVehiculos.size(); i++) {
			ja3.put(listaVehiculos.get(i).report());
		}
		jo.put("vehicles", ja3);

		// 1
		for (int i = 0; i < listaCruces.size(); i++) {
			ja1.put(listaCruces.get(i).report());
		}
		jo.put("junctions", ja1);

		return jo;
	}

	void reset() {

		listaCruces.clear();
		listaVehiculos.clear();
		listaCarreteras.clear();

		mapaCarreteras.clear();
		mapaCruces.clear();
		mapaVehiculos.clear();
	}
}
