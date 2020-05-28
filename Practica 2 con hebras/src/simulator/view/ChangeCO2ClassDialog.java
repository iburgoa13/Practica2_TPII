package simulator.view;

import exception.EventException;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChangeCO2ClassDialog extends JDialog {
    //CONTROLADOR
    private Controller _c;
    //BOTONES
    private JButton _ok;
    private JButton _cancel;

    //JCOMBOX Y JSPINNER
    private JComboBox<String> _vehiculos;
    private JComboBox<Integer> _co2;
    private JSpinner _ticks;



    public ChangeCO2ClassDialog(Controller c){
        _c = c;
        initCO2();
    }


    private void initCO2(){
        setTitle("CHANGE CO2 Class");
        setSize(400,400);
        setLocationRelativeTo(null);
        List<Vehicle> v = new ArrayList<>();
        for(Vehicle x : _c.get_roadMap().getVehicles()){
            v.add(x);
        }
        JLabel t = new JLabel("Schedule an event to Change the CO2 class of a vehicle after a given number of " +
                "simulation ticks from now.");

        add(t,BorderLayout.NORTH);
        //Ajusto el tick
        _ticks = new JSpinner(new SpinnerNumberModel(1,1,10000,1));
        _ticks.setToolTipText("Simulation ticks 1-10000");
        _ticks.setMaximumSize(new Dimension(60,20));
        _ticks.setMinimumSize(new Dimension(60,20));
        _ticks.setPreferredSize(new Dimension(60,20));

        //creo el jcombox de vehiculos y tamaños
        _vehiculos = new JComboBox<String>();
        _vehiculos.setMaximumSize(new Dimension(60,20));
        _vehiculos.setMinimumSize(new Dimension(60,20));
        _vehiculos.setPreferredSize(new Dimension(60,20));

        //inserto los vehiculos
        for(int i = 0; i < v.size(); i++){
            _vehiculos.addItem(v.get(i).getId());
        }



        //creo jcombox del co2
        _co2 = new JComboBox<Integer>();
        _co2.setMaximumSize(new Dimension(60,20));
        _co2.setMinimumSize(new Dimension(60,20));
        _co2.setPreferredSize(new Dimension(60,20));
        //inserto para elegir el co2 de 0 a 10
        for(int i = 0; i <=10;i++){
            _co2.addItem(i);
        }
        //inserto en un JPanel los elementos
        JPanel accion = new JPanel();
        accion.add(new JLabel("Vehicle: "));
        accion.add(_vehiculos);
        accion.add(new JLabel("CO2 Class: "));
        accion.add(_co2);
        accion.add(new JLabel("Ticks: "));
        accion.add(_ticks);
        getContentPane().add(accion,BorderLayout.CENTER);
        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.CENTER));
        getContentPane().add(botones,BorderLayout.SOUTH);
        _cancel = new JButton("Cancel");
        _cancel.addActionListener(new ActionButtonCO2());
        _ok = new JButton("Ok");
        _ok.addActionListener(new ActionButtonCO2());
        botones.add(_cancel);
        botones.add(_ok);
        setVisible(true);
        pack();
    }
    private class ActionButtonCO2 implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand()=="Cancel"){
               // System.exit(0);
                dispose();
            }
            if(e.getActionCommand()=="Ok"){
                String id = (String) _vehiculos.getSelectedItem();
                Integer co2 = (Integer) _co2.getSelectedItem();
                Integer tick = (Integer) _ticks.getValue();
                if(tick > 10000){

                }

                Pair<String,Integer> cs = new Pair<>(id,co2);
                List<Pair<String,Integer>> l = new ArrayList<>();
                l.add(cs);
                NewSetContClassEvent x = null;

                try {
                    if(id==null){throw  new EventException("Vehiculo cargado incorrectamente");}
                    _c.createNewSetContClassEvent(tick,l);
                } catch (EventException ex) {
                    ImageIcon icon = null;
                    icon = Imagenes.ERROR.imagen();
                    if(icon!=null){
                        JOptionPane.showMessageDialog(null,ex.getMessage(), "MENSAJE DE ERROR", JOptionPane.DEFAULT_OPTION,icon);
                    }
                    else JOptionPane.showMessageDialog(null,ex.getMessage(), "MENSAJE DE ERROR", JOptionPane.DEFAULT_OPTION);
                }
                dispose();
            }
        }
    }
}
