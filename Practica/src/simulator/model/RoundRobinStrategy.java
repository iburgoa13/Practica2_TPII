package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements
LightSwitchingStrategy{
	protected int _timeSlot;
    public RoundRobinStrategy(int t){
        _timeSlot = t;
    }
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if(roads.isEmpty()) return -1;
        if(currGreen == -1) return 0;
        if(currTime - lastSwitchingTime < _timeSlot) return currGreen;
        else return (currGreen+1)%roads.size();

	}

}

