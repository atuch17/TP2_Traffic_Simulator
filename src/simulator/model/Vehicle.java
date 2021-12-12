package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import exceptions.NegativeException;
import exceptions.StatusException;

// cuando es travelling su velocidad no debe ser 0
public class Vehicle extends SimulatedObject {

	private List<Junction> itinerary; // al menos 2
	private int maximunSpeed; // positivo!
	private int currentSpeed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contaminationClass; // 0 to 10
	private int totalContamination;
	private int totalTravelledDistance;

	private int indexCruce;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws NegativeException {
		super(id);
		if (maxSpeed < 0 || contClass < 0 || contClass > 10 || itinerary.size() < 2) {
			throw new NegativeException("Algun argumento de vehiculo es negativo");
		}

		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		this.indexCruce = 0;
		this.currentSpeed=0;//
		this.maximunSpeed = maxSpeed;
		this.contaminationClass = contClass;
		this.status = VehicleStatus.PENDING;
		this.totalTravelledDistance = 0;
		this.totalContamination = 0;
		this.location=0;

	}

	@Override
	void advance(int time) throws NegativeException {
        if (this.getStatus() == VehicleStatus.TRAVELING) {
            int oldLocation = this.location;

            if (this.location+ this.currentSpeed >= this.road.getLength()) {
                indexCruce++;
                this.location = this.road.getLength();
                this.totalTravelledDistance = 0;
                for (int i =0; i<indexCruce; i++ ) {
                    this.totalTravelledDistance += this.itinerary.get(i).roadTo(this.itinerary.get(i+1)).getLength();
                }

                this.location = min(this.location + this.currentSpeed, this.road.getLength());

                int c = (this.location - oldLocation) * this.contaminationClass;
                this.totalContamination += c;
                this.road.addContamination(c);

                this.road.getDestination().enter(this);
                this.currentSpeed=0;
                this.status = VehicleStatus.WAITING;


            }else {

                this.location = min(this.location + this.currentSpeed, this.road.getLength());
                this.totalTravelledDistance+=this.currentSpeed;
                int c = (this.location - oldLocation) * this.contaminationClass;
                this.totalContamination += c;
                this.road.addContamination(c);
            }


        }
    }

	void moveToNextRoad() throws StatusException, NegativeException { 

		if (this.status == VehicleStatus.PENDING || this.status == VehicleStatus.WAITING) {
			if (this.road != null) {
				this.road.exit(this);
			}
			if (indexCruce == this.itinerary.size() - 1) {
				this.status = VehicleStatus.ARRIVED;
				this.location = 0;
				this.road = null;
				this.currentSpeed=0;
				
			}else {
				this.road = this.itinerary.get(indexCruce).roadTo(this.itinerary.get(indexCruce + 1));
				this.status = VehicleStatus.TRAVELING;
				this.location = 0;
				this.road.enter(this);
			}
			

		} else {
			throw new StatusException("El estado del vehiculos no es Pending o Waiting");
		}

	}

	private int min(int i, int j) {
		if (i < j) {
			return i;
		}
		return j;
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", getId());
		jo.put("speed", currentSpeed);
		jo.put("distance", totalTravelledDistance);
		jo.put("co2", totalContamination);
		jo.put("class", contaminationClass);
		jo.put("status", status);

		if (status != VehicleStatus.ARRIVED && status != VehicleStatus.PENDING) {
			jo.put("road", road.getId());
			jo.put("location", location);
		}
		return jo;
	}

	void setSpeed(int s) throws NegativeException {
		if (s >= 0) {
			this.currentSpeed = min(s, this.maximunSpeed);
		} else {
			throw new NegativeException("Speed negativo");
		}
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public int getCurrentSpeed() {
		return this.currentSpeed;
	}

	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public Road getRoad() {
		return road;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) { // created just for testting by atuch and mr.chen
		this.location = location;
	}

	public int getContaminationClass() {
		return contaminationClass;
	}

	void setContaminationClass(int c) throws NegativeException {
		if (c > 10 || c < 0) {
			throw new NegativeException("Valor de ContaminationClass erroneo");
		} else {
			contaminationClass = c;
		}
	}

	public int getMaximunSpeed() {
		return maximunSpeed;
	}

	

	public int getTotalContamination() {
		return totalContamination;
	}

	

	public int getTotalTravelledDistance() {
		return totalTravelledDistance;
	}

	public Junction getJunction () {
		return this.itinerary.get(getIndexCruce());
		
	}

	public int getIndexCruce() {
		return indexCruce;
	}

	
}
