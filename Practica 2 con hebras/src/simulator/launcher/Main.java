package simulator.launcher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import exception.*;
import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

import javax.swing.*;

public class Main {

    private final static String _modeDefaultValue = "gui";
    private final static Integer _timeLimitDefaultValue = 10;
    private static String _inFile = null;
    private static String _outFile = null;
    private static String _ticks = null;
    private static Factory<Event> _eventsFactory = null;
    private static Factory<LightSwitchingStrategy> _lssFactory = null;
    private static Factory<DequeuingStrategy> _dqsFactory = null;
    private static Integer _t = null;
    private static String _mode = null;

    private static void parseArgs(String[] args) {

        // define the valid command line options
        //
        Options cmdLineOptions = buildOptions();

        // parse the command line as provided in args
        //
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(cmdLineOptions, args);
            parseHelpOption(line, cmdLineOptions);
            parseTicksSimulatorOption(line);
            parseModeOption(line);
            parseInFileOption(line);
            parseOutFileOption(line);
            //añadido el metodo para -t


            // if there are some remaining arguments, then something wrong is
            // provided in the command line!
            //
            String[] remaining = line.getArgs();
            if (remaining.length > 0) {
                String error = "Illegal arguments:";
                for (String o : remaining)
                    error += (" " + o);
                throw new ParseException(error);
            }

        } catch (ParseException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }

    }

    private static Options buildOptions() {
        Options cmdLineOptions = new Options();

        cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
        cmdLineOptions.addOption(
                Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
        cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
        cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator�s main loop (default value is "+_timeLimitDefaultValue+")").build());
        cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execute Mode whit values gui  or console").build());
        return cmdLineOptions;
    }


    private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
            System.exit(0);
        }
    }

    private static void parseModeOption(CommandLine line) throws ParseException {
        _mode = line.getOptionValue("m");
        if(_mode == null ){
            _mode = _modeDefaultValue;
        }
        if(!_mode.equalsIgnoreCase(_modeDefaultValue) && !_mode.equalsIgnoreCase("console")){
            throw new ParseException("Mode "+_mode+" is incorrect, enter 'gui' or 'console' in arguments.");
        }
    }
    private static void parseInFileOption(CommandLine line) throws ParseException {
        _inFile = line.getOptionValue("i");
        if (_inFile == null && _mode.equalsIgnoreCase("console")) {
            throw new ParseException("An events file is missing");
        }
    }

    private static void parseOutFileOption(CommandLine line) throws ParseException {
        _outFile = line.getOptionValue("o");
    }

    private static void parseTicksSimulatorOption(CommandLine line) throws ParseException{
        _ticks = line.getOptionValue("t");
        if(_ticks==null) {
            _t = _timeLimitDefaultValue;
        }
        else {
            try {

                _t = Integer.parseInt(_ticks);
            } catch (Exception e) {
                throw new ParseException("Invalid number " + _ticks);
            }
        }



   
    }
    private static void initFactories() {
        List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
        lsbs.add(new RoundRobinStrategyBuilder());
        lsbs.add( new MostCrowdedStrategyBuilder());
        _lssFactory = new BuilderBasedFactory<>(lsbs);

        List<Builder<DequeuingStrategy>>dqbs = new ArrayList<>();
        dqbs.add(new MoveFirstStrategyBuilder());
        dqbs.add(new MoveAllStrategyBuilder());
        _dqsFactory = new BuilderBasedFactory<>(dqbs);

        //a que hay que llamar a eventos o builders
        List<Builder<Event>> ebs = new ArrayList<>();
        ebs.add(new NewJunctionEventBuilder(_lssFactory,_dqsFactory));
        ebs.add(new NewCityRoadEventBuilder());
        ebs.add(new NewInterCityRoadEventBuilder());
        ebs.add(new NewVehicleEventBuilder());
        ebs.add(new SetWeatherEventBuilder());
        ebs.add(new SetContClassEventBuilder());
        _eventsFactory = new BuilderBasedFactory<>(ebs);


        // TODO complete this method to initialize _eventsFactory

    }

    private static void startBatchMode() throws TrafficSimulatorException, IOException, ControllerException, EventException {
        TrafficSimulator _traffic = new TrafficSimulator();
        Controller _controller = new Controller(_traffic,_eventsFactory);
        //deben ser distintos de nulo, en los metodos hay excepciones
        InputStream in = new FileInputStream(_inFile);
        OutputStream out;
        if(_outFile == null) out = System.out;
        else out = new FileOutputStream(_outFile);
        _controller.loadEvents(in);
        _controller.run(_t,out);
        in.close();
        System.out.println("Done!");

    }

    private static void start(String[] args) throws TrafficSimulatorException, EventException, ControllerException, IOException, VehicleException {
        initFactories();
        parseArgs(args);
       if(_mode.equalsIgnoreCase("console")) {
            startBatchMode();
        }
        else startGUIMode();

    }
    private static void startGUIMode() throws ControllerException, FileNotFoundException, EventException {

        TrafficSimulator _traffic = new TrafficSimulator();
        Controller _controller = new Controller(_traffic,_eventsFactory);
        if(_inFile!= null){
            InputStream in = new FileInputStream(_inFile);
            _controller.loadEvents(in);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow(_controller);
            }
        });
    }

    // example command lines:
    //
    // -i resources/examples/ex1.json
    // -i resources/examples/ex1.json -t 300
    // -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
    // --help

    public static void main(String[] args) {

        try {
           start(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
