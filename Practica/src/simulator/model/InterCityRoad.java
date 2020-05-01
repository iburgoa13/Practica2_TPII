package simulator.model;

import exception.RoadException;

public class InterCityRoad extends Road {
    InterCityRoad(String id, Junction src, Junction d, int m, int c, int l, Weather w) throws RoadException {
        super(id, src, d, m, c, l, w);
    }

    @Override
    void reduceTotalContamination() {
        int tc = _totalCont;
        int x = 2;//SUNNY
        if(_weather == Weather.STORM) x = 20;
        if(_weather == Weather.CLOUDY) x = 3;
        if(_weather == Weather.RAINY) x = 10;
        if(_weather == Weather.WINDY) x = 15;
        _totalCont= (int)(((100.0-x)/100.0)*tc);
    }


    @Override
    void updateSpeedLimit() {
        if(_totalCont >= _limitCont){
            _limitSpeed = (int) (_maxSpeed*0.5);
        }
        else _limitSpeed = _maxSpeed;
    }

    @Override
    int calculateVehicleSpeed(Vehicle v) {
        if(_weather == Weather.STORM) return (int) (_limitSpeed*0.8);
        return _limitSpeed;
    }
}

