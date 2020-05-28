package simulator.view;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatusBar extends JPanel implements TrafficSimObserver {
    private JLabel _newEvents;
    private JLabel _time;
    public StatusBar(Controller c){
        init();
        c.addObserver(this);
    }
    private void init(){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createBevelBorder(1));
        _time = new JLabel();
        _newEvents = new JLabel();
        _newEvents.setLocation(100,100);
        add(_time);
        //lo uso para separar el time del evento
        add(Box.createRigidArea(new Dimension(50,5)));


        add(_newEvents);
    }
    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _time.setText("Time: "+ time);
                _newEvents.setText("");
            }
        });
    }
    
    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _time.setText("Time: "+ time);
                _newEvents.setText("");
            }
        });
    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _time.setText("Time: "+ time);
                _newEvents.setText("Event added("+ e.toString()+")");
            }
        });
    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _time.setText("Time: "+ time);
                _newEvents.setText("Welcome");
            }
        });
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _time.setText("Time: "+ time);
                _newEvents.setText("Welcome");
            }
        });
    }

    @Override
    public void onError(String err) {

    }
}
