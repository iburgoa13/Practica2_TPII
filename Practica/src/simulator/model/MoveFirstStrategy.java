package simulator.model;

import java.util.List;

import simulator.misc.SortedArrayList;

public class MoveFirstStrategy implements DequeuingStrategy{

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> v = new SortedArrayList<>();
		if(q.isEmpty())return v;
		v.add(q.get(0));
		return v;
	}

}

