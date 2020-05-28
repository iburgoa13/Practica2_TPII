package simulator.model;

import java.util.List;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import exception.*;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
    protected List<Pair<String,Integer>> _cs;
 
   

    public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) throws EventException {
        super(time);
        if(cs==null)throw new EventException("cs es nulo");
        _cs = cs;
    }



	@Override
	void execute(RoadMap map) throws RoadException, JunctionException, RoadMapException, VehicleException, EventException {
		for(Pair<String ,Integer> a : _cs){
	           Vehicle v = map.getVehicle(a.getFirst());
	           if(v == null){
	               throw new EventException("El vehiculo no existe");
	           }
	           v.setContaminationClass(a.getSecond());

	        }

	}
	@Override
	public String toString(){
    	String co2="";
		for(int i = 0; i < _cs.size()-1;i++){
			String r = _cs.get(i).getFirst();
			Integer w = _cs.get(i).getSecond();
			co2+= "("+r+","+w+"),";
		}
		String r = _cs.get(_cs.size()-1).getFirst();
		Integer w = _cs.get(_cs.size()-1).getSecond();
		co2+= "("+r+","+w+")";
		return "Change CO2 class: ["+co2+"]";

	}
}

