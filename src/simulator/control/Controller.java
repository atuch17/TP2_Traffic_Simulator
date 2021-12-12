package simulator.control;

import simulator.model.Event;
import simulator.model.TrafficSimObserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import exceptions.NegativeException;
import exceptions.StatusException;
import simulator.factories.Factory;
import simulator.model.TrafficSimulator;

public class Controller {
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws NegativeException {
		if (sim == null || eventsFactory == null)
			throw new NegativeException("Problemas en la constructora del controlador");
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}

	public void loadEvents(InputStream in) throws JSONException, NegativeException {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray j_Arr = jo.getJSONArray("events");

		for (int i = 0; i < j_Arr.length(); i++) { // no hace falta
			sim.addEvent(eventsFactory.createInstance(j_Arr.getJSONObject(i)));
		}

	}

	public void run(int n, OutputStream out) throws NegativeException, StatusException {
		int i = 0;
		int k = 0;

		if (out != null) {// Para esta practica ya no hace falta imprimirlo xd
			PrintStream p = new PrintStream(out);

			p.print("{");
			p.println(" \"states\": [ ");

			while (i < n - 1) { // n-1
				sim.advance();
				p.print(sim.report());
				p.println(",");
				i++;
				if (i == 141) {
					k = 0;
				}
			}
			if (i == n - 1) { // n-1
				sim.advance();
				p.println(sim.report());
			}

			p.println("]}");

		} else {
			sim.advance();
		}
	}

	// NEW
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}

	void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}

	public void addEvent(Event e) {
		sim.addEvent(e);
	}

	public void reset() {
		sim.reset();
	}
}
