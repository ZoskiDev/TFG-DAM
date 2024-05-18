package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.javacord.api.entity.server.Server;

import botlogic.O5O;

/**
 * 
 * @author Zyssk0
 * Clase GraphicalUserInterfaceSelectServer, esta clase representa un JDialog para seleccionar un servidor con el cual trabajar 
 * 
 * */
public class GraphicalUserInterfaceSelectServer extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboBox_servidor = new JComboBox<String>();
	private Map<String, String> id_servers;
	private Optional<Server> servidor_seleccionado = null;
	private GraphicalUserInterfaceLogic logicaPadre;
	/**
	 * Create the dialog.
	 */
	public GraphicalUserInterfaceSelectServer(Set <Server> servidores, GraphicalUserInterfaceLogic logica) {
		setTitle("Seleccion servidor");
		logicaPadre = logica;
		id_servers = new HashMap<String, String>();
		fillComboBox(servidores);
		setup();
	}
	/**
	 * Metodo fillComboBox, este metodo se encargara de llenar la comboBox con los servidores que actualmente estan incorporando al bot O5O
	 * */
	private void fillComboBox(Set <Server> s) {
		// TODO Auto-generated method stub
		Iterator <Server> servidores = s.iterator();
		while(servidores.hasNext()) {
			Server serv = servidores.next();
			id_servers.put(serv.getName(), serv.getIdAsString());
			comboBox_servidor.addItem(serv.getName());
		}
		
		
	}
	private void setup() {
		setModal(true);
		setBounds(100, 100, 286, 195);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lbl_Select_Server = new JLabel("Selecciona servidor:");
			lbl_Select_Server.setFont(new Font("Tahoma", Font.PLAIN, 24));
			contentPanel.add(lbl_Select_Server);
		}
		{
			comboBox_servidor.setEditable(true);
			comboBox_servidor.setMaximumRowCount(50);
			contentPanel.add(comboBox_servidor);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(comboBox_servidor != null)
							setServidor_seleccionado(logicaPadre.getBotApi().getServerById(id_servers.get(comboBox_servidor.getSelectedItem())));
							setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setServidor_seleccionado(null);
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	public Optional<Server> getServidor_seleccionado() {
		return servidor_seleccionado;
	}
	public void setServidor_seleccionado(Optional<Server> servidor_seleccionado) {
		this.servidor_seleccionado = servidor_seleccionado;
	}

}
