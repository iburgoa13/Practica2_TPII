package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import exception.EventException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {
	private static final String _type = "set_cont_class";
    public SetContClassEventBuilder() {
        super(_type);
    }
	@Override
	protected Event createTheInstance(JSONObject data) throws EventException {
        int t = data.getInt("time");
        JSONArray array = data.getJSONArray("info");
        List<Pair<String, Integer>> list= new ArrayList<>();
        for(int i = 0; i < array.length();i++){
            JSONObject o = array.getJSONObject(i);
            String x = o.getString("vehicle");
            int c = o.getInt("class");
            Pair<String, Integer> p = new Pair<String, Integer>(x,c);
            list.add(p);
        }
        return new NewSetContClassEvent(t,list);

	}

}

