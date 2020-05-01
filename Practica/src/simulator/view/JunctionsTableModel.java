package simulator.view;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
    private String [] nomColJ = {"Id","Green","Queues"};
    private List<Junction> _junctions;
    public JunctionsTableModel(Controller c){
        _junctions = null;
        c.addObserver(this);
    }
    @Override
    public String getColumnName(int c){
        return nomColJ[c];
    }
    @Override
    public int getRowCount() {
        return (_junctions==null) ? 0 : _junctions.size();
    }

    @Override
    public int getColumnCount() {
        return nomColJ.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object j = null;
        switch (columnIndex){
            case 0 : j = _junctions.get(rowIndex).getId();break;
            case 1 : j = _junctions.get(rowIndex).getCarreteraEntranteEnVerde();break;
            case 2 : j = _junctions.get(rowIndex).getVehiculosEnCarreterasEntrantes();break;
        }
        return j;
    }

    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
        _junctions = new ArrayList<>();
        for(Junction j : map.getJunctions()){
            _junctions.add(j);
        }
        fireTableStructureChanged();
    }

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        _junctions = new ArrayList<>();
        for(Junction j : map.getJunctions()){
            _junctions.add(j);
        }
        fireTableStructureChanged();
    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {

    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        _junctions = null;
        fireTableStructureChanged();
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onError(String err) {

    }
}
