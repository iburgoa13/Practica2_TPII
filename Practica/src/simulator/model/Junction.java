package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import exception.JunctionException;
import exception.RoadException;
import exception.VehicleException;

public class Junction extends SimulatedObject {
    //lista de carreteras que entran al cruce, el CRUCE ES EL DESTINO
    //YO COMO CRUCE ME VIENE UNA CARRETERA QUE YO PARA EL SOY SU DEST
    protected List<Road> _entrantes;
    /*
    Map(j,r) donde j es el cruce al cual YO CRUCE estoy conectado junto
    a la carretera r, es decir yo para r soy un origen
    se usa para ver que carretera usar para llegar a j
     */
    protected Map<Junction,Road> _mapCarreteraSaliente;

    /*
    lista de colas para las carreteras entrantes
    la cola iesima (que es List<Vehicle>) es la iesima carretera de
    la lista de carreteras _entrantes
    entonces
    _entrantes(2) quiere decir que la cola de el sera queue(2) ahi
    guarda sus coches
     */
    protected List<List<Vehicle>> _queue;
    protected Map<Road,List<Vehicle>> _mapQueue;
    protected int _currGreen;
    protected int _last;
    protected LightSwitchingStrategy _ls;
    protected DequeuingStrategy _dq;
    protected int _x,_y;
    Junction(String id, LightSwitchingStrategy ls, DequeuingStrategy dq,
             int x, int y) throws JunctionException {
        super(id);
        if(ls == null)throw new JunctionException("LS NULL");
        if(dq == null) throw new JunctionException("DQ NULL");
        if(x < 0) throw new JunctionException("coordenada x negativa");
        if(y < 0) throw new JunctionException("coordenada y negativa");
        _x = x;
        _y = y;
        _ls = ls;
        _dq = dq;
        _currGreen = -1 ;//rojo
        _last = 0;
        _entrantes = new ArrayList<>();
        _mapCarreteraSaliente = new HashMap<>();
        _queue = new ArrayList<>();
        _mapQueue = new HashMap<>();
    }


    void addIncommingRoad(Road r) throws JunctionException {
        //compruebo si su destino soy yo
        if(r._dest == this) {
            _entrantes.add(r);
            List<Vehicle> p = new LinkedList<>();
            _queue.add(p);
            _mapQueue.put(r, p);
        }
        else throw new JunctionException("El cruce destino no soy yo");
    }
    void addOutGoingRoad(Road r) throws JunctionException {
        //para comprobar que es una entrante YO SOY SRC
        if(r._src == this && !_mapCarreteraSaliente.containsKey(r._dest)){
            _mapCarreteraSaliente.put(r._dest,r);

        }
        else throw new JunctionException("El cruce origen no soy yo");
    }

    void enter(Vehicle v){


        List<Vehicle> x = _mapQueue.get(v._road);

        if(x!=null && !x.contains(v)) x.add(v);

    }

    public List<Road> getInRoads(){
        return _entrantes;
    }
    public int getGreenLightIndex(){return _currGreen;}
    public int getX(){return _x;}
    public int getY(){return _y;}
    Road roadTo(Junction j){
        return _mapCarreteraSaliente.get(j);
    }
    @Override
    void advance(int time) throws VehicleException, RoadException, JunctionException {
        List<List<Vehicle>>p = new ArrayList<>();
        /*
        me creo una lista de colas
         */
        if(_currGreen!=-1){
            //Road r = this._entrantes.get(_currGreen);
            p.add(_dq.dequeue(_queue.get(_currGreen)));

        }


       /* for(int i = 0; i < _queue.size();i++){
             p.add(_dq.dequeue(_queue.get(i)));
        }/*
        inserto en p las colas que tengan que moverse
        despues elimino esos vehiculos de las colas
        */


        for (List<Vehicle> vehicles : _queue) {
            if (!vehicles.isEmpty()) {
                for (int j = 0; j < p.get(0).size(); j++) {
                    p.get(0).get(j).moveToNextRoad();
                    vehicles.remove(p.get(0).get(j));
                }
            }
        }
      
        int currGreen = _ls.chooseNextGreen(_entrantes,_queue,_currGreen,_last,time);
        if(currGreen != _currGreen){
            _currGreen = currGreen;
            _last = time;
        }
    }
    List<String> getIDsVehicles(int ind){
        List<String> aux = new ArrayList<>();
        for(int i = 0; i < _queue.get(ind).size();i++){
            aux.add(_queue.get(ind).get(i).getId());
        }
        return aux;
    }
    @Override
    public JSONObject report() {
        JSONObject obj = new JSONObject();
        obj.put("id",_id);
        if(_currGreen == -1) obj.put("green","none");
        else obj.put("green",_entrantes.get(_currGreen).getId());
        JSONArray array = new JSONArray();




        for(int i = 0; i < _entrantes.size();i++){
            JSONObject colas = new JSONObject();
            colas.put("road",_entrantes.get(i));
            colas.put("vehicles",getIDsVehicles(i));
            array.put(colas);
        }
        obj.put("queues",array);
        return obj;
    }
    public String getCarreteraEntranteEnVerde(){
        if(_currGreen==-1) return "NONE";
        else return _entrantes.get(_currGreen)._id;
    }
    public String getVehiculosEnCarreterasEntrantes(){
        if(_entrantes.isEmpty()) return "";
        String road ="";
        for(int i = 0; i < _entrantes.size();i++){
            road+= _entrantes.get(i)._id+"[";
            List<String> v = getIDsVehicles(i);
            for(int j = 0; j < v.size();j++){
                if(j != v.size()-1){
                    road+=v.get(j)+",";
                }
                else{
                    road+=v.get(j);
                }
            }
            road+="] ";
        }
        return road;
    }
}

