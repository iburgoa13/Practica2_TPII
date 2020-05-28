package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import exception.EventException;

public interface Factory<T> {
	public T createInstance(JSONObject info) throws JSONException, EventException;
}
