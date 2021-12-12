package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int timeSlot;

	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if (roads.isEmpty())
			return -1;

		if (currGreen == -1) {
			return busquedaCircular(qs, 0);
		}

		if (currTime - lastSwitchingTime < timeSlot) {
			return currGreen;
		}else {
			return busquedaCircular(qs, currGreen+1);
		}
	}

	public int busquedaCircular(List<List<Vehicle>> qs, int start) { 
		int ind=0;
		int i = (start+1)%qs.size();
		int cola = 0;
		
		while (i != start) {
			if (qs.get(i).size() > cola) {
				cola = qs.get(i).size();
				ind = i;
			}
			i= (1+i)% qs.size();
		}

		return ind;
	}

}
