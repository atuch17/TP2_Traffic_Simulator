package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.NegativeException;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
    private Factory<LightSwitchingStrategy>lssFactory;
    private Factory<DequeuingStrategy> dqsFactory;

    public NewJunctionEventBuilder(Factory<LightSwitchingStrategy>lssFactory, Factory<DequeuingStrategy> dqsFactory) {
        super("new_junction");
        this.lssFactory = lssFactory;
        this.dqsFactory = dqsFactory;
    }

    @Override
    protected Event createTheInstance(JSONObject data) throws JSONException, NegativeException { 

       // if (data.has("new_junction")) {
            return new NewJunctionEvent(data.getInt("time"), data.getString("id"), this.lssFactory.createInstance(data.getJSONObject("ls_strategy")), 
                    this.dqsFactory.createInstance(data.getJSONObject("dq_strategy")),
                    data.getJSONArray("coor").getInt(0), data.getJSONArray("coor").getInt(1));
       //}

       // return null;
    }


}