package simulator.view;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {
    private String [] colName = {"Time", "Desc"};
    private List<Event> _events;
    public EventsTableModel(Controller _control){
        _events = null;
        _control.addObserver(this);
    }
    @Override
    public int getRowCount() {
        return (_events == null) ? 0 : _events.size();
    }

    @Override
    public String getColumnName(int c){
        return colName[c];
    }
    @Override
    public int getColumnCount() {
        return colName.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object s = null;
        switch (columnIndex){
            case 0 : s = _events.get(rowIndex).getTime();
            break;
            case 1 : s = _events.get(rowIndex).toString();
            break;
        }
        return s;
    }

    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
        fireTableDataChanged();
    }

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        fireTableDataChanged();
    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        _events = events;
        fireTableDataChanged();
    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        _events = events;
        fireTableDataChanged();
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {
        _events = events;
        fireTableDataChanged();
    }

    @Override
    public void onError(String err) {

    }
}
