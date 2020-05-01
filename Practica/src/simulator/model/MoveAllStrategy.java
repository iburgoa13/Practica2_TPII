package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
        List<Vehicle> v = new ArrayList<>();
        Iterator<Vehicle> it = q.iterator();
        while(it.hasNext()){
            v.add(it.next());
        }
        return v;
        /* List<Vehicle> v = new SortedArrayList<>(new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle o1, Vehicle o2) {
                return o1._location - o2._location;
            }
        });

        v = q;*/
        //return Collections.unmodifiableList(q);

	}

}

