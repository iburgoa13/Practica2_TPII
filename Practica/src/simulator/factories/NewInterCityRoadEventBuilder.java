package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder 
extends NewRoadEventBuilder{
	
	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

/*	@Override
    protected Event createTheInstance(JSONObject data) {

    }*/

    @Override
    public Event nuevaRoad(){
        return new NewInterCityRoadEvent(time,id,src,dest,l,c,max,_w);
    }
}

