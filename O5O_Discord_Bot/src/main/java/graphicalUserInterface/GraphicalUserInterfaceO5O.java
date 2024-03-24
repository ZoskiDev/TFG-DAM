package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * @author Zyssk0
 * Clase representada para la vista en el modelo del proyecto
 * */
public class GraphicalUserInterfaceO5O extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GraphicalUserInterfaceLogic logica;
	private Color 						colorVerde;
	private Color 						colorRojo;
	private JMenu						mn_opciones = new JMenu("Opciones");
	private JLabel						lbl_status;
	private JLabel						lbl_server;
	
	private void checkBotStatus() {
		if(logica.isBotReady()) {
			lbl_status.setText(lbl_status.getText() + " ONLINE");
			lbl_status.setForeground(colorVerde);
		}
		else {
			lbl_status.setText(lbl_status.getText() + " ERROR...");
			lbl_status.setForeground(colorRojo);
		}
	}
	private void setup() {
		JPanel pan_north = new JPanel();
		getContentPane().add(pan_north, BorderLayout.NORTH);
		
		mn_opciones.setHorizontalAlignment(SwingConstants.LEFT);
		pan_north.add(mn_opciones);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Probando 1");
		mn_opciones.add(mntmNewMenuItem);
		
		JPanel pan_south = new JPanel();
		getContentPane().add(pan_south, BorderLayout.SOUTH);
		
		lbl_status = new JLabel("Status:");
		lbl_status.setVerticalAlignment(SwingConstants.TOP);
		lbl_status.setHorizontalAlignment(SwingConstants.LEFT);
		pan_south.add(lbl_status);
		
		lbl_server = new JLabel("Servidor Seleccionado:");
		lbl_server.setHorizontalAlignment(SwingConstants.RIGHT);
		pan_south.add(lbl_server);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textAreaLOG = new JTextArea();
		textAreaLOG.setEditable(false);
		textAreaLOG.setRows(20);
		textAreaLOG.setColumns(60);
		getContentPane().add(textAreaLOG, BorderLayout.WEST);
	}
	public GraphicalUserInterfaceO5O( ) {
		colorVerde = new Color(22,208,59);
		colorRojo = new Color (211,43,13);
		setTitle("O5O G.U.I");
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup();
		setVisible(true);
		this.logica = new GraphicalUserInterfaceLogic();
		checkBotStatus();
	}

}
