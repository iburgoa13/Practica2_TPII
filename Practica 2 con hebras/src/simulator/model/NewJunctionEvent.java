package simulator.model;

import exception.*;

public class NewJunctionEvent extends Event{

    protected String _id;
    protected LightSwitchingStrategy _ls;
    protected DequeuingStrategy _dq;
    protected int _x,_y;
    public NewJunctionEvent(int time, String id,LightSwitchingStrategy ls,
                            DequeuingStrategy dq, int x, int y){
        super(time);
        _id = id;
        _ls = ls;
        _dq = dq;
        _x = x;
        _y = y;
    }
	@Override
	void execute(RoadMap map) throws RoadException, JunctionException, RoadMapException {
		Junction junction = new Junction(_id,_ls,_dq,_x,_y);
        map.addJunction(junction);
	}

    @Override
    public String toString(){
        return "New Junction '"+_id+"'";
    }
}
