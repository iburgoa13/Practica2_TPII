package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import exception.*;
import simulator.factories.Factory;
import simulator.misc.Pair;
import simulator.model.*;

public class Controller {
	protected TrafficSimulator _trafficSimulator;
    protected Factory<Event> _eventsFactory;

    public Controller(TrafficSimulator sim, Factory<Event> e) throws ControllerException {
        if(sim == null || e == null) throw new ControllerException("Valores nulos");
        _eventsFactory = e; 
        _trafficSimulator = sim;

    }

    public int get_time() {
        return _trafficSimulator.getTime();
    }

    public RoadMap get_roadMap(){
        return _trafficSimulator.get_roadMap();
    }


    public void loadEvents(InputStream in) throws EventException, ControllerException {
        JSONObject jo = new JSONObject(new JSONTokener(in));
        JSONArray array = jo.getJSONArray("events");
        if(array.isEmpty())throw new ControllerException("Entrada vacia");
        for(int i = 0; i < array.length();i++){
           Event e =  _eventsFactory.createInstance(array.getJSONObject(i));
           addEvent(e);
        }
    }

    //nuevo run añadido
    public void run(int n) throws TrafficSimulatorException {

            _trafficSimulator.advance();

    }
    public void run (int n, OutputStream out) throws TrafficSimulatorException {
        PrintStream p = new PrintStream(out);
        JSONObject states = new JSONObject();
        JSONArray array = new JSONArray();
        p.println("{");
        p.println(" \"states\": [");
        for(int i = 0; i < n; i++){
            _trafficSimulator.advance();
            p.print(_trafficSimulator.report());
            if(i!=n-1)p.print(",");
            p.print("\n");
        }
        p.println("]");
        p.println("}");
        states.put("states",array);

        
    }

    public void reset(){
        _trafficSimulator.reset();
    }
    public void addObserver(TrafficSimObserver o){
       _trafficSimulator.addObserver(o);
    }
    public void removeObserver(TrafficSimObserver o){
        _trafficSimulator.removeObserver(o);
    }
    public void addEvent(Event e){
        _trafficSimulator.addEvent(e);
    }

    public void createNewSetContClassEvent(Integer tick, List<Pair<String, Integer>> l) throws EventException {
        Event x = new NewSetContClassEvent(get_time()+tick, l);
        addEvent(x);
    }

    public void createSetWeatherEvent(int t, List<Pair<String, Weather>> l) throws EventException {
        SetWeatherEvent x = new SetWeatherEvent(get_time() +t,l);
        addEvent(x);
    }
}
