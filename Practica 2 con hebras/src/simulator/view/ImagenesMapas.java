package simulator.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public enum ImagenesMapas {
    CAR_FRONT("Car_front"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/car_front.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    CAR("Car"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/car.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    CONT_0("Cont_0"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/cont_0.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    CONT_1("Cont_1"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/cont_1.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    CONT_2("Cont_2"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/cont_2.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    CONT_3("Cont_3"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/cont_3.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    CONT_4("Cont_4"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/cont_4.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    CONT_5("Cont_5"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/cont_5.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    CLOUD("Cloud"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/cloud.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    RAIN("Rain"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/rain.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    STORM("Storm"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/storm.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    SUN("Sun"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/sun.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    },
    WIND("Wind"){
        @Override
        public Image imagen() {
            Image icon = null;
            URL u = null;
            try {
                u = ImagenesMapas.class.getResource("icons/wind.png");
                if (u != null) return ImageIO.read(u);
            } catch (IOException i) {
            }
            return null;
        }
    };
    private String _name;

    private ImagenesMapas(String s){
        _name = s;
    }
    public abstract Image imagen() ;
}
