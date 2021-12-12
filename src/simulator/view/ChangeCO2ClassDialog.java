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

import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RoadMap roadMap;
	private int estado;
	private Dimension di;
	private JSpinner changeTicks;
	private JComboBox<Vehicle> changeVeh;
	private JComboBox<Integer> changeCO2;
	private DefaultComboBoxModel<Vehicle> _defaultComboV;	
	private DefaultComboBoxModel<Integer> _defaultComboCO2;	
	
	public ChangeCO2ClassDialog(RoadMap roadMap, Frame parent) {
		super(parent, true);		
		this.roadMap=roadMap;
		this.estado = -1;	
		this.di = new Dimension(80, 20);
		initGUI();
	}
	
	private void initGUI() {
		setTitle("Change CO2 Class");
		
		JPanel panelDescripcion = new JPanel();
		//panelDescripcion.setLayout(new BoxLayout(panelDescripcion, BoxLayout.Y_AXIS));
		panelDescripcion.add(new JLabel ("Selecciona un evento para cambiar la clase CO2 de un vehiculo."));
		
		JPanel panelCuerpo = new JPanel();
		panelCuerpo.add( new JLabel ("Vehicle: "));	
		_defaultComboV=new DefaultComboBoxModel<>();		
		changeVeh = new JComboBox<>(_defaultComboV);
		changeVeh.setPreferredSize(di);
		
		
		panelCuerpo.add(changeVeh);
		
		panelCuerpo.add(Box.createGlue());		//para separar
		
		panelCuerpo.add( new JLabel ("CO2 Class: "));
		_defaultComboCO2=new DefaultComboBoxModel<>();
		changeCO2 = new JComboBox<>(_defaultComboCO2);
		changeCO2.setPreferredSize(di);		
		
		for(int i=0;i<11;i++)
			_defaultComboCO2.addElement(i);

		panelCuerpo.add(changeCO2);
		
		panelCuerpo.add(Box.createGlue());		//para separar
		
		panelCuerpo.add( new JLabel ("Ticks: "));
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
		
		bOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {							
						estado = 1;
						setVisible(false);				 
			}					 
		});
		
		
		setSize(500,200);
		add(panelDescripcion);
		panelDescripcion.add(Box.createGlue());
		panelDescripcion.add(panelCuerpo);
		panelDescripcion.add(panelBotones);
		setVisible(false);
	}	
	
	
	
	public int getEstado() {
		if(roadMap !=null) {
			List<Vehicle> listaVeh = roadMap.getVehicles();
			_defaultComboV.removeAllElements();
			for(Vehicle elements: listaVeh) {
				_defaultComboV.addElement(elements);
			}
		}
		setVisible(true);
		return estado;
	}
	//////////////////////////////////////
	public int getDialogCO2() {
		return (Integer) changeCO2.getSelectedItem();
	}	

	public int getNumTicks() {
		return (Integer) changeTicks.getValue();
	}	

	public Vehicle getDialogVeh() {
		return (Vehicle) changeVeh.getSelectedItem();
	}
	
}
