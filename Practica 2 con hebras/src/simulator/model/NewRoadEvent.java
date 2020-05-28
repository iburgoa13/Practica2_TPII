package simulator.model;

import exception.JunctionException;
import exception.RoadException;
import exception.RoadMapException;

public abstract class NewRoadEvent extends Event{

    protected String _id;
    protected String _src;
    protected String  _dest;
    protected int _l;
    protected int _co2;
    protected int _max;
    protected Weather _w;
    protected Junction s,d;
    public NewRoadEvent(int time, String id,String src,
                                 String d, int l, int co2l,int m,Weather w) {
        super(time);
        _id = id;
        _src = src;
        _dest = d;
        _l = l;
        _co2 = co2l;
        _max = m;
        _w = w;
    }
    
	@Override
	void execute(RoadMap map) throws RoadException, JunctionException, RoadMapException {
		s = map._junctionMap.get(_src);
		d = map._junctionMap.get(_dest);
		cargaRoad(map);
	}

	public abstract void cargaRoad(RoadMap map) throws RoadException, JunctionException, RoadMapException;

}

