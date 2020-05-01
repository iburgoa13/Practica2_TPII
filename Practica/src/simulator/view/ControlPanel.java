package simulator.view;

import exception.ControllerException;
import exception.EventException;
import exception.TrafficSimulatorException;
import jdk.nashorn.internal.scripts.JO;
import org.json.JSONException;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class ControlPanel extends JPanel implements TrafficSimObserver {

    private Controller _controller;
    //Le añado RoadMap para poder recoger los coches y carreteras
    private RoadMap _roadMap;
    private JButton _loadEvents;
    private  JButton _changeCO2;
    private JButton _changeWeather;
    private JButton _run;
    private JButton _stop;
    private JButton _exit;
    private JSpinner _ticks;
    private boolean _stopped;
    private JFileChooser _file;
    private   JToolBar toolbar;
    public ControlPanel(Controller _c){
        _controller = _c;
        _stopped = false;
        _roadMap = _c.get_roadMap();
        init();
        //añado mi observador
        _controller.addObserver(this);
    }
    private void cargaImagen(JButton button, ImageIcon img, String boton){
       ImageIcon icon = null;
       icon = img;
       if(icon!=null) button.setIcon(icon);
        else button.setText(boton);
    }
    private void init(){
        setLayout(new BorderLayout());
        //barra de botones
         toolbar = new JToolBar();
         //boton cargar eventos
        _loadEvents = new JButton();
        cargaImagen(_loadEvents,Imagenes.LOAD_EVENT.imagen(), "Load_event");
        //_loadEvents.setIcon(new ImageIcon(ControlPanel.class.getResource("icons/open.png")));
        //_loadEvents.setIcon(new ImageIcon("resources/icons/open.png"));
        _loadEvents.setToolTipText("Elige archivo");

        _loadEvents.addActionListener(new JFileChooserListener());

        //boton cambiar co2
        _changeCO2 = new JButton();
        //_changeCO2.setIcon(new ImageIcon("resources/icons/co2class.png"));
        cargaImagen(_changeCO2,Imagenes.CO2CLASS.imagen(),"co2class");
        _changeCO2.setToolTipText("Cambia co2 del vehiculo");
        _changeCO2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new ChangeCO2ClassDialog(_controller);
                    }
                });

            }
        });

        //boton cambiar weather
        _changeWeather = new JButton();
        cargaImagen(_changeWeather,Imagenes.WEATHER.imagen(),"weather");
       // _changeWeather.setIcon(new ImageIcon("resources/icons/weather.png"));
        _changeWeather.setToolTipText("Cambia el weather de la carretera");
        _changeWeather.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new ChangeWeatherDialog(_controller);
                    }
                });
            }
        });

        //boton run
        _run = new JButton();
        cargaImagen(_run,Imagenes.RUN.imagen(),"run");
        //_run.setIcon(new ImageIcon("resources/icons/run.png"));
        _run.setToolTipText("Arranca el simulador");
        _run.addActionListener(new RunListener());

        //boton stop
        _stop = new JButton();
        cargaImagen(_stop,Imagenes.STOP.imagen(),"stop");
        //_stop.setIcon(new ImageIcon("resources/icons/stop.png"));
        _stop.setToolTipText("Detiene la simulacion");
        _stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        //añado los botones a la izquierda
        toolbar.add(_loadEvents);
        toolbar.add(_changeCO2);
        toolbar.add(_changeWeather);
        toolbar.add(_run);
        toolbar.add(_stop);
        //hago una separacion para añadir los ticks
        toolbar.addSeparator();

        //JSpinner de los ticks
        toolbar.add(new JLabel("Ticks: "));
       _ticks =  new JSpinner(new SpinnerNumberModel(10,1,10000,1));
       _ticks.setToolTipText("Simulation tick to run: 1-10000");
        _ticks.setMaximumSize(new Dimension(60,30));
        _ticks.setMinimumSize(new Dimension(60,30));

        toolbar.add(_ticks);

        //hago que no se puedan mover
        toolbar.setFloatable(false);

       //boton exit
       _exit = new JButton();
       cargaImagen(_exit,Imagenes.EXIT.imagen(),"exit");
       //_exit.setIcon(new ImageIcon("resources/icons/exit.png"));
       _exit.setToolTipText("Salir de la simulacion");
       _exit.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               int d = JOptionPane.YES_NO_OPTION;

               ImageIcon icon = null;
               icon = Imagenes.EXIT_QUIT.imagen();

               int i;
               if(icon!=null){
                    i = JOptionPane.showConfirmDialog(null,"Are sure you want to quit?","Quit",d,JOptionPane.QUESTION_MESSAGE,icon);
               }
               else {
                    i = JOptionPane.showConfirmDialog(null,"Are sure you want to quit?","Quit",d,JOptionPane.QUESTION_MESSAGE);
               }
               // JOptionPane.showMessageDialog(null,"Are sure you want to quit?","Quit",d,JOptionPane.QUESTION_MESSAGE);
               if(i == JOptionPane.YES_OPTION){ System.exit(0);}

           }
       });
       //añado un Box.createGlue para separar
       toolbar.add(Box.createGlue());
       toolbar.add(_exit);
       add(toolbar, BorderLayout.PAGE_START);
    }
    private class RunListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int ticks = (int) _ticks.getValue();
            _stopped = false;
            run_sim( ticks);
        }
    }

    private class JFileChooserListener extends Component implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            _file = new JFileChooser();
            //PREGUNTAR MAÑANA VIERNES
            _file.setCurrentDirectory(new File(System.getProperty("user.dir")+"/resources/examples"));
            _file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            _file.setFileFilter(new FileNameExtensionFilter("JSON File","json"));
            _file.setMultiSelectionEnabled(false);
            if(_file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                try{
                    _controller.reset();
                    _controller.loadEvents(new FileInputStream(_file.getSelectedFile()));

                }  catch (FileNotFoundException | EventException | ControllerException | JSONException ex) {
                    ImageIcon icon = new ImageIcon(ControlPanel.class.getResource("icons/error.png"));
                    JOptionPane.showMessageDialog(null,"File not found", "MENSAJE DE ERROR", JOptionPane.DEFAULT_OPTION,icon);
                   // throw new IllegalArgumentException("File not found");
                }
            }

        }
    }
    private void stop(){
        _stopped = true;
    }
    private void run_sim(int n){
        if(n > 0 && !_stopped){
            try{
                _controller.run(1);
            } catch (TrafficSimulatorException e) {
               _stopped = true;
               return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    run_sim(n-1);
                }
            });
        }
        else{
            //enableToolBar(true);
            _stopped = true;
        }
    }
  /*  private class FileListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            _file = new JFileChooser();
            File f = new File(System.getProperty("user.dir"));
            _file.setCurrentDirectory(f);
            if(f==null)System.out.println("NO");
            _file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            _file.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
            _file.setMultiSelectionEnabled(false);
        }
    }*/
    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {

    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        _ticks.setValue(10);
        _stopped = false;
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onError(String err) {
        //tengo dudas en onError();
       // JOptionPane.showMessageDialog(null,err, "MENSAJE DE ERROR", JOptionPane.DEFAULT_OPTION);
    }
}
