package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.NegativeException;

public interface Factory<T> {			//quien la implementa?
	public T createInstance(JSONObject info) throws JSONException, NegativeException;
}
