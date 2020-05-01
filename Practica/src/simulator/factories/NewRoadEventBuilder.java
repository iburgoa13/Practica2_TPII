package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewRoadEvent;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {
	protected int time,l,c,max;
	protected String id,src,dest,w;
    protected Weather _w = null;

	NewRoadEventBuilder(String type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Event createTheInstance(JSONObject data) {

        time = data.getInt("time");
        id = data.getString("id");
        src = data.getString("src");
        dest = data.getString("dest");
        l = data.getInt("length");
        c = data.getInt("co2limit");
        max = data.getInt("maxspeed");
        w = data.getString("weather");
        _w = Weather.valueOf(w.toUpperCase());
        return nuevaRoad();

	}

    public abstract  Event nuevaRoad();


}
