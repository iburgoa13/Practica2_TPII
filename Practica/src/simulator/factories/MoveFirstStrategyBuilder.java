package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveFirstStrategy;

public class MoveFirstStrategyBuilder extends
Builder<DequeuingStrategy>{

	static String _type ="move_first_dqs";
	public MoveFirstStrategyBuilder() {
		super(_type);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		// TODO Auto-generated method stub
		return new MoveFirstStrategy();
	}

}

