package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

    static String _type = "new_vehicle";
    public NewVehicleEventBuilder() {
        super(_type);
    }
	@Override
	protected Event createTheInstance(JSONObject data) {

        int t = data.getInt("time");
        String id = data.getString("id");
        int m = data.getInt("maxspeed");
        int c = data.getInt("class");
        JSONArray array = new JSONArray();
        array = data.getJSONArray("itinerary");
        List<String> iti = new ArrayList<>();
        for(int i = 0; i < array.length();i++){
            iti.add(array.getString(i));
        }
        return new NewVehicleEvent(t,id,m,c,iti);
	}

}

