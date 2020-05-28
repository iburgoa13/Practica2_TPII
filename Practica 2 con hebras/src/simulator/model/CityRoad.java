package simulator.model;

import exception.RoadException;

public class CityRoad extends Road {
    CityRoad(String id, Junction src, Junction d, int m, int c, int l, Weather w) throws RoadException {
        super(id, src, d, m, c, l, w);
    }

    @Override
    void reduceTotalContamination() {
        int x = 2;//cualquier caso menos windy o storm
        if(_weather == Weather.WINDY ||
        _weather == Weather.STORM) x = 10;
        _totalCont -=x;
        if(_totalCont<0) _totalCont = 0;
    }

    @Override
    void updateSpeedLimit() {
        _limitSpeed = _maxSpeed;
    }

    @Override
    int calculateVehicleSpeed(Vehicle v) {
        int f, s;
        s = _limitSpeed;
        f = v._classCont;
        //ceil redondea arriba
        return (int) ((((11.0-f)/11.0)*s));
       // return 1+(int) (((11.0-f)/11.0)*s);
    }
}

