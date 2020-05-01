package simulator.model;

import exception.*;

public class NewInterCityRoadEvent extends NewRoadEvent {

	public NewInterCityRoadEvent(int time, String id, String src, String d, int l, int co2l, int m, Weather w) {
		super(time, id, src, d, l, co2l, m, w);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cargaRoad(RoadMap map) throws RoadException, JunctionException, RoadMapException {
		Road r = null;
		r = new InterCityRoad(_id,s,d,_max,_co2,_l,_w);
		s.addOutGoingRoad(r);
		d.addIncommingRoad(r);
		map.addRoad(r);
	}


	@Override
	public String toString(){
		return "New InterCityRoad '"+_id+"'";
	}
}

