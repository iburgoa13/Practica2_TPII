package simulator.model;

import java.util.ArrayList;
import java.util.List;

import exception.*;

public class NewVehicleEvent extends Event{

    protected String _id;
    protected int _m;
    protected int _c;
    protected List<String> _iti;
    public NewVehicleEvent(int time, String id, int m, int c, List<String>iti) {
        super(time);
        _id = id;
        _m = m;
        _c = c;
        _iti = iti;

    }
	@Override
	void execute(RoadMap map)
			throws RoadException, JunctionException, RoadMapException, VehicleException, EventException {
		List<Junction> iti = new ArrayList<>();
        for (String s : _iti) {
            iti.add(map.getJunction(s));
        }
        Vehicle v = new Vehicle(_id,_m,_c,iti);

        map.addVehicle(v);
        v.moveToNextRoad();


	}

	@Override
    public String toString(){
        return "New Vehicle '"+_id+"'";
    }
}