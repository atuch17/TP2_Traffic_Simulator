package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RoadMap roadMap;
	private int estado;
	private Dimension di;
	private JSpinner changeTicks;
	private JComboBox<Road> changeRoad;
	private JComboBox<Weather> changeWeather;
	private DefaultComboBoxModel<Road> _defaultComboR;
	private DefaultComboBoxModel<Weather> _defaultComboW;

	public ChangeWeatherDialog(RoadMap roadMap, Frame parent) {
		super(parent, true);
		this.roadMap = roadMap;
		this.estado = -1;
		this.di = new Dimension(80, 20);
		initGUI();
	}

	private void initGUI() {
		setTitle("Change Road Weather");

		JPanel panelDescripcion = new JPanel();
		// panelDescripcion.setLayout(new BoxLayout(panelDescripcion,
		// BoxLayout.Y_AXIS));
		panelDescripcion.add(new JLabel("Selecciona la carretera y el tiempo que quieres cambiar en X pasos."));

		JPanel panelCuerpo = new JPanel();
		panelCuerpo.add(new JLabel("Road: "));
		_defaultComboR = new DefaultComboBoxModel<>();
		changeRoad = new JComboBox<>(_defaultComboR);
		changeRoad.setPreferredSize(di);
		//

		panelCuerpo.add(changeRoad);

		panelCuerpo.add(Box.createGlue()); // para separar

		panelCuerpo.add(new JLabel("Weather: "));
		_defaultComboW = new DefaultComboBoxModel<>();
		changeWeather = new JComboBox<>(_defaultComboW);
		changeWeather.setPreferredSize(di);

		panelCuerpo.add(changeWeather);

		panelCuerpo.add(Box.createGlue()); // para separar

		panelCuerpo.add(new JLabel("Ticks: "));
		changeTicks = new JSpinner();
		changeTicks.setPreferredSize(di);
		panelCuerpo.add(changeTicks);

		JPanel panelBotones = new JPanel();
		JButton bCancel = new JButton("Cancel");
		JButton bOK = new JButton("Ok");
		panelBotones.add(bCancel);
		panelBotones.add(bOK);
		bCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				estado = 0;
				setVisible(false);
			}
		});
		/* metodo que devuelva el estado 0k/cancel */
		bOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				estado = 1;
				setVisible(false);
			}
		});

		setSize(500, 200);
		add(panelDescripcion);
		panelDescripcion.add(Box.createGlue());
		panelDescripcion.add(panelCuerpo);
		panelDescripcion.add(panelBotones);
		setVisible(false);
	}

	public int getEstado() {

		List<Road> listaR = roadMap.getRoads();
		_defaultComboR.removeAllElements();
		for (Road elements : listaR) {
			_defaultComboR.addElement(elements);

		}		
		
		//Weather States
		_defaultComboW.addElement(Weather.CLOUDY);
		_defaultComboW.addElement(Weather.RAINY);
		_defaultComboW.addElement(Weather.STORM);
		_defaultComboW.addElement(Weather.SUNNY);
		_defaultComboW.addElement(Weather.WINDY);

		setVisible(true);
		return estado;
	}

	public Weather getDialogW() {
		return (Weather) changeWeather.getSelectedItem();
	}

	public int getNumTicks() {
		return (Integer) changeTicks.getValue();
	}

	public Road getDialogR() {
		return (Road) changeRoad.getSelectedItem();
	}

}
