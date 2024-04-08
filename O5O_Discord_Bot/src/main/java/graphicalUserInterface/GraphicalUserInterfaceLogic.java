package graphicalUserInterface;

import org.javacord.api.DiscordApi;

import botlogic.O5O;

/**
 * @author Zyssk0
 * Clase encargada de representar la logica de la interfaz en el modelo vista controlador del proyecto
 * 
 * */
public class GraphicalUserInterfaceLogic {
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
		if(bot.getApi() != null)
			return true;
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
			//TODO settear logins tope chulos con que no se ha seleccionado un servidor valido
			if(!isServerActive())
				bot.setServidor_seleccionado(null);
		}
	}
	
	public String getActiveServerName() {
		String serverName = "";
		try {
			serverName =  bot.getServidor_seleccionado().getName();
		}catch(Exception e) {
			//TODO settear logs
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
}
