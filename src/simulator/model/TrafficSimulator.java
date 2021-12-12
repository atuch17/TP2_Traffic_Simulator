package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import exceptions.NegativeException;
import exceptions.StatusException;
import simulator.misc.SortedArrayList;


public class TrafficSimulator implements Observable<TrafficSimObserver>{

	private RoadMap mapaCarreteras; // guarda todos los objetos de la simulacion
	private List<Event> listaEventos; // usar SortedArrayList
	private int tiempo;

	
	//private TrafficSimObserver t;

	//PARAMETROS NUEVOS
	private List<TrafficSimObserver> observerList;
	
	
	public TrafficSimulator() {
		super();
		this.mapaCarreteras = new RoadMap();
		this.listaEventos = new SortedArrayList<Event>();
		this.tiempo = 0;
		this.observerList = new ArrayList<TrafficSimObserver>();
	}

	public void addEvent(Event e) {
		listaEventos.add(e);
		//
		notifyOnEventAdded(e);
	}

	public void advance() throws NegativeException, StatusException {		
		// 1
		tiempo++;
		
		//
		notifyOnAdvanceStart();

		// 2

		while (listaEventos.size() > 0 && listaEventos.get(0).getTime() == this.tiempo) {
			listaEventos.remove(0).execute(mapaCarreteras);
		}

		// 3
		for (Junction cruces : mapaCarreteras.getJunctions()) {
			cruces.advance(tiempo);
		}
		// 4
		for (Road carreteras : mapaCarreteras.getRoads()) {
			carreteras.advance(tiempo);
			//TODO enun try & catch --_>thia.notifyeror..?
		}	
		
		//
		notifyOnAdvanceEnd();
	}

	public void reset() {
		tiempo = 0;
		mapaCarreteras.reset();
		listaEventos.clear();
		notifyOnReset();
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time", tiempo);
		jo.put("state", mapaCarreteras.report());
		return jo;
	}
	
	//PARTE NUEVA
	
	@Override
	public void addObserver(TrafficSimObserver o) {
		if(!observerList.contains(o))
			this.observerList.add(o);		
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		this.observerList.remove(o);		
	}	

	public void notifyOnAdvanceStart() {
		for(TrafficSimObserver ob:observerList)
			ob.onAdvanceStart(mapaCarreteras, listaEventos, tiempo);
	} 	

	public void notifyOnAdvanceEnd() {
		for(TrafficSimObserver ob:observerList)
			ob.onAdvanceEnd(mapaCarreteras, listaEventos, tiempo);
	}

	public void notifyOnEventAdded(Event e) {
		for(TrafficSimObserver ob:observerList)
			ob.onEventAdded(mapaCarreteras, listaEventos, e, tiempo);
	}	

	public void notifyOnReset() {
		for(TrafficSimObserver ob:observerList)
			ob.onReset(mapaCarreteras, listaEventos, tiempo);
	}

	public void notifyOnError(String err) {	//TODO revisar xq esta mal, ver desde donde llamarla, y hacer el throw
		try {
			for(TrafficSimObserver ob:observerList)
				ob.onError(err);		
		}
		catch(Exception ex){
			//observerList.onError(err);
			notifyOnError(ex.getMessage());	//IGUAL AKI NO SE HACE EL TRY CATCH Y SE HACE EN LOS ADVANCE
		}
	}

}
