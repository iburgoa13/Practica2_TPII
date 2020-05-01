package simulator.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import exception.*;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{

    protected RoadMap _roadMap;
    protected List<Event> _events;
    protected int _time;
    protected List<TrafficSimObserver> _observer;
    public TrafficSimulator(){
        _time = 0;
        _roadMap = new RoadMap();
        _observer = new ArrayList<>();
        _events = new SortedArrayList<>(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2)
            {
            	//if(o1._time < o2._time)return 1;
            	//if(o1._time==o2._time)return 0;
            	//return -1;
                return o1._time - o2._time;
            }
        });
    }

    public RoadMap get_roadMap(){
        return _roadMap;
    }


    public void addEvent(Event e){
        _events.add(e);
        for(TrafficSimObserver o : _observer){
            o.onEventAdded(_roadMap,_events,e,_time);
        }
    }
    public void advance() throws TrafficSimulatorException {
        _time++;
        for(TrafficSimObserver o : _observer){
            o.onAdvanceStart(_roadMap,_events,_time);
        }
        List<Event> borrar = new ArrayList<>();
        try {
            for (Event e : _events) {
                if (e.getTime() == _time) {
                    e.execute(_roadMap);
                    borrar.add(e);
                }
            }
            for (Event event : borrar) {
                _events.remove(event);
            }
            for (Junction j : _roadMap._junctionList) {
                j.advance(_time);
            }
            for (Road r : _roadMap._roadList) {
                r.advance(_time);
            }
        }
        catch (JunctionException | RoadMapException |RoadException | EventException |VehicleException e){
            for(TrafficSimObserver o : _observer){
                o.onError(e.getMessage());
                throw new TrafficSimulatorException(e.getMessage());
            }
        }

        for(TrafficSimObserver o : _observer){
            o.onAdvanceEnd(_roadMap,_events,_time);
        }
    }
    public void reset(){
        _roadMap.reset();
        _events.clear();
        _time = 0;
        for(TrafficSimObserver o : _observer){
            o.onReset(_roadMap,_events,_time);
        }
    }

    public JSONObject report(){
        JSONObject obj = new JSONObject();
        obj.put("time",_time);
        obj.put("state",_roadMap.report());
        return obj;
    }

    @Override
    public void addObserver(TrafficSimObserver o) {
        _observer.add(o);
        //uno de los dos es
      /*  for(TrafficSimObserver a : _observer){
            a.onRegister(_roadMap,_events,_time);
        }*/
        o.onRegister(_roadMap,_events,_time);
    }

    @Override
    public void removeObserver(TrafficSimObserver o) {
        _observer.remove(o);
    }

    public List<Vehicle> getMap() {
       return  _roadMap.getVehicles();
    }

    public int getTime() {
       return  _time;
    }
}

