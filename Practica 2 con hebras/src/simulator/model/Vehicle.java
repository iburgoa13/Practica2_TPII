package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import exception.JunctionException;
import exception.RoadException;
import exception.VehicleException;

public class Vehicle extends SimulatedObject {

    protected List<Junction> _itinerary;
    protected int _maxSpeed;
    protected int _actualSpeed;
    protected VehicleStatus _status;
    protected Road _road;
    protected int _location;
    protected int _classCont;
   
    protected int _totalCont;
    protected int _totalDistance;
    //ultimo cruce visitado
    protected int _junction;
    //clase protected
    Vehicle(String id, int m, int c, List<Junction> iti) throws VehicleException {

        super(id);
        if(m < 0){
            throw new VehicleException("La velocidad debe de ser positiva");

        }
        if(c < 0 || c > 10) throw  new VehicleException("El valor de la contaminacion debe ser entre 0 y 10 incluidos");
        if(iti.size()<2)throw new VehicleException("El itinerario debe tener dos cruces minimo");
        _maxSpeed = m;
        _classCont = c;
        _location = 0;
        _road = null;
        _status = VehicleStatus.PENDING;
        _actualSpeed = 0;
        _totalCont = 0;
        _totalDistance = 0;
        _junction = 0;
        _itinerary = Collections.unmodifiableList(new ArrayList<>(iti));


    }

    public int get_maxSpeed() {
        return _maxSpeed;
    }

    public int get_actualSpeed() {
        return _actualSpeed;
    }

    public int get_totalCont() {
        return _totalCont;
    }

    public int get_totalDistance() {
        return _totalDistance;
    }
    public String getLocationGUI(){
        if(_status == VehicleStatus.PENDING) return "Pending";
        else if(_status == VehicleStatus.TRAVELING){
            return _road._id+":"+_location;
        }
        else if(_status == VehicleStatus.WAITING){
            return "Waiting:"+_itinerary.get(_junction)._id;
        }
        else
         return "Arrived";
    }
    public String getIti(){
        String iti = "[";
        for(int i = 0; i < _itinerary.size()-1;i++){
            iti+= _itinerary.get(i)._id+",";
        }
        iti+= _itinerary.get(_itinerary.size()-1)+"]";
        return iti;
    }
    void setSpeed(int s) throws VehicleException {
        if(s<0)throw new VehicleException("Valor de velocidad negativo");
        _actualSpeed = Math.min(s,_maxSpeed);

    }

    void setContaminationClass(int c) throws VehicleException {
        if(c < 0 || c >10)throw new VehicleException("El valor de la contaminacion debe estar entre 0 y 10");
        _classCont = c;
    }
    void moveToNextRoad() throws JunctionException, RoadException, VehicleException {
        if(_status != VehicleStatus.PENDING && _status != VehicleStatus.WAITING){
            throw new VehicleException("movetonextroad fallo aqui");
        }
        else{
            if(_road!=null)_road.exit(this);
            _location = 0;
            _actualSpeed = 0;
            if(_junction != _itinerary.size()-1){
                Junction src = _itinerary.get(_junction);
                Junction dest = _itinerary.get(_junction+1);
                _junction++;
                Road r = src.roadTo(dest);
                _road = r;
                r.enter(this);
                _status = VehicleStatus.TRAVELING;
            }
            else {
                _status = VehicleStatus.ARRIVED;
                _road = null;
            }

        }

    }
    @Override
    void advance(int time) throws RoadException {

        int locationAnt = _location;
        _location = Math.min(_location + _actualSpeed, _road._longitud);

        int f,c,l;
        f = _classCont;
        l = Math.abs(_location - locationAnt);
        c = f*l;
        _totalCont+=c;
        _road.addContamination(c);
        _totalDistance += l;
        if(_location >= _road._longitud){
            _status = VehicleStatus.WAITING;
            _actualSpeed = 0;

            _road._dest.enter(this);

        }
    }

    @Override
    public JSONObject report() {
        JSONObject obj = new JSONObject();
        obj.put("id",_id);
        obj.put("speed",_actualSpeed);
        obj.put("distance",_totalDistance);
        obj.put("co2",_totalCont);
        obj.put("class",_classCont);
        obj.put("status",_status.toString());
        obj.put("road",_road);
        if(_road!= null)obj.put("location",_location);
        return obj;

    }
    public VehicleStatus getStatus(){
        return _status;
    }
    public Road getRoad(){
        return _road;
    }
    public int getLocation(){return _location;}
    public int getContClass(){
        return _classCont;
    }
}

