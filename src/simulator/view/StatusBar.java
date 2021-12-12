package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	//TODO al aniadir eventos creo que no pone correctamntte del todo el texto 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int tiempo=0;
	JLabel textoJLabel;
	JLabel eventJLabel;

	public StatusBar(Controller _ctrl) {
		super();
		textoJLabel = new JLabel();
		eventJLabel = new JLabel();
		_ctrl.addObserver(this);
		
		initGUI();
	}


	private void initGUI() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
				
		textoJLabel.setText("Tiempo: "+getTime());
		eventJLabel.setText("Evento: Vacio");
		
		this.add(textoJLabel);
		this.add(new JSeparator(JSeparator.VERTICAL));	//TODO 
		this.add(eventJLabel);
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setTime(time);
		textoJLabel.setText("Tiempo: "+getTime());
		eventJLabel.setText("Evento: Vacio");
		
		
		this.add(textoJLabel);
		this.add(eventJLabel);
		time++;
		
	}


	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {		
		this.add(textoJLabel);		
		this.add(eventJLabel);
	}


	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.add(textoJLabel);
		eventJLabel.setText("Evento: " + e.toString());
		this.add(eventJLabel);
		
	}


	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		time = 0;
		setTime(time);
		
	}


	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.add(textoJLabel);		
		this.add(eventJLabel);
	}


	@Override
	public void onError(String err) {
		this.add(textoJLabel);
		eventJLabel.setText("Evento: " + err.toString());		
		this.add(eventJLabel);
	}
	
	public int getTime() {
		return tiempo;
	}
	
	public int setTime(int time) {
		return tiempo=time;
	}
}
