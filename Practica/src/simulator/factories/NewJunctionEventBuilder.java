package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import exception.EventException;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {


    static String _type = "new_junction";
    protected Factory<LightSwitchingStrategy> _lss;
    protected Factory<DequeuingStrategy> _dqs;
    public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssF,
                                   Factory<DequeuingStrategy> dqsF) {
        super(_type);
        _dqs = dqsF;
        _lss = lssF;
    }
	@Override
	protected Event createTheInstance(JSONObject data) throws JSONException, EventException {
		int _time = data.getInt("time");
        String _id = data.getString("id");
        JSONArray coor = data.getJSONArray("coor");
        return new NewJunctionEvent(_time,_id,_lss.createInstance(data.getJSONObject("ls_strategy")), _dqs.createInstance(data.getJSONObject("dq_strategy")),
                coor.getInt(0),coor.getInt(1));

	}

}

