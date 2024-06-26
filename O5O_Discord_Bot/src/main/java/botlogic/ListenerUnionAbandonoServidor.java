package botlogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.server.ServerEvent;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.api.listener.server.ServerJoinListener;
import org.javacord.api.listener.server.ServerLeaveListener;

import graphicalUserInterface.GraphicalUserInterfaceLogic;

public class ListenerUnionAbandonoServidor implements ServerJoinListener, ServerLeaveListener{

	private static Logger logger = LogManager.getLogger(ListenerUnionAbandonoServidor.class);
	private GraphicalUserInterfaceLogic logica;
	public ListenerUnionAbandonoServidor(GraphicalUserInterfaceLogic logica) {
		this.logica = logica;
	}

	@Override
	public void onServerLeave(ServerLeaveEvent event) {
		logger.info("O5O ha sido eliminado del servidor: " + event.getServer().getName() + " actualizando lista de servidores disponibles");
		logica.quitarServidorLista(event.getServer().getName());
	}

	@Override
	public void onServerJoin(ServerJoinEvent event) {
		logger.info("O5O ha sido añadido a un nuevo servidor: " + event.getServer().getName() + " actualizando lista de servidores disponibles");
		logica.aniadirServidorLista(event.getServer());
	}
	
	

}
