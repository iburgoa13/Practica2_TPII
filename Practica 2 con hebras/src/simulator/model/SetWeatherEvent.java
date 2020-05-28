package simulator.model;

import java.util.List;

import exception.*;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event{

    List<Pair<String ,Weather>> _ws;
    public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws EventException {
        super(time);
        if(ws == null) throw new EventException("ws es nulo");
        _ws = ws;
    }
	@Override
	void execute(RoadMap map)
			throws RoadException, JunctionException, RoadMapException, VehicleException, EventException {

        for(Pair<String ,Weather> a : _ws){
            Road r = map.getRoad(a.getFirst());
            if(r == null){
                throw new EventException("lA CARRETERA NO EXISTE");
            }
            r.setWeather(a.getSecond());
        }

	}
    @Override
    public String toString(){
        String ww="";
        for(int i = 0; i < _ws.size()-1;i++){
            String r = _ws.get(i).getFirst();
            String w = _ws.get(i).getSecond().toString();
            ww+= "("+r+","+w+"),";
        }
        String r = _ws.get(_ws.size()-1).getFirst();
        String w = _ws.get(_ws.size()-1).getSecond().toString();
        ww+= "("+r+","+w+")";
        return "Change Weather: ["+ww+"]";

    }

}

