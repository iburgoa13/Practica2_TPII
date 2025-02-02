package simulator.model;

import exception.EventException;
import exception.JunctionException;
import exception.RoadException;
import exception.RoadMapException;
import exception.VehicleException;

public abstract class Event implements Comparable<Event> {

	protected int _time;

	Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Time must be positive (" + time + ")");
		else
			_time = time;
	}

	public int getTime() {
		return _time;
	}

	@Override
	public int compareTo(Event o) {
		// TODO complete
		return 0;
	}

	abstract void execute(RoadMap map) throws RoadException, JunctionException, RoadMapException, VehicleException, EventException;
}
