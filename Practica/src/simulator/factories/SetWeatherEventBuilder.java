package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import exception.EventException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

    private static final String _type = "set_weather";
    public SetWeatherEventBuilder() {
        super(_type);
    }
	@Override
	protected Event createTheInstance(JSONObject data) throws EventException {
		int t = data.getInt("time");
        JSONArray array = data.getJSONArray("info");
        List<Pair<String, Weather>> list= new ArrayList<>();
        for(int i = 0; i < array.length();i++){
            JSONObject o = array.getJSONObject(i);
            String x = o.getString("road");
            String w = o.getString("weather");
            Pair<String, Weather> p = new Pair<String, Weather>(x,Weather.valueOf(w));
            list.add(p);
        }
        return new SetWeatherEvent(t,list);
	}

}

