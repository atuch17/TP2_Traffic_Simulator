 package simulator.model;

import exceptions.NegativeException;

public class CityRoad extends Road{	
	
	protected CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws NegativeException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}
	
	@Override
	void reduceTotalContamination() {
		int x;
		
		if(weatherConditions == Weather.STORM || weatherConditions == Weather.WINDY) x= 10; 
		else x= 2;
		
		totalContamination=totalContamination-x;
		
		
		if(totalContamination<0) totalContamination=0;
	}
	
	@Override
	void updateSpeedLimit() {
		SpeedLimit = maximumSpeed;
		
	}
	
	@Override
	int calculateVehicleSpeed(Vehicle v) throws NegativeException {
		
		
		v.setSpeed((int)(((11.0-v.getContaminationClass() )/11.0)*SpeedLimit));
		
		return v.getCurrentSpeed();
	}
}
