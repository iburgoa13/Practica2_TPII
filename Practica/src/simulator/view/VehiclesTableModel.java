package simulator.view;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {
    private String[] nomColV ={"Id","Location","Itinerary", "CO2 Class","Max. Speed","Speed", "Total CO2", "Distance"};
    private List<Vehicle> _vehicles;

    public VehiclesTableModel(Controller _control){
        _vehicles = null;
        _control.addObserver(this);
    }
    @Override
    public int getRowCount() {
        return (_vehicles== null) ? 0 : _vehicles.size();
    }

    @Override
    public String getColumnName(int c){
        return nomColV[c];
    }
    @Override
    public int getColumnCount() {
        return nomColV.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object v = null;
        switch (columnIndex){
            case 0 : v = _vehicles.get(rowIndex).getId();
            break;
            case 1 : v = _vehicles.get(rowIndex).getLocationGUI();break;
            case 2 : v = _vehicles.get(rowIndex).getIti();break;
            case 3 : v = _vehicles.get(rowIndex).getContClass();break;
            case 4 : v = _vehicles.get(rowIndex).get_maxSpeed();break;
            case 5 : v = _vehicles.get(rowIndex).get_actualSpeed();break;
            case 6 : v = _vehicles.get(rowIndex).get_totalCont();break;
            case 7 : v = _vehicles.get(rowIndex).get_totalDistance();break;
        }
        return v;
    }

    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
        this._vehicles = new ArrayList<>();
        for(Vehicle v: map.getVehicles()){
            _vehicles.add(v);
        }
        fireTableStructureChanged();
    }

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        this._vehicles = new ArrayList<>();
        for(Vehicle v: map.getVehicles()){
            _vehicles.add(v);
        }
        fireTableStructureChanged();
    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        _vehicles = null;
        fireTableStructureChanged();
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onError(String err) {

    }
}
