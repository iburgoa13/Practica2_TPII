package simulator.view;

import javax.swing.*;

public enum Imagenes {
    LOAD_EVENT("Open"){
        @Override
        public ImageIcon imagen(){
            ImageIcon icon = null;
            java.net.URL url = null;
            url = Imagenes.class.getResource("icons/open.png");
            if(url!=null) icon = new ImageIcon(url);
            return icon;
        }
    },
    ERROR("Error"){
        @Override
        public ImageIcon imagen(){
            ImageIcon icon = null;
            java.net.URL url = null;
            url = Imagenes.class.getResource("icons/error.png");
            if(url!=null) icon = new ImageIcon(url);
            return icon;
        }
    },

    EXIT_QUIT("Exit-quit"){
        @Override
        public ImageIcon imagen(){
            ImageIcon icon = null;
            java.net.URL url = null;
            url = Imagenes.class.getResource("icons/exit-quit.png");
            if(url!=null) icon = new ImageIcon(url);
            return icon;
        }
    },
    RUN("Run"){
        @Override
        public ImageIcon imagen() {
            ImageIcon icon = null;
            java.net.URL url = null;
            url = Imagenes.class.getResource("icons/run.png");
            if(url!=null) icon = new ImageIcon(url);
            return icon;
        }
    },
    STOP("Stop"){
        @Override
        public ImageIcon imagen() {
            ImageIcon icon = null;
            java.net.URL url = null;
            url = Imagenes.class.getResource("icons/stop.png");
            if(url!=null) icon = new ImageIcon(url);
            return icon;
        }
    },
    CO2CLASS("CO2Class"){
        @Override
        public ImageIcon imagen() {
            ImageIcon icon = null;
            java.net.URL url = null;
            url = Imagenes.class.getResource("icons/co2class.png");
            if(url!=null) icon = new ImageIcon(url);
            return icon;
        }
    },
    WEATHER("Weather"){
        @Override
        public ImageIcon imagen() {
            ImageIcon icon = null;
            java.net.URL url = null;
            url = Imagenes.class.getResource("icons/weather.png");
            if(url!=null) icon = new ImageIcon(url);
            return icon;
        }
    },
    EXIT("Exit"){
        @Override
        public ImageIcon imagen() {
            ImageIcon icon = null;
            java.net.URL url = null;
            url = Imagenes.class.getResource("icons/exit.png");
            if(url!=null) icon = new ImageIcon(url);
            return icon;
        }
    };
    private String _name;
    private Imagenes(String n){
        _name = n;
    }

    public abstract ImageIcon imagen();
}
