package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		
			
		// Inicio de sector de menubar
		
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			
			JMenu mn_opciones_server = new JMenu("Opciones Servidor");
			mn_opciones_server.setHorizontalAlignment(SwingConstants.LEFT);
			menuBar.add(mn_opciones_server);
			
			JMenuItem mntm_seleccionar_servidor = new JMenuItem("Seleccionar servidor");
			mn_opciones_server.add(mntm_seleccionar_servidor);
			
			JMenu mn_opciones_1 = new JMenu("Herramientas");
			mn_opciones_1.setHorizontalAlignment(SwingConstants.LEFT);
			menuBar.add(mn_opciones_1);
			
			JMenuItem mntm_mandar_mensaje_1 = new JMenuItem("Mandar Mensaje");
			mn_opciones_1.add(mntm_mandar_mensaje_1);
			
			JMenuItem mntm_ = new JMenuItem("New menu item");
			mn_opciones_1.add(mntm_);
		
		//Fin de sector de menubar
		
		
		
		// Inicio de sector central consola logs
			
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			
			JTextArea textAreaLOG = new JTextArea();
			textAreaLOG.setEditable(false);
			textAreaLOG.setRows(20);
			textAreaLOG.setColumns(60);
			getContentPane().add(textAreaLOG, BorderLayout.WEST);
		
		// Fin de sector central consola logs	
		
		// Inicio de sector del panel sur
			
			JPanel pan_south = new JPanel();
			getContentPane().add(pan_south, BorderLayout.SOUTH);
			pan_south.setLayout(new GridLayout(1, 2));
			
			lbl_status = new JLabel("Status:");
			lbl_status.setVerticalAlignment(SwingConstants.TOP);
			lbl_status.setHorizontalAlignment(SwingConstants.LEFT);
			pan_south.add(lbl_status);
			
			lbl_server = new JLabel("Servidor Seleccionado:");
			lbl_server.setHorizontalAlignment(SwingConstants.LEFT);
			pan_south.add(lbl_server);
			
		// Fin de sector de panel sur
			
		//Inicio sector listeners
			mntm_seleccionar_servidor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logica.setActiveServer();
					if(logica.getActiveServerName() == null && !logica.isServerActive()) {
						JOptionPane.showMessageDialog(null, "Error, no se ha seleccionado ningun servidor", "Error servidor", JOptionPane.ERROR_MESSAGE);
						return;
					}
						lbl_server.setText("Servidor Seleccionado: " + logica.getActiveServerName());
				}
			});
			
			mntm_mandar_mensaje_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!logica.isServerActive()) {
						JOptionPane.showMessageDialog(null, "Selecciona un servidor antes para mandar un mensaje", "Error servidor", JOptionPane.ERROR_MESSAGE);
						return;
					}
					logica.startMessagesGUI();	
				}
			});
		//Fin sector listeners
	}
	public GraphicalUserInterfaceO5O( ) {
		getContentPane().setBackground(Color.GRAY);
		setBackground(Color.WHITE);
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
