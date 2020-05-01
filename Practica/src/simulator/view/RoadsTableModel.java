package simulator.view;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {
    private String[] nomColR= {"Id", "Length","Weather","Max.Speed","Speed Limit","Total CO2","CO2 Limit"};
    private List<Road> _roads;

    public RoadsTableModel(Controller controller){
        _roads = null;
        controller.addObserver(this);
    }
    @Override
    public String getColumnName(int c){
        return nomColR[c];
    }
    @Override
    public int getRowCount() {
        return (_roads==null) ? 0 : _roads.size();
    }

    @Override
    public int getColumnCount() {
        return nomColR.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object r = null;
        switch (columnIndex){
            case 0 : r = _roads.get(rowIndex).getId();break;
            case 1 : r = _roads.get(rowIndex).getLength();break;
            case 2 : r = _roads.get(rowIndex).getWeather();break;
            case 3 : r = _roads.get(rowIndex).get_maxSpeed();break;
            case 4 : r = _roads.get(rowIndex).get_limitSpeed();break;
            case 5 : r = _roads.get(rowIndex).getTotalCO2();break;
            case 6 : r = _roads.get(rowIndex).getCO2Limit();break;
        }
        return r;
    }

    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
        _roads = new ArrayList<>();
        for(Road r : map.getRoads()) {
            _roads.add(r);
        }
        fireTableStructureChanged();
    }

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        _roads = new ArrayList<>();
        for(Road r : map.getRoads()) {
            _roads.add(r);
        }
        fireTableStructureChanged();

    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {

    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        _roads = null;
        fireTableStructureChanged();
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onError(String err) {

    }
}
