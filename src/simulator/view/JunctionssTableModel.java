package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionssTableModel extends AbstractTableModel implements TrafficSimObserver{

	
	private static final long serialVersionUID = 1L;
	
	
	private List<Junction> junction;
	private String[] _colNames = { "Id", "Green", "Queues"};

	public JunctionssTableModel(Controller _ctrl) {
		junction= new ArrayList<Junction>();
		_ctrl.addObserver(this);
	}

	public void update() {
		fireTableDataChanged();;		
	}
	
	public void setEventsList(List<Junction> junction) {
		this.junction = junction;
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
		return junction == null ? 0 : junction.size();
	}

	@Override	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {		
		case 0:
			s = junction.get(rowIndex).getId();
			break;
		case 1:
			s = junction.get(rowIndex).getGreenLightIndex();
			break;
		case 2:
			s = junction.get(rowIndex).getListaCarreterasEntrantes();
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
		junction=map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		junction=map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		junction=map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		junction=map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onError(String err) {
		// Auto-generated method stub		
	}
}
