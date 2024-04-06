package graphicalUserInterface;

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
		select_server = new GraphicalUserInterfaceSelectServer(O5O.getApi().getServers());
		send_messages = new GraphicalUserInterfaceSendMessages();
	}
	/**
	 * Metodo para comprobar si el bot ha inicializado correctamente
	 * @return true si esta inicializado
	 * @return false si no se ha inicializado correctamente
	 * */
	public boolean isBotReady() {
		if(O5O.getApi() != null)
			return true;
		return false;
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
	public boolean isServerActive() {
		if (bot.getServidor_seleccionado() != null)
				return true;
		return false;
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
	public void sendMessages() {
		send_messages.setVisible(true);	
		send_messages.fillJComboBox(bot.getServidor_seleccionado());
	}
}
