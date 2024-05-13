package graphicalUserInterface;




import java.awt.Desktop;
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
	private O5O bot = new O5O();
	
	public GraphicalUserInterfaceLogic() {
		select_server = new GraphicalUserInterfaceSelectServer(bot.getApi().getServers(), this);
		send_messages = new GraphicalUserInterfaceSendMessages(this);
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
	
	public  DiscordApi getBotApi() {
		return bot.getApi();
	}
	public void setActiveServer() {
		try{
			select_server.setVisible(true);
			bot.setServidor_seleccionado(select_server.getServidor_seleccionado().get());  
		}catch(Exception e) {
			logger.error("Excepcion capturada en metodo: setActiveServer() " + e.toString() + " retornando valor null");
			if(!isServerActive())
				bot.setServidor_seleccionado(null);
		}
	}
	
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
	
	public boolean isServerActive() {
		if (bot.getServidor_seleccionado() != null)
				return true;
		return false;
	}
	
	public void startMessagesGUI() {
			send_messages.fillJComboBox(bot.getServidor_seleccionado());
			send_messages.setVisible(true);
		}
	
	public void sendMessageToBot(String message, String channelID) {
		bot.sendMessage(message, channelID);
	}
	public void getInviteLink() {
		String link = bot.getApi().createBotInvite(Permissions.fromBitmask(888));
		System.out.println("link de invitacion de O5O" + link);
		try {
			Desktop.getDesktop().browse(new URI(link));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
