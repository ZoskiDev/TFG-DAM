package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class GraphicalUserInterfaceBadWords extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	private JLabel lbl_servidor;
	private GraphicalUserInterfaceLogic logicaPadre;
	private String serverName = "";
	
	/**
	 * Create the frame.
	 */
	public GraphicalUserInterfaceBadWords(GraphicalUserInterfaceLogic logica) {
		setTitle("BadWords");
		logicaPadre = logica;
		setup();
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
		
		lbl_servidor = new JLabel("Mostrando BadWords de:  ");
		lbl_servidor.setFont(new Font("Tahoma", Font.PLAIN, 24));
		pan_north.add(lbl_servidor);
		
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
			
			JButton btn_uploadBadWords = new JButton("Actualizar");
			btn_uploadBadWords.setMnemonic(KeyEvent.VK_ENTER);
			pan_south.add(btn_uploadBadWords);
			
			JButton btn_clearBadWords = new JButton("Borrar");
			pan_south.add(btn_clearBadWords);
			
		//Fin pan_south
			
		//Inicio listeners
			btn_clearBadWords.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearText();
				}
			});

		//Fin listeners
	}
	public void loadBadWords(File badWordstxt) {
		if(logicaPadre.isServerActive()) 
			serverName = logicaPadre.getActiveServerName();
			lbl_servidor.setText("Mostrando BadWords de:  " + serverName);
		fillTextArea(badWordstxt);
	}
	private void fillTextArea(File toFill) {
		try(BufferedReader reader = new BufferedReader(new FileReader(toFill))){
			String linea;
			while((linea = reader.readLine()) != null) {
				textArea.append(linea + "\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		}
	private void clearText() {
		textArea.setText("");
	}

}
