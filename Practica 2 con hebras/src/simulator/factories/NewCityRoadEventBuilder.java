package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.NewInterCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder {

	static String _type = "new_city_road";
	public NewCityRoadEventBuilder() {
		super(_type);
		// TODO Auto-generated constructor stub
	}
	/*@Override
	protected Event createTheInstance(JSONObject data) {
		super.createTheInstance(data);
		return new NewCityRoadEvent(time,id,src,dest,l,c,max,_w);
	}*/
	@Override
	public Event nuevaRoad(){
		return new NewCityRoadEvent(time,id,src,dest,l,c,max,_w);
	}

}

