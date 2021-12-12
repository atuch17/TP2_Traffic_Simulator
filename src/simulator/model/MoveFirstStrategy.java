package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy{
	
	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List <Vehicle> q2 = new ArrayList<Vehicle>();
		if (q.size()>0)
			q2.add(q.get(0));
		return q2;
	}
}
