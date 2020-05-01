package simulator.view;

import simulator.control.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MainWindow extends JFrame {
    private Controller _control;
    public MainWindow(Controller c){
        super("Traffic Simulator");

        _control = c;
        initGUI();
    }
    private void initGUI(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        mainPanel.add(new ControlPanel(_control),BorderLayout.PAGE_START);
        /*
        mainPanel .add( new ControlPanel( _ctrl ), BorderLayout. PAGE_START );
mainPanel .add( new StatusBar( _ctrl ),BorderLayout. PAGE_END );
         */
        JPanel viewsPanel = new JPanel(new GridLayout(1,2));
        mainPanel.add(viewsPanel,BorderLayout.CENTER);

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel,BoxLayout.Y_AXIS));
        viewsPanel.add(tablesPanel);

        JPanel mapsPanel = new JPanel();
        mapsPanel.setLayout(new BoxLayout(mapsPanel,BoxLayout.Y_AXIS));
        viewsPanel.add(mapsPanel);

        //tablas
        JPanel eventsView = cretaeViewPanel(new JTable(new EventsTableModel(_control)),"Events");
        eventsView.setPreferredSize(new Dimension(500,200));

        eventsView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,1),
                "Events", TitledBorder.LEFT,TitledBorder.TOP));
        tablesPanel.add(eventsView);


        JPanel vehiclesView = cretaeViewPanel(new JTable(new VehiclesTableModel(_control)),"Vehicles");
        vehiclesView.setPreferredSize(new Dimension(500,200));

        vehiclesView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,1),
                "Vehicles", TitledBorder.LEFT,TitledBorder.TOP));
        tablesPanel.add(vehiclesView);


        JPanel roadsView = cretaeViewPanel(new JTable(new RoadsTableModel(_control)),"Roads");
        roadsView.setPreferredSize(new Dimension(500,200));

        roadsView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,1),
                "Roads", TitledBorder.LEFT,TitledBorder.TOP));
        tablesPanel.add(roadsView);



        JPanel junctionsView = cretaeViewPanel(new JTable(new JunctionsTableModel(_control)),"Junctions");
        junctionsView.setPreferredSize(new Dimension(500,200));

        junctionsView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,1),
                "Junctions", TitledBorder.LEFT,TitledBorder.TOP));
        tablesPanel.add(junctionsView);
        //JPanel eventsView = cretaeViewPanel(new JTable(....))
        /*

         */
        //maps
        JPanel mapView = cretaeViewPanel(new MapComponent(_control),"Map");
        mapView.setPreferredSize(new Dimension(500,400));
        mapView.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,1),
                "Map", TitledBorder.LEFT,TitledBorder.TOP));
        mapsPanel.add(mapView);
        JPanel mapByRoad = cretaeViewPanel(new MapByRoadComponent(_control),"Map by Road");
        mapByRoad.setPreferredSize(new Dimension(500,400));
        mapByRoad.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,1),
                "Map by Road", TitledBorder.LEFT,TitledBorder.TOP));
        mapsPanel.add(mapByRoad);
        //... .. . . .

        mainPanel.add(new StatusBar(_control),BorderLayout.PAGE_END);
        pack();
        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    }
    private JPanel cretaeViewPanel(JComponent c, String title){
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(c));
        return p;
    }
}
