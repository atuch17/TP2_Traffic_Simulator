package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.misc.Pair;
import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private boolean _stopped;
	private JToolBar barraBotones;
	private JFileChooser fileChooser;
	private JSpinner ticks;
	private JButton load;
	private JButton changeCo2;
	private JButton changeWeather;
	private JButton b_play;
	private JButton b_stop;
	private JButton cerrar;

	private int time;
	private RoadMap roadMap;

	public ControlPanel(Controller _ctrl) {
		super();
		barraBotones = new JToolBar();
		fileChooser = new JFileChooser(
				"C:\\hlocal\\workspace (programacion)\\GitHub\\TrafficSimulatorTP\\resources\\examples");//TODO cambiar
		// fileChooser.setCurrentDirectory(new File("/Temp/"));
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 500, 1)); // value, min, max, step
		load = new JButton();
		changeCo2 = new JButton();
		changeWeather = new JButton();
		b_play = new JButton();
		b_stop = new JButton();
		cerrar = new JButton();

		this._ctrl = _ctrl;
		this._stopped = false;
		
		_ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		this.add(barraBotones); // BorderLayout.WEST ?

		/* BOTONES: Carga del fichero de eventos */
		boton1();

		/* BOTONES: Cambio de la clase de contaminación de un vehículo */
		boton2();

		/* BOTONES: Cambio de las condiciones atmosféricas de una carretera */
		boton3();

		/* BOTONES: Ticks */
		boton4();

		/* BOTONES: Ejecución */
		boton5();

		/* BOTONES: Parada */
		boton6();

		/* BOTONES: Cerrar */
		boton7();

		// LOS AnADIMOS
		barraBotones.add(load);
		barraBotones.addSeparator(); // Para darle el efecto de separar los botones
		barraBotones.add(changeCo2);
		barraBotones.add(changeWeather);
		barraBotones.addSeparator();
		barraBotones.add(b_play);
		barraBotones.add(b_stop);
		barraBotones.addSeparator();
		barraBotones.add(new JLabel("Ticks: "));
		barraBotones.add(ticks);
		barraBotones.add(Box.createGlue()); // Para que este el la misma linea
		barraBotones.addSeparator();
		barraBotones.add(cerrar);
	}

	private void boton1() {
		ImageIcon icono1 = new ImageIcon("resources/icons/open.png");
		load.setIcon(icono1);
		load.setToolTipText("Carga el fichero que tu elijas");		
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON file", "json"));
				int returnValue = fileChooser.showOpenDialog(fileChooser);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					try {
						Main.set_inFile(fileChooser.getSelectedFile().getName());
						_ctrl.reset();
						_ctrl.loadEvents(new FileInputStream(fileChooser.getSelectedFile()));
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Error al abrir archivo");
					}
				}
			}
		});
	}

	private void boton2() {
		ImageIcon icono2 = new ImageIcon("resources/icons/co2class.png");
		changeCo2.setIcon(icono2);
		changeCo2.setToolTipText("Cambia de la clase de contaminacion de un vehiculo");
		changeCo2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeCO2();
			}
		});
	}

	private void changeCO2() {
		ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog(roadMap, (Frame) SwingUtilities.getWindowAncestor(this));
		int status = dialog.getEstado();

		if (status == 1) { // probar
			try {
				List<Pair<String, Integer>> cs = new ArrayList<>();

				cs.add(new Pair<String, Integer>(dialog.getDialogVeh().getId(), dialog.getDialogCO2()));

				time = time + dialog.getNumTicks();
				_ctrl.addEvent(new NewSetContClassEvent(time, cs));

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al obtener el resultado del boton.");
			}
		}

	}

	private void boton3() {
		ImageIcon icono3 = new ImageIcon("resources/icons/weather.png");
		changeWeather.setIcon(icono3);
		changeWeather.setToolTipText("Cambia de la clase de contaminacion de un vehiculo");
		changeWeather.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeWeather();
			}
		});
	}

	private void changeWeather() {
		ChangeWeatherDialog dialog =  new ChangeWeatherDialog(roadMap, (Frame) SwingUtilities.getWindowAncestor(this));
		int status = dialog.getEstado();
		
		if (status == 1) { // probar
			try {
				List<Pair<String, Weather>> cs = new ArrayList<>();

				cs.add(new Pair<String, Weather>(dialog.getDialogR().getId(), dialog.getDialogW()));

				time = time + dialog.getNumTicks();
				_ctrl.addEvent(new SetWeatherEvent(time, cs));

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al obtener el resultado del boton.");
			}
		}
	}

	private void boton4() {
		Dimension d = new Dimension(80, 40);
		ticks.setMaximumSize(d);
		ticks.setMinimumSize(d);
		ticks.setPreferredSize(d);
	}

	private void boton5() {
		ImageIcon icono4 = new ImageIcon("resources/icons/run.png");
		b_play.setIcon(icono4);
		b_play.setToolTipText("Que empiece el juego!");
		b_play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				start();
			}

		});
	}

	private void boton6() {
		ImageIcon icono5 = new ImageIcon("resources/icons/stop.png");
		b_stop.setIcon(icono5);
		b_stop.setToolTipText("Pausa el juego");
		b_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stop();
			}
		});
	}

	private void boton7() {
		ImageIcon icono6 = new ImageIcon("resources/icons/exit.png");
		cerrar.setIcon(icono6);
		cerrar.setToolTipText("Cierra el programa");
		cerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ret = JOptionPane.showConfirmDialog(null, "Realmente desea salir?", "Confirmar salida",
						JOptionPane.YES_NO_OPTION, 2);
				if (ret == JOptionPane.YES_OPTION) {
					System.exit(0);
				}

			}
		});
	}

	private void start() {
		_stopped = false;
		enableToolBar(false);
		run_sim((Integer) ticks.getValue());
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1, null);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al ejecutar el programa");
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}

	private void enableToolBar(boolean b) {
		load.setEnabled(b);
		changeCo2.setEnabled(b);
		changeWeather.setEnabled(b);
		b_play.setEnabled(b);
	}

	private void stop() {
		_stopped = true;
	}


	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.roadMap = map;
		this.time = time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.roadMap = map;
		this.time = time;

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// Auto-generated method stub
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.roadMap = map;
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.roadMap = map;
		this.time = time;
	}

	@Override
	public void onError(String err) {
		// Auto-generated method stub
	}

}
