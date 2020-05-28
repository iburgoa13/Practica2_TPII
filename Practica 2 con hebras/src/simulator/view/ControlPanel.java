package simulator.view;

import exception.ControllerException;
import exception.EventException;
import exception.TrafficSimulatorException;
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
    private JFileChooser _file;
    private   JToolBar toolbar;

    /*
    PRACTICA 3 OPCIONAL
     */
    private volatile Thread _thread;
    private JSpinner _delay;

    public ControlPanel(Controller _c){
        _controller = _c;
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
        _loadEvents.setToolTipText("Elige archivo");
        _loadEvents.addActionListener(new JFileChooserListener());

        //boton cambiar co2
        _changeCO2 = new JButton();
        cargaImagen(_changeCO2,Imagenes.CO2CLASS.imagen(),"co2class");
        _changeCO2.setToolTipText("Cambia co2 del vehiculo");
        _changeCO2.addActionListener(new changeCO2Listener());


        //boton cambiar weather
        _changeWeather = new JButton();
        cargaImagen(_changeWeather,Imagenes.WEATHER.imagen(),"weather");
        _changeWeather.setToolTipText("Cambia el weather de la carretera");
        _changeWeather.addActionListener(new changeWeatherListener());


        //boton run
        _run = new JButton();
        cargaImagen(_run,Imagenes.RUN.imagen(),"run");
        _run.setToolTipText("Arranca el simulador");
        _run.addActionListener(new RunListener());

        //boton stop
        _stop = new JButton();
        cargaImagen(_stop,Imagenes.STOP.imagen(),"stop");
        _stop.setToolTipText("Detiene la simulacion");
        _stop.addActionListener(new stopListener());


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

        /*
        PRACTICA 3
        JSPINNER DE DELAY
         */
        toolbar.add(new JLabel("Delay: "));
        _delay = new JSpinner(new SpinnerNumberModel(10,0,1000,1));
        _delay.setToolTipText("Simulation delay to stop 0-1000");
        _delay.setMaximumSize(new Dimension(60,30));
        _delay.setMinimumSize(new Dimension(60,30));

        toolbar.add(_delay);
        //boton exit
        _exit = new JButton();
        cargaImagen(_exit,Imagenes.EXIT.imagen(),"exit");
        _exit.setToolTipText("Salir de la simulacion");
        _exit.addActionListener(new exitListener());


        //añado un Box.createGlue para separar
        toolbar.add(Box.createGlue());
        toolbar.add(_exit);
        add(toolbar, BorderLayout.PAGE_START);
    }
    private class stopListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            stop();
        }
    }
    private class changeCO2Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ChangeCO2ClassDialog(_controller);
                }
            });
        }
    }
    private class exitListener implements ActionListener{

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
            if(i == JOptionPane.YES_OPTION){ System.exit(0);}
        }
    }
    private class changeWeatherListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ChangeWeatherDialog(_controller);
                }
            });
        }
    }

    private class RunListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                int ticks = (int) _ticks.getValue();
                long delay = ((Number) _delay.getValue()).longValue();
                if(_thread==null){
                    _thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            run_sim(ticks,delay);
                            _thread = null;
                        }
                    });
                    _thread.start();
                }
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage(), "errooorsito", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private class JFileChooserListener extends Component implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            _file = new JFileChooser();
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
                }
            }

        }
    }
    private void stop(){
        if(_thread != null){
            _thread.interrupt();
            setButton(true);
        }
    }
    private void run_sim(int n, long delay){
        setButton(false);
        while(n > 0 && !_thread.isInterrupted()){
            try{
                _controller.run(1);
                Thread.sleep(delay);
            }
            catch (InterruptedException i){
                return;
            }
            catch (TrafficSimulatorException  e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "errorsito", JOptionPane.ERROR_MESSAGE);
                        setButton(true);
                        return;                    }
                });
            }
            n--;
        }
        setButton(true);
    }

    private void setButton(boolean b) {
        _changeCO2.setEnabled(b);
        _changeWeather.setEnabled(b);
        _loadEvents.setEnabled(b);
        _run.setEnabled(b);
        _delay.setEnabled(b);
        _exit.setEnabled(b);
        _ticks.setEnabled(b);
    }


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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _ticks.setValue(10);
                _delay.setValue(10);
            }
        });

    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {

    }

    @Override
    public void onError(String err) {
    }
}

