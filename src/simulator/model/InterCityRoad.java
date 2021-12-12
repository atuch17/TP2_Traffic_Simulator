package simulator.model;

import exceptions.NegativeException;

public class InterCityRoad extends Road{

	protected InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws NegativeException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x;
		
		if(weatherConditions == Weather.SUNNY) x= 2;
		else if(weatherConditions == Weather.CLOUDY)x= 3;
		else if(weatherConditions == Weather.RAINY)x= 10;
		else if(weatherConditions == Weather.WINDY)x= 15;
		else x= 20;	//STORM
		
		totalContamination= (int) (((100.0-x)/100.0)*totalContamination);
		
	}

	@Override
	void updateSpeedLimit() {
		if(totalContamination > contaminationAlarmLimit || SpeedLimit > maximumSpeed)
			SpeedLimit=(int)(maximumSpeed*0.5);		
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		
		if(weatherConditions == Weather.STORM) 
			v.setCurrentSpeed((int) (SpeedLimit*0.8));
		else v.setCurrentSpeed(SpeedLimit);
		return v.getCurrentSpeed();
	}

}
