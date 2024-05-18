package graphicalUserInterface;




import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.Permissions;

import botlogic.O5O;

/**
 * @author Zyssk0
 * 
 * @info Clase encargada de representar la logica de la interfaz en el modelo vista controlador del proyecto
 * 
 * */
public class GraphicalUserInterfaceLogic {
	private static Logger logger = LogManager.getLogger(GraphicalUserInterfaceLogic.class);
	private GraphicalUserInterfaceSelectServer select_server;
	private GraphicalUserInterfaceSendMessages send_messages;
	private GraphicalUserInterfaceBadWords bad_words;
	private O5O bot = new O5O();
	
	/**
	 * Constructor de la clase GraphicalUserInterfaceLogic
	 * 
	 * este constructor se encarga de lanzar las interfaces hijas en un segundo plano y tenerlas en espera hasta la necesaria utilizacion de las mismas
	 * */
	public GraphicalUserInterfaceLogic() {
		select_server = new GraphicalUserInterfaceSelectServer(bot.getApi().getServers(), this);
		send_messages = new GraphicalUserInterfaceSendMessages(this);
		bad_words = new GraphicalUserInterfaceBadWords(this);
		createPathBadWords();
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
	 * Metodo setActiveServer
	 * 
	 * este metodo se encarga de lanzar la interfaz GraphicalUserInterfaceSelectServer
	 * 
	 * */
	public void setActiveServer() {
		try{
			select_server.setVisible(true);
			bot.setServidor_seleccionado(select_server.getServidor_seleccionado().get());
			select_server.getServidor_seleccionado().ifPresent(bot::setServidor_seleccionado);
		}catch(Exception e) {			
			if(!isServerActive())
				bot.setServidor_seleccionado(null);
		}
	}
	
	/**
	 * Metodo getActiveServer
	 * 
	 * este metodo se encarga de recibir el nombre del servidor donde se esta trabajando actualmente
	 * 
	 * @return el nombre del objeto servidor en el que se este trabajando, si lo hay
	 * 
	 * @return null si no existe un servidor en el que se este trabajando.
	 * */
	public String getActiveServerName() {
		String serverName = "";
		try {
			serverName =  bot.getServidor_seleccionado().getName();
		}catch(Exception e) {
			logger.error("Excepcion capturada en metodo: getActiveServerName() " + e.toString() + " retornando valor null");
			return null;
		}
		return serverName;
	}
	
	/**
	 * Metodo para indicar si hay un servidor activo para trabajar
	 * 
	 * @return true si lo hay
	 * 
	 * @return false si no lo hay
	 * */
	public boolean isServerActive() {
		return bot.isServerSetted();
	}
	
	/**
	 * startMessagesGUI
	 * 
	 * metodo encargado de cargar los canales de un servidor y lanzar la clase GraphicalUserInterfaceSendMessages
	 * */
	public void startMessagesGUI() {
			send_messages.fillJComboBox(bot.getServidor_seleccionado());
			send_messages.setVisible(true);
		}
	/**
	 * sendMessageToBot
	 * 
	 * metodo encargado de dar la señal al bot para mandar un mensaje
	 * 
	 * @param message como mensaje a mandar
	 * 
	 * @param channelID como ID del canal al que mandar el mensaje
	 * */
	public void sendMessageToBot(String message, String channelID) {
		bot.sendMessage(message, channelID);
	}
	/**
	 * getInviteLink
	 * 
	 * este metodo se encarga de dar una invitacion CON TODOS LOS PERMISOS
	 * 
	 * sus salidas son tanto por consola como por navegador
	 * 
	 * */
	public void getInviteLink() {
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
		String txtName = getActiveServerName() + "badWords.txt";
		File badWordstxt = new File("badWords/" + txtName);
		if(!badWordstxt.exists()) {
			try {
				badWordstxt.createNewFile();
			} catch (IOException e) {
				logger.error("Error de entrada salida a la hora de crear el archivo de badwords >" + e.toString());
				e.printStackTrace();
			}
		}
		bad_words.loadBadWords(badWordstxt);
		bad_words.setVisible(true);
	}
	private void createPathBadWords() {
		File badWordsPath = new File  ("badWords/");
		if(!badWordsPath.exists())
			badWordsPath.mkdir();
	}
}
