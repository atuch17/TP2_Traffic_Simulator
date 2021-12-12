package simulator.model;

import java.util.Iterator;
import java.util.List;

import exceptions.NegativeException;
import simulator.misc.Pair;


public class NewSetContClassEvent extends Event {
	private List<Pair<String, Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) throws NegativeException {
		super(time);
		if(cs==null) {
			throw new NegativeException("La lista cs de NewSetContClassEvent es null!");
		}
		this.cs=cs;
	}

	@Override
	void execute(RoadMap map) throws NegativeException {
		Iterator<Pair<String, Integer>> it = cs.iterator();
		
		while(it.hasNext()) {
			Pair<String, Integer> p = it.next();
			Vehicle veh = map.getVehicle(p.getFirst());
			if(veh==null)
				throw new NegativeException("El vehicluo no existe en el mapa de carreteras!");
			veh.setContaminationClass(p.getSecond());				
		}
	}
	
	@Override
	public String toString() {
		return "New Contamination Class '" + cs.toString() + "'"; // su id //SI?
	}
}
