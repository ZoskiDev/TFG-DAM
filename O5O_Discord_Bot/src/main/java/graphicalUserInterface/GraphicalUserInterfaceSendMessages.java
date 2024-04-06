package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;

public class GraphicalUserInterfaceSendMessages extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox <String>comboBox_selmensaje = new JComboBox<String>();
	private Map<String, String> id_channels;
	
	/**
	 * Create the frame.
	 */
	public GraphicalUserInterfaceSendMessages() {
		setup();
	}
	public void fillJComboBox(Server toFill) {
		List <ServerTextChannel> canales = toFill.getTextChannels();
		for(ServerTextChannel canal: canales) {
			id_channels.put(canal.getName(), canal.getIdAsString());
			comboBox_selmensaje.addItem(canal.getName());
		}
		
	}
	private void setup() {
		setTitle("Mandar Mensajes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		//Inicio pan_north
		
		JPanel pan_north = new JPanel();
		contentPane.add(pan_north, BorderLayout.NORTH);
		
		JLabel lbl_servidor = new JLabel("Mandando mensaje en:  ");
		lbl_servidor.setFont(new Font("Tahoma", Font.PLAIN, 24));
		pan_north.add(lbl_servidor);
		
		
		pan_north.add(comboBox_selmensaje);
		
		//Fin pan_north
			
		//Inicio pan_center
			JPanel pan_center = new JPanel();
			contentPane.add(pan_center, BorderLayout.CENTER);
			pan_center.setLayout(new BorderLayout(0, 0));
			
			JTextArea textArea = new JTextArea();
			pan_center.add(textArea, BorderLayout.CENTER);
		//Fin pan_center
			
		//Inicio pan_south
			
			JPanel pan_south = new JPanel();
			contentPane.add(pan_south, BorderLayout.SOUTH);
			
			JButton btn_cancelMessage = new JButton("Cancelar");
			btn_cancelMessage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			
			JButton btn_borrar = new JButton("Borrar");
			pan_south.add(btn_borrar);
			pan_south.add(btn_cancelMessage);
			
			JButton btn_sendMessage = new JButton("Enviar");
			pan_south.add(btn_sendMessage);
			
		//Fin pan_south
	}

}
