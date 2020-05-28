package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.scene.Scene;
import org.json.JSONObject;

import exception.RoadException;
import exception.VehicleException;
import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject {
    protected Junction _src,_dest;
    protected int _longitud;
    protected int _maxSpeed;
    protected int _limitSpeed;
    protected int _limitCont;
    protected int _totalCont;
    protected Weather _weather;
    protected List<Vehicle> _vehicles;
    Road(String  id,Junction src,Junction d,int m,int c,int l, Weather w) throws RoadException {

        super(id);
        if(m<0) throw new RoadException("velocidad negativa");
        if(c<0) throw new RoadException("valor de contaminacion negativo");
        if(l < 0)throw new RoadException("valor de la longitud negativa");
        if(src == null) throw new RoadException("Junction de inicio nulo");
        if(d == null) throw new RoadException("Junction destino nulo");
        if(w == null) throw new RoadException("Weather null");
        _src = src;
        _dest = d;
        _longitud = l;
        _maxSpeed = m;
        _limitSpeed = m;
        _limitCont = c;
        _totalCont = 0;
        _weather = w;
        _vehicles = new SortedArrayList<>(new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle o1, Vehicle o2) {
                return o2._location - o1._location;
            	/*if(o1._location > o2._location)return -1;
            	if(o2._location > o1._location)return 1;
            	else return 0;*/
            }
        });
    }

    void enter(Vehicle v) throws RoadException {
        if(v._location==0 && v._actualSpeed==0){
            _vehicles.add(v);
            // _src.enter(v);
        }
        else throw new RoadException("location y/o velocidad incorrectas");
    }
    void exit(Vehicle v){
        _vehicles.remove(v);
    }
    void setWeather(Weather w) throws RoadException {
        if(w==null)throw new RoadException("Weather null");
        _weather = w;
    }
    void addContamination(int c) throws RoadException {
        if(c<0)throw new RoadException("valor de contaminacion negativo");
        _totalCont += c;
    }
    abstract void reduceTotalContamination();
    abstract void updateSpeedLimit();
    abstract int calculateVehicleSpeed(Vehicle v);

    public List<Vehicle> getVehicles(){
        return _vehicles;
    }

    @Override
    void advance(int time) throws VehicleException, RoadException {
        reduceTotalContamination();
        updateSpeedLimit();
        for(Vehicle v: _vehicles){
            int s = calculateVehicleSpeed(v);
            v.setSpeed(s);
            v.advance(time);
        }
    }

    @Override
    public JSONObject report() {
        JSONObject obj = new JSONObject();
        obj.put("id",_id);
        obj.put("speedlimit",_limitSpeed);
        obj.put("co2",_totalCont);
        obj.put("weather",_weather.toString());
        List<Vehicle> _idV = Collections.unmodifiableList(_vehicles);
        List<String> _v = new ArrayList<>();
        for (Vehicle vehicle : _idV) {
            _v.add(vehicle.getId());
        }
        obj.put("vehicles", _v);
        return obj;
    }

    public Junction  getSrc(){
        return _src;
    }
    public Junction getDest(){return _dest;}
    public int getTotalCO2(){
        return _totalCont;
    }
    public int getCO2Limit(){
        return _limitCont;
    }
    public int getLength(){
        return _longitud;
    }
    public String getWeather(){
        return _weather.toString();
    }
    public int get_maxSpeed() {return _maxSpeed;}

    public int get_limitSpeed() {
        return _limitSpeed;
    }

}

