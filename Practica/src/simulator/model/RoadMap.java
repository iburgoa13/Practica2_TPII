package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import exception.RoadMapException;

public class RoadMap {

    protected List<Junction> _junctionList;
    protected List<Road> _roadList;
    protected List<Vehicle> _vehicleList;
    protected Map<String,Junction> _junctionMap;
    protected Map<String,Road> _roadMap;
    protected Map<String,Vehicle> _vehicleMap;
    
    RoadMap(){
        _junctionList = new ArrayList<>();
        _roadList = new ArrayList<>();
        _vehicleList = new ArrayList<>();
        _junctionMap = new HashMap<>();
        _roadMap = new HashMap<>();
        _vehicleMap = new HashMap<>();
    }
    public List<Vehicle> getVehilces(){
        return _vehicleList;
    }
    void addJunction(Junction j) throws RoadMapException {
        if(_junctionMap.containsKey(j.getId())){
            throw new RoadMapException("Junction ya existe");
        }
        _junctionList.add(j);
        _junctionMap.put(j.getId(),j);
    }

    void addRoad(Road r) throws RoadMapException {
        if(_roadMap.containsKey(r.getId())){
            throw new RoadMapException("Road ya existe");
        }
        if(!_junctionMap.containsKey(r._src.getId()) || !_junctionMap.containsKey(r._dest.getId())){
            throw new RoadMapException("Junction no existen");
        }
        _roadList.add(r);
        _roadMap.put(r.getId(),r);
    }

    void addVehicle(Vehicle v) throws RoadMapException {
        for(int i = 0; i < v._itinerary.size()-1;i++){
            Junction ori = v._itinerary.get(i);
            Junction dest = v._itinerary.get(i+1);
            Road r = ori.roadTo(dest);
            
            if(r==null) throw new RoadMapException("La carretera entre los cruces no existe");
        }
        if(_vehicleMap.containsKey(v.getId())) throw new RoadMapException("El vehiculo " + v.getId() +" ya existe");
        _vehicleMap.put(v.getId(),v);
        _vehicleList.add(v);
    }

    public Junction getJunction(String id){
        return _junctionMap.get(id);
    }
    public Road getRoad(String id){
        return _roadMap.get(id);
    }

    public Vehicle getVehicle(String id){
        return _vehicleMap.get(id);
    }

    public List<Junction> getJunctions(){
        return Collections.unmodifiableList(_junctionList);
    }

    public List<Road>getRoads(){
        return Collections.unmodifiableList(_roadList);
    }

    public List<Vehicle>getVehicles(){
        return Collections.unmodifiableList(_vehicleList);
    }

    void reset(){
        _vehicleMap.clear();
        _roadMap.clear();
        _junctionMap.clear();
        _vehicleList.clear();
        _junctionList.clear();
        _roadList.clear();
    }

    public JSONObject report(){
        JSONObject obj = new JSONObject();
        JSONArray junction = new JSONArray();
        JSONArray road = new JSONArray();
        JSONArray vehicle = new JSONArray();
        for(int i = 0; i < _junctionList.size();i++){
            junction.put(i,_junctionList.get(i).report());
        }
        obj.put("junctions",junction);
        for(int i = 0; i < _roadList.size();i++){
            road.put(i,_roadList.get(i).report());
        }
        obj.put("roads",road);

        for(int i = 0; i < _vehicleList.size();i++){
            vehicle.put(i,_vehicleList.get(i).report());
        }
        obj.put("vehicles",vehicle);
        return obj;
    }


}

