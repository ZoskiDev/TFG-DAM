package graphicalUserInterface;




import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;

import botlogic.O5O;

/**
 * @author Zyssk0
 * 
 * @info Clase encargada de representar la logica de la interfaz en el modelo vista controlador del proyecto
 * 
 * */
public class GraphicalUserInterfaceLogic {
	private static Logger logger = LogManager.getLogger(GraphicalUserInterfaceLogic.class);
	private GraphicalUserInterfaceSelectServer seleccionarServidor;
	private GraphicalUserInterfaceSendMessages mandarMensajes;
	private GraphicalUserInterfaceBadWords badWords;
	private O5O bot;
	
	/**
	 * Constructor de la clase GraphicalUserInterfaceLogic
	 * 
	 * este constructor se encarga de lanzar las interfaces hijas en un segundo plano y tenerlas en espera hasta la necesaria utilizacion de las mismas
	 * */
	public GraphicalUserInterfaceLogic() {
		bot = new O5O(this);
		seleccionarServidor = new GraphicalUserInterfaceSelectServer(bot.getApi().getServers(), this);
		mandarMensajes = new GraphicalUserInterfaceSendMessages(this);
		badWords = new GraphicalUserInterfaceBadWords(this);
		crearRutaBadWords();
	}
	/**
	 * Metodo para comprobar si el bot ha inicializado correctamente
	 * @return true si esta inicializado
	 * @return false si no se ha inicializado correctamente
	 * */
	public boolean isBotReady() {
		logger.info("Comprobando estado de la API de O5O...");
		if(bot.getApi() != null) {
			logger.info("Conexión establecida de manera exitosa");
			return true;
			}
		logger.fatal("Error a la hora de establecer la conexión con el bot");
		return false;
	}
	
	/**
	 * Metodo getBotApi
	 * 
	 * @return la api del bot en la que se esta trabajando
	 * */
	public  DiscordApi getBotApi() {
		return bot.getApi();
	}
	/**
	 * Metodo setServidorSeleccionado
	 * 
	 * este metodo se encarga de lanzar la interfaz GraphicalUserInterfaceSelectServer
	 * 
	 * */
	public void setServidorSeleccionado() {
		try{
			seleccionarServidor.setVisible(true);
			bot.setServidorSeleccionado(seleccionarServidor.getServidorSeleccionado().get());
			seleccionarServidor.getServidorSeleccionado().ifPresent(bot::setServidorSeleccionado);
		}catch(Exception e) {			
			if(!isServidorActivo())
				bot.setServidorSeleccionado(null);
		}
	}
	
	/**
	 * Metodo getNombreServidorSeleccionado
	 * 
	 * este metodo se encarga de recibir el nombre del servidor donde se esta trabajando actualmente
	 * 
	 * @return el nombre del objeto servidor en el que se este trabajando, si lo hay
	 * 
	 * @return null si no existe un servidor en el que se este trabajando.
	 * */
	public String getNombreServidorSeleccionado() {
		String serverName = "";
		try {
			if(bot.getServidorSeleccionado() != null)
				serverName = bot.getServidorSeleccionado().getName();
		}catch(NullPointerException e) {
			logger.error("no existe valor de servidor seleccionado, retornando null");
			return null;
		}
		return serverName;
	}
	
	/**
	 * Metodo isServidorActivo
	 * 
	 * metodo utilizado para indicar si hay un servidor activo para trabajar
	 * 
	 * @return true si lo hay
	 * 
	 * @return false si no lo hay
	 * */
	public boolean isServidorActivo() {
		return bot.isServidorSeleccionado();
	}
	
	/**
	 * iniciarGUIMensajes
	 * 
	 * metodo encargado de cargar los canales de un servidor y lanzar la clase GraphicalUserInterfaceSendMessages
	 * */
	public void iniciarGUIMensajes() {
			mandarMensajes.fillJComboBox(bot.getServidorSeleccionado());
			mandarMensajes.setVisible(true);
		}
	/**
	 * mandarMensajeBot
	 * 
	 * metodo encargado de dar la señal al bot para mandar un mensaje
	 * 
	 * @param mensaje como mensaje a mandar
	 * 
	 * @param idCanal como ID del canal al que mandar el mensaje
	 * */
	public void mandarMensajeBot(String mensaje, String idCanal) {
		bot.enviarMensaje(mensaje, idCanal);
	}
	/**
	 * getlinkInvitacion
	 * 
	 * este metodo se encarga de dar una invitacion CON TODOS LOS PERMISOS
	 * 
	 * sus salidas son tanto por consola como por navegador
	 * 
	 * */
	public void getlinkInvitacion() {
		String link = bot.getApi().createBotInvite(Permissions.fromBitmask(888));
		System.out.println("link de invitacion de O5O: " + link);
		try {
			Desktop.getDesktop().browse(new URI(link));
		} catch (IOException e) {
			logger.error("Excepcion capturada > " + e.toString());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			logger.error("Error en el envio de la URI para navegador > " + e.toString());
			e.printStackTrace();
		}
	}
	/**
	 * startBadWordsGUI
	 * 
	 * este metodo trabaja en dos partes
	 * 
	 * la primera es creando un archivo de configuracion de badWords si no existe uno previamente
	 * 
	 * en caso de que exista, lo manda a la interfaz GraphicalUserInterfaceBadWords
	 * */
	public void startBadWordsGUI() {
		String txtName = getNombreServidorSeleccionado() + "badWords.txt";
		File badWordstxt = new File("badWords/" + txtName);
		if(!badWordstxt.exists()) {
			try {
				badWordstxt.createNewFile();
			} catch (IOException e) {
				logger.error("Error de entrada salida a la hora de crear el archivo de badwords >" + e.toString());
				e.printStackTrace();
			}
		}
		badWords.cargarBadWords(badWordstxt);
		badWords.setVisible(true);
	}
	private void crearRutaBadWords() {
		File rutaBadWords = new File  ("badWords/");
		if(!rutaBadWords.exists())
			rutaBadWords.mkdir();
	}
	public void quitarServidorLista(String servidorEliminar) {
		seleccionarServidor.eliminarServidor(servidorEliminar);
		//Si el servidor que se ha ido es el seleccionado en el momento
		if(bot.getServidorSeleccionado().getName().equalsIgnoreCase(servidorEliminar)) {
			bot.setServidorSeleccionado(null);
			GraphicalUserInterfaceO5O.setVoidToServidorSeleccionado();
		}
	}
	public void aniadirServidorLista(Server servidorAniadir) {
		seleccionarServidor.aniadirServidor(servidorAniadir);
	}
}
