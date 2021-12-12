package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Vehicle> vehicle;
	private String[] _colNames = { "Id", "Location", "Iterinary","CO2 Class", "Max.Speed", "Speed", "Total CO2", "Disstance" };

	public VehiclesTableModel(Controller _ctrl) {
		vehicle= new ArrayList<Vehicle>();
		_ctrl.addObserver(this);
	}

	public void update() {		
		fireTableDataChanged();;		
	}
	
	public void setEventsList(List<Vehicle> vehicle) { 		
		this.vehicle = vehicle;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	public int getRowCount() {
		return vehicle == null ? 0 : vehicle.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {		
		case 0:
			s = vehicle.get(rowIndex).getId();
			break;
		case 1:
            if (vehicle.get(rowIndex).getStatus() == VehicleStatus.PENDING) {

                s = "Pending";
            } else if (vehicle.get(rowIndex).getStatus() == VehicleStatus.TRAVELING) {
                s = vehicle.get(rowIndex).getRoad() + ": "+ vehicle.get(rowIndex).getLocation();
            } else if (vehicle.get(rowIndex).getStatus() == VehicleStatus.WAITING) {
                s = "Waiting: " + vehicle.get(rowIndex).getJunction(); //TODO
            } else {
                s = "Arrived";
            }
            break;
		case 2:
			s = vehicle.get(rowIndex).getItinerary();
			break;
		case 3:
			s = vehicle.get(rowIndex).getContaminationClass();
			break;
		case 4:
			s = vehicle.get(rowIndex).getMaximunSpeed();
			break;
		case 5:
			s = vehicle.get(rowIndex).getCurrentSpeed();
			break;
		case 6:
			s = vehicle.get(rowIndex).getTotalContamination();
			break;
		case 7:
			 s = vehicle.get(rowIndex).getTotalTravelledDistance();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
		vehicle=map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {		
		vehicle=map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {		
		vehicle=map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		vehicle=map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// Auto-generated method stub		
	}
}
