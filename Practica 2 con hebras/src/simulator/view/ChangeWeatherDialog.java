package simulator.view;

import exception.EventException;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.Weather;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChangeWeatherDialog extends JDialog {
    private JButton _ok,_cancel;
    private Controller _c;
    private JComboBox<String> _road;
    private JComboBox<String> _weather;
    private JSpinner _ticks;

    public ChangeWeatherDialog(Controller c){
        _c = c;
        initWeather();
    }
    private void initWeather(){
        setTitle("Change Road Weather");
        setSize(400,400);
       setLocationRelativeTo(null);
        List<Road> _roads = new ArrayList<>();
        for(Road r : _c.get_roadMap().getRoads()){
            _roads.add(r);
        }
        JLabel label = new JLabel("Schedule an event to change the weather of a road" +
                "after a given a number of simulation ticks from now.");
        add(label, BorderLayout.NORTH);

        _ticks = new JSpinner(new SpinnerNumberModel(1,1,10000,1));
        _ticks.setToolTipText("Simulation ticks 1-10000");
        _ticks.setMaximumSize(new Dimension(60,20));
        _ticks.setMinimumSize(new Dimension(60,20));
        _ticks.setPreferredSize(new Dimension(60,20));

        _road = new JComboBox<String>();
        _road.setMaximumSize(new Dimension(60,20));
        _road.setMinimumSize(new Dimension(60,20));
        _road.setPreferredSize(new Dimension(60,20));

        for(int i = 0; i < _roads.size(); i++){
            _road.addItem(_roads.get(i).getId());
        }




        List<String> _w = new ArrayList<>();
        _w.add(Weather.STORM.toString());
        _w.add(Weather.WINDY.toString());
        _w.add(Weather.CLOUDY.toString());
        _w.add(Weather.RAINY.toString());
        _w.add(Weather.SUNNY.toString());

        _weather = new JComboBox<String>();
        _weather.setMaximumSize(new Dimension(60,20));
        _weather.setMinimumSize(new Dimension(60,20));
        _weather.setPreferredSize(new Dimension(60,20));
        for(int i = 0; i <_w.size();i++){
            _weather.addItem(_w.get(i));
        }

        JPanel accion = new JPanel();
        accion.add(new JLabel("Road: "));
        accion.add(_road);
        accion.add(new JLabel("Weather: "));
        accion.add(_weather);
        accion.add(new JLabel("Ticks: "));
        accion.add(_ticks);
        getContentPane().add(accion,BorderLayout.CENTER);
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.CENTER));
        getContentPane().add(botones,BorderLayout.SOUTH);
        _cancel = new JButton("Cancel");
        _cancel.addActionListener(new ActionButtonWeather());
        _ok = new JButton("Ok");
        _ok.addActionListener(new ActionButtonWeather());
        botones.add(_cancel);
        botones.add(_ok);
        setVisible(true);
        pack();





    }
    private class ActionButtonWeather implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand()=="Cancel"){
                dispose();
            }
            if(e.getActionCommand()=="Ok"){
                String idC = (String) _road.getSelectedItem();
                String W = (String) _weather.getSelectedItem();
                int t = (int) _ticks.getValue();
                Weather we = Weather.valueOf(W);
                List<Pair<String,Weather>> l = new ArrayList<>();
                Pair<String,Weather> _e = new Pair<>(idC,we);
                l.add(_e);
                try {
                    if(idC==null){throw new EventException("Road cargada incorrectamente");}
                    _c.createSetWeatherEvent(t,l);
                    dispose();
                } catch (EventException ex) {
                    ImageIcon icon = null;
                    icon = Imagenes.ERROR.imagen();
                    if(icon!=null){
                        JOptionPane.showMessageDialog(null,ex.getMessage(), "MENSAJE DE ERROR", JOptionPane.DEFAULT_OPTION,icon);

                    }
                    else JOptionPane.showMessageDialog(null,ex.getMessage(), "MENSAJE DE ERROR", JOptionPane.DEFAULT_OPTION);

                }
            }
        }
    }
}
