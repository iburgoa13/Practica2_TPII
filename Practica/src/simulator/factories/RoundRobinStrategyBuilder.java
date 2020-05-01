package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder 
extends Builder<LightSwitchingStrategy>{
	static String _type = "round_robin_lss";
    public RoundRobinStrategyBuilder() {
        super(_type);
    }
	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {

        if(data.isEmpty())return new RoundRobinStrategy(1);
        int t = data.getInt("timeslot");

        return new RoundRobinStrategy(t);

	}


}

