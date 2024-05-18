package graphicalUserInterface;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * @author Zyssk0
 * 
 * Esta clase representa la interfaz encargada de mostrar la blacklist de palabras no deseadas (badWords)
 * */
public class GraphicalUserInterfaceBadWords extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel		contentPane;
	private JTextArea	textArea;
	private JLabel		lbl_servidor;
	private GraphicalUserInterfaceLogic logicaPadre;
	private String		serverName = "";
	private File		fileTXT;
	
	/**
	 * Create the frame.
	 */
	public GraphicalUserInterfaceBadWords(GraphicalUserInterfaceLogic logica) {
		setModal(true);
		setTitle("BadWords");
		logicaPadre = logica;
		setup();
	}

	private void setup() {
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
			textArea.setToolTipText("Cada linea representa una badword a eliminar, ten en cuenta esto a la hora de configurar el filtro");
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
			btn_exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearText();
					setVisible(false);
				}
			});
			btn_uploadBadWords.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!serverName.isBlank())
						writeToFile(fileTXT);
						setVisible(false);
						clearText();
					}
			});
			btn_clearBadWords.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearText();
				}
			});

	        // Añadir un WindowListener para manejar el evento de cierre
	        this.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	                clearText();
	            }
	        });
		//Fin listeners
	}
	/**
	 * Metodo loadBadWords
	 * 
	 * este metodo se encarga de cargar en la interfaz grafica el archivo de texto que se envia desde la GUI padre
	 * 
	 * @param archivo (de texto plano) a cargar
	 * 
	 * */
	public void loadBadWords(File toShow) {
		if(logicaPadre.isServerActive()) { 
			serverName = logicaPadre.getActiveServerName();
			lbl_servidor.setText("Mostrando BadWords de:  " + serverName);
		}
		fileTXT = toShow;
		fillTextArea(fileTXT);
	}
	/**
	 * Metodo fillTextArea
	 * 
	 * este metodo se encarga de mostrar el contenido del archivo de texto plano en la interfaz
	 * 
	 * @param toFill siendo el archivo a mostrar en la interfaz grafica
	 * 
	 * @exception FileNotFoundException si el archivo se ha enviado de manera equivoca
	 * 
	 * @exception IOException si el archivo no se puede leer (no es un txt)
	 * */
	private void fillTextArea(File toFill) {
		try(BufferedReader reader = new BufferedReader(new FileReader(toFill))){
			String linea;
			while((linea = reader.readLine()) != null) {
				textArea.append(linea + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		}
	/**
	 * Metodo writeToFile
	 * 
	 * Este metodo se encarga de salvar los cambios que se hayan hecho en el texto de la interfaz y cargarlos al archivo de procedencia
	 * 
	 * @param archivo en el que guardar los cambios
	 * 
	 * Se sobreentiende que cada linea es una palabra o palabras juntas que no se desean en ese servidor
	 * 
	 * @exception IOException si el archivo proporcionado no se puede escribir
	 * */
	private void writeToFile(File toWrite) {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(toWrite, false));) {
			writer.write(textArea.getText());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void clearText() {
		textArea.setText("");
	}
	
}
