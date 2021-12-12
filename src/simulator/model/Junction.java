package simulator.model;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.NegativeException;
import exceptions.StatusException;

public class Junction extends SimulatedObject{

    private List<Road> listaCarreterasEntrantes;
    private Map<Junction, Road> mapaCarreterasSalientes; 			//para saber que carretera seleccionar en los cruces
    private Map<Road, List<Vehicle>> mapaCola;
    private List<List<Vehicle>> listaColas;
    private int intSemaforoVerde;    								// -1 si TODAS estan en rojo
    private int ultPasoCambioSemaforo=0;
    private LightSwitchingStrategy estrategiaCambioSemaforo;    	//cambiar el color del semaforo
    private DequeuingStrategy estrategiaExtraerElementosCola;    	//para eliminar vehiculos
    private int x;       											//para dibujar el cruce
    private int y;

    Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws NegativeException { //habria que poner protected delante?
        super(id);

        if(lsStrategy==null || dqStrategy==null || xCoor<0 || yCoor<0) {
            throw new NegativeException("Algun argumento de Junction es erroneo!");
        }
        
        listaCarreterasEntrantes = new ArrayList<Road>();
        listaColas = new ArrayList<List<Vehicle>>();
        mapaCarreterasSalientes = new HashMap<Junction, Road>();
        mapaCola = new HashMap<Road, List<Vehicle>>();
        this.x = xCoor;
        this.y = yCoor;
        this.estrategiaCambioSemaforo = lsStrategy;
        this.estrategiaExtraerElementosCola = dqStrategy;
        this.intSemaforoVerde=-1;//correcto?        
        
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public JSONObject report() {
    	JSONObject jo= new JSONObject();
		jo.put("id", getId());
		
		if(intSemaforoVerde==-1)
			jo.put("green", "none");
		else
			jo.put("green", listaCarreterasEntrantes.get(intSemaforoVerde).getId());
		
		
		JSONArray ja1 = new JSONArray();
		jo.put("queues", ja1);
		
		for (Road r: listaCarreterasEntrantes) {
			JSONObject jo_aux= new JSONObject();
			ja1.put(jo_aux);
			jo_aux.put("road", r.getId());
			
			JSONArray ja2 = new JSONArray();
			jo_aux.put("vehicles", ja2);
			
			for(Vehicle v: mapaCola.get(r)) {
				ja2.put(v.getId());
			}
			
		}
		
		return jo;
    }

    @Override
    void advance(int time) throws StatusException, NegativeException {
    	if (intSemaforoVerde != -1) {
    		System.out.println(intSemaforoVerde);
    		List<Vehicle> q = listaColas.get(intSemaforoVerde);
    		System.out.println(listaColas.size());
    		for (Vehicle v: estrategiaExtraerElementosCola.dequeue(q)) {
    			v.moveToNextRoad();
    			q.remove(v);
    		}
    	}
    	
    	int next = estrategiaCambioSemaforo.chooseNextGreen(listaCarreterasEntrantes, listaColas, intSemaforoVerde, ultPasoCambioSemaforo, time);
    	if (next != intSemaforoVerde) {
    		intSemaforoVerde = next;
    		ultPasoCambioSemaforo = time;
    	}
    }
    
    void addIncommingRoad(Road r) {
        List<Vehicle> cola = new LinkedList<Vehicle>();
        listaCarreterasEntrantes.add(r);
        listaColas.add(cola);
        mapaCola.put(r, cola);

    }

    void addOutGoingRoad(Road r) {
    	this.mapaCarreterasSalientes.put(r.getDestination(), r);       
    }

    void enter(Vehicle v) {		
    	Road r = v.getRoad();
    	List<Vehicle> q = mapaCola.get(r);
    	q.add(v);
    }

    Road roadTo(Junction j) {        														
        return mapaCarreterasSalientes.get(j);
    }

	public int getGreenLightIndex() {
		
		return intSemaforoVerde;
	}

	public List<Road> getListaCarreterasEntrantes() {
		return listaCarreterasEntrantes;
	}


	}