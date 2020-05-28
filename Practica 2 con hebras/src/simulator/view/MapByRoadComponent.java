package simulator.view;

import simulator.control.Controller;
import simulator.model.*;
import simulator.model.Event;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {
    private static final long serialVersionUID = 1L;

    private static final int _JRADIUS = 10;

    private static final Color _BG_COLOR = Color.WHITE;
    private static final Color _JUNCTION_COLOR = Color.BLUE;
    private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
    private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
    private static final Color _RED_LIGHT_COLOR = Color.RED;

    private RoadMap _map;

    private Image _car;

    public MapByRoadComponent(Controller control){
        setPreferredSize(new Dimension(300,200));
        initGUI();

        control.addObserver(this);
    }

    private void initGUI(){
        _car = ImagenesMapas.CAR.imagen();
    }
    private Image loadImage(String i){
        Image icon = null;
        try {
            URL u = null;
            u = MapByRoadComponent.class.getResource("icons/"+i);
            return ImageIO.read(u);
        } catch (IOException e) {
        }
        return icon;
    }
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // clear with a background color
        g.setColor(_BG_COLOR);
        g.clearRect(0, 0, getWidth(), getHeight());

        if (_map == null || _map.getJunctions().size() == 0) {
            g.setColor(Color.red);
            g.drawString("No map by road yet!", getWidth() / 2 - 50, getHeight() / 2);
        } else {
            //updatePrefferedSize();
            drawMap(g);
        }
    }
    public void update(RoadMap map) {
        _map = map;
        repaint();
    }

    private void drawMap(Graphics g){
        int x1 = 50;
        int x2 = getWidth()-100;
        int y = 0;
        int i = 0;
        for(Road r : _map.getRoads()) {
            y = (i+1)*50 ;
            drawRoads(g, x1, x2, y, r);
            drawJunction(g,x1,x2,y, r);
            for(Vehicle v : r.getVehicles()) {
                drawVehicle(g, x1, x2, y, r, v);
            }
            i++;
        }
    }
    private void drawVehicle(Graphics g, int x1, int x2, int y, Road r, Vehicle v){
        //la posicion del vehiculo en la imagen
        int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLength()));
        g.drawImage(_car,x,y-10,16,16,this);
        g.drawString(v.getId(),x, y-15);
    }
    private void drawRoads(Graphics g, int x1, int x2, int y, Road r){
        //Cambia el color de la carretera segun su co2
        int roadColorValue = 200 - (int) (200.0 * Math.min(1.0, (double) r.getTotalCO2() / (1.0 + (double) r.getCO2Limit())));
        Color roadColor = new Color(roadColorValue, roadColorValue, roadColorValue);
        g.setColor(roadColor);
        g.drawLine(x1,y,x2,y);


        //pone de negro el nombre de las carreteras
        g.setColor(Color.black);
        g.drawString(r.getId(),x1-30,y);

        //añade el weather
        Image img = changeWeather(r);
        g.drawImage(img,x2+10,y-30,30,30,this);

        //añade fotos del co2
        Image icon = changeCO2(r);
        g.drawImage(icon,x2+50, y -30, 30,30,this);
    }

    private Image changeCO2(Road r){
        int c = (int) Math.floor(Math.min((double) r.getTotalCO2()/(1.0 + (double) r.getCO2Limit()), 1.0 / 0.19));

        if(c == 0) return ImagenesMapas.CONT_0.imagen();
        if(c == 1) return ImagenesMapas.CONT_1.imagen();
        if(c == 2) return ImagenesMapas.CONT_2.imagen();
        if(c == 3) return ImagenesMapas.CONT_3.imagen();
        if(c == 4) return ImagenesMapas.CONT_4.imagen();
        return ImagenesMapas.CONT_5.imagen();
    }
    private Image changeWeather(Road r){
        if(r.getWeather() == Weather.SUNNY.name())return ImagenesMapas.SUN.imagen();//return loadImage("sun.png");
        else if(r.getWeather().equals(Weather.STORM.name())) return ImagenesMapas.STORM.imagen();//return loadImage("storm.png");
        else if(r.getWeather().equals(Weather.WINDY.name())) return ImagenesMapas.WIND.imagen();//loadImage("wind.png");
        else if(r.getWeather().equals(Weather.RAINY.name())) return ImagenesMapas.RAIN.imagen();//loadImage("rain.png");
        else return ImagenesMapas.CLOUD.imagen();// loadImage("cloud.png");
    }


    private void drawJunction(Graphics g, int x1, int x2, int y,Road r){

        //Colorea los inicios
        g.setColor(_JUNCTION_COLOR);
        g.fillOval(x1-_JRADIUS/2,y- _JRADIUS/2,_JRADIUS,_JRADIUS);
        g.setColor(_JUNCTION_LABEL_COLOR);
        g.drawString(r.getSrc().getId(),x1,y-10);

        //Colorea los destinos segun el color del semaforo
        int index = r.getDest().getGreenLightIndex();
        Color destino = _RED_LIGHT_COLOR;
        if(index!= -1 && r.equals(r.getDest().getInRoads().get(index))){
            destino = _GREEN_LIGHT_COLOR;
        }
        g.setColor(destino);
        g.fillOval(x2-_JRADIUS/2,y- _JRADIUS/2,_JRADIUS,_JRADIUS);
        g.setColor(_JUNCTION_LABEL_COLOR);
        g.drawString(r.getDest().getId(),x2,y-10);
    }
    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
    }

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                update(map);
            }
        });    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                update(map);
            }
        });    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                update(map);
            }
        });    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                update(map);
            }
        });    }

    @Override
    public void onError(String err) {
    }
}
