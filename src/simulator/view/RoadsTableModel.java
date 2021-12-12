package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<Road> road;
	private String[] _colNames = { "Id", "Length", "Weather", "Max.Speed", "Speed Limit","Total CO2","CO2 Limit" };

	public RoadsTableModel(Controller _ctrl) {
		road= new ArrayList<Road>();
		_ctrl.addObserver(this);
	}

	public void update() {		
		fireTableDataChanged();;		
	}
	
	public void setEventsList(List<Road> road) {
		this.road = road;
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
		return road == null ? 0 : road.size();
	}

	@Override	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {		
		case 0:
			s = road.get(rowIndex).getId();
			break;
		case 1:
			s = road.get(rowIndex).getLength();
			break;
		case 2:
			s = road.get(rowIndex).getWeatherConditions();
			break;
		case 3:
			s = road.get(rowIndex).getMaximumSpeed();
			break;
		case 4:
			s = road.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			s = road.get(rowIndex).getTotalCO2();
			break;
		case 6:
			s = road.get(rowIndex).getCO2Limit();
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
		road=map.getRoads();
		fireTableDataChanged();
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		road=map.getRoads();
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		road=map.getRoads();
		fireTableDataChanged();
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		road=map.getRoads();
		fireTableDataChanged();
		
	}

	@Override
	public void onError(String err) {
		// Auto-generated method stub
		
	}
}
