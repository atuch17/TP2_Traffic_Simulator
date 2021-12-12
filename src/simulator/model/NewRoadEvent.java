package simulator.model;

import exceptions.NegativeException;

public abstract class NewRoadEvent extends Event {
	
	protected String id;
	protected String srcJun;	
	protected Junction j_srcJun;
	protected String destJunc;
	protected Junction j_destJunc;
	protected int length;
	protected int co2Limit;
	protected int maxSpeed;
	protected Weather weather;
	
	NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather) {
		super(time);

		this.id = id;
		this.srcJun = srcJun;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) throws NegativeException {
		
	}
	
	@Override
	public String toString() {
		return "New Road '" + id + "'";//TODO pude que no hafa falta o que desde la hijas hago un super.tostring()
	}
	
	abstract Road createRoadObject() throws NegativeException;
}
