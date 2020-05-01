package extra.jtable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JTableExamples extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 1);
    private EventsTableModel _model;
    private JTable _eventsTable;
    private JButton addButton;

    // this is what we show in the table
    // esto es lo que mostramos en la table
    private List<EventEx> _events;
    private JSpinner _time;
    private JComboBox<Integer> _priotiry;

    public JTableExamples() {

        super("JTable Example");
        initGUI();
    }

    public void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.setContentPane(mainPanel);

        JPanel buttonsPanel = new JPanel();
        mainPanel.add(buttonsPanel, BorderLayout.PAGE_START);

        // spinner for selecting time
        _time = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
        _time.setToolTipText("Simulation tick to run: 1-10000");
        _time.setMaximumSize(new Dimension(80, 40));
        _time.setMinimumSize(new Dimension(80, 40));
        _time.setPreferredSize(new Dimension(80, 40));

        // comboxfor selecting priority
        _priotiry = new JComboBox<Integer>();
        for (int i = 0; i < 10; i++) {
            _priotiry.addItem(i);
        }

        addButton = new JButton("Add Event");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addEvent();
            }
        });

        buttonsPanel.add( new JLabel("Time: "));
        buttonsPanel.add(_time);
        buttonsPanel.add( new JLabel("Priority: "));
        buttonsPanel.add(_priotiry);
        buttonsPanel.add(addButton);

        // table
        JPanel eventsPanel = new JPanel(new BorderLayout());
        mainPanel.add(eventsPanel,BorderLayout.CENTER);

        // add border
        eventsPanel.setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Events", TitledBorder.LEFT,
                TitledBorder.TOP));

        // the model
        _model = new EventsTableModel();
        _eventsTable = new JTable(_model);

        eventsPanel.add(new JScrollPane(_eventsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

        // the actual events list
        _events = new ArrayList<EventEx>();
        _model.setEventsList(_events);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700, 300);
        pack();
        setVisible(true);
    }

    public void addEvent() {
        try {
            Integer time = (Integer) _time.getValue();
            Integer priority = (Integer) _priotiry.getSelectedItem();
            _events.add(new EventEx(time, priority));

            // avisamos al modelo de tabla para que se actualize. En la
            // prÃ¡ctica esto no hace falta porque se le avisa directamente
            // el TrafficSimulator
            //
            // We notify the table model that the list has been changed. This is not
            // required in the assignment, it will be notified directly by the
            // TrafficSimulator since it is an observer
            //
            _model.update();
        } catch (Exception e) {
            JOptionPane.showMessageDialog( //
                    (Frame) SwingUtilities.getWindowAncestor(this), //
                    "Something went wrong ...",
                    "ERROR", //
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JTableExamples();
            }
        });
    }

}

