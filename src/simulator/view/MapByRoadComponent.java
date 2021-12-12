package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private static final Color _BLACK_ROAD = Color.BLACK;

	private Image[] imagenes;

	private RoadMap _map;

	private Image _car;
	private Image _clima;

	private int x1;
	private int x2;
	private int y;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);

	}

	private void initGUI() {
		setPreferredSize(new Dimension(300, 200));

		_car = loadImage("car.png");

		imagenes = new Image[6];
		imagenes[0] = loadImage("cont_0.png");
		imagenes[1] = loadImage("cont_1.png");
		imagenes[2] = loadImage("cont_2.png");
		imagenes[3] = loadImage("cont_3.png");
		imagenes[4] = loadImage("cont_4.png");
		imagenes[5] = loadImage("cont_5.png");
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		x1 = 50;
		x2 = getWidth() - 100;
		
		int i = 0;
		for (Road r : _map.getRoads()) {
			y = (i + 1) * 50;

			g.setColor(_BLACK_ROAD);
			g.drawString(r.getId(), 20, y);
			g.drawLine(x1, y, x2, y);

			drawRoads(g, r);
			drawVehicles(g, r);
			drawClima(g,r);
			drawContamination(g,r);
			
			i++;
		}

	}
	
	private void drawContamination(Graphics g, Road road) {		
		int C=0;

        C = ( int ) Math.floor(Math.min(( double ) road.getTotalCO2()/(1.0 + ( double ) road.getCO2Limit()),1.0) / 0.19);
        
		g.drawImage(imagenes[C], x2+60, y-16, 32, 32, this);
	}
	
	private void drawClima(Graphics g, Road road) {
		if (road.getWeatherConditions() == Weather.CLOUDY)
			_clima = loadImage("cloud.png");
		else if (road.getWeatherConditions() == Weather.RAINY)
			_clima = loadImage("rain.png");
		else if (road.getWeatherConditions() == Weather.STORM)
			_clima = loadImage("storm.png");
		else if (road.getWeatherConditions() == Weather.SUNNY)
			_clima = loadImage("sun.png");
		else if (road.getWeatherConditions() == Weather.WINDY)
			_clima = loadImage("wind.png");		
		
		
		g.drawImage(_clima, x2+20, y-16, 32, 32, this);
	}

	private void drawRoads(Graphics g, Road road) {
		
		g.setColor(_JUNCTION_COLOR);
		g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(road.getSource().getId(), x1, y - 10);
		
		Color arrowColor = _RED_LIGHT_COLOR;
		int idx = road.getDestination().getGreenLightIndex();
		if (idx != -1 && road.equals(road.getDestination().getListaCarreterasEntrantes().get(idx))) {
			arrowColor = _GREEN_LIGHT_COLOR;
		}

		g.drawString(road.getDestination().getId(), x2, y - 10);
		g.setColor(arrowColor);
		g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

	}

	private void drawVehicles(Graphics g, Road road) {
		for (Vehicle v : road.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) road.getLength()));
				g.setColor(new Color(0, 0,0,0));  //lo hago transparente
				g.fillOval(x - 1, y - 10, 14, 14);
				g.drawImage(_car, x, y - 15, 22, 22, this);
				g.drawString(v.getId(), x, y - 10);
			}
		}
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
