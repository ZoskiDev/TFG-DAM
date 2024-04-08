package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;

public class GraphicalUserInterfaceSendMessages extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox <String>comboBox_SelCanal = new JComboBox<String>();
	private JTextArea textArea;
	private Map<String, String> id_channels;
	private GraphicalUserInterfaceLogic logicaPadre;
	
	/**
	 * Create the frame.
	 */
	public GraphicalUserInterfaceSendMessages(GraphicalUserInterfaceLogic logica) {
		logicaPadre = logica;
		id_channels = new HashMap<String, String>();
		setup();
	}
	public void fillJComboBox(Server toFill) {
		setTitle("Mandando mensaje en: " + toFill.getName());
		List <ServerTextChannel> canales = toFill.getTextChannels();
		for(ServerTextChannel canal: canales) {
			id_channels.put(canal.getName(), canal.getIdAsString());
			comboBox_SelCanal.addItem(canal.getName());
		}
		
	}
	private void setup() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		
		pan_north.add(comboBox_SelCanal);
		
		//Fin pan_north
			
		//Inicio pan_center
			JPanel pan_center = new JPanel();
			contentPane.add(pan_center, BorderLayout.CENTER);
			pan_center.setLayout(new BorderLayout(0, 0));
			
			textArea = new JTextArea();
			pan_center.add(textArea, BorderLayout.CENTER);
		//Fin pan_center
			
		//Inicio pan_south
			
			JPanel pan_south = new JPanel();
			contentPane.add(pan_south, BorderLayout.SOUTH);
			
			JButton btn_exit = new JButton("Salir");
			pan_south.add(btn_exit);
			
			JButton btn_sendMessage = new JButton("Enviar");
			btn_sendMessage.setMnemonic(KeyEvent.VK_ENTER);
			pan_south.add(btn_sendMessage);
			
			JButton btn_clearMessage = new JButton("Borrar");
			pan_south.add(btn_clearMessage);
			
		//Fin pan_south
			
		//Inicio listeners
		
			btn_sendMessage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logicaPadre.sendMessageToBot(textArea.getText(), id_channels.get(comboBox_SelCanal.getSelectedItem()));
					clearText();
				}
			});
			
			btn_exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearText();
					setVisible(false);
					comboBox_SelCanal.removeAllItems();
				}
			});
			btn_clearMessage.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					clearText();
				}
			});
			
		//Fin listeners
	}
	private void clearText() {
		textArea.setText("");
	}

}
