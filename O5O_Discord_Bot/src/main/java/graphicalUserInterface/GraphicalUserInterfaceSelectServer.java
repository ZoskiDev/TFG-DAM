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

/**
 * 
 * @author Zyssk0
 * Clase GraphicalUserInterfaceSelectServer, esta clase representa un JDialog para seleccionar un servidor con el cual trabajar 
 * 
 * */
public class GraphicalUserInterfaceSelectServer extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboBoxServidor = new JComboBox<String>();
	private Map<String, String> idServidores;
	private Optional<Server> servidorSeleccionado = null;
	private GraphicalUserInterfaceLogic logicaPadre;
	/**
	 * Create the dialog.
	 */
	public GraphicalUserInterfaceSelectServer(Set <Server> servidores, GraphicalUserInterfaceLogic logica) {
		setTitle("Seleccion servidor");
		logicaPadre = logica;
		idServidores = new HashMap<String, String>();
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
			idServidores.put(serv.getName(), serv.getIdAsString());
			comboBoxServidor.addItem(serv.getName());
		}
		
		
	}
	
	public void eliminarServidor(String servidorEliminar) {
		comboBoxServidor.removeItem(servidorEliminar);
		idServidores.remove(servidorEliminar);
	}
	
	public void aniadirServidor(Server servidorAniadir) {
		idServidores.put(servidorAniadir.getName(), servidorAniadir.getIdAsString());
		comboBoxServidor.addItem(servidorAniadir.getName());
	}
	private void setup() {
		setModal(true);
		setBounds(100, 100, 286, 195);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lbl_SelectServer = new JLabel("Selecciona servidor:");
			lbl_SelectServer.setFont(new Font("Tahoma", Font.PLAIN, 24));
			contentPanel.add(lbl_SelectServer);
		}
		{
			comboBoxServidor.setEditable(true);
			comboBoxServidor.setMaximumRowCount(50);
			contentPanel.add(comboBoxServidor);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnOK = new JButton("OK");
				btnOK.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(comboBoxServidor != null)
							setServidorSeleccionado(logicaPadre.getBotApi().getServerById(idServidores.get(comboBoxServidor.getSelectedItem())));
							setVisible(false);
					}
				});
				btnOK.setActionCommand("OK");
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
			}
			{
				JButton btnCancelar = new JButton("Cancelar");
				btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setServidorSeleccionado(null);
						setVisible(false);
					}
				});
				buttonPane.add(btnCancelar);
			}
		}
	}
	public Optional<Server> getServidorSeleccionado() {
		return servidorSeleccionado.or(() -> Optional.empty());
	}
	public void setServidorSeleccionado(Optional<Server> servidor_seleccionado) {
		this.servidorSeleccionado = servidor_seleccionado;
	}

}
