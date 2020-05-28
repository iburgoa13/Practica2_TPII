package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends
Builder<LightSwitchingStrategy>{
	static String _type = "most_crowded_lss";
	public MostCrowdedStrategyBuilder() {
		super(_type);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {

        if(data.isEmpty()) return new MostCrowdedStrategy(1);
        int t = data.getInt("timeslot");

        return new MostCrowdedStrategy(t);

	}

}

