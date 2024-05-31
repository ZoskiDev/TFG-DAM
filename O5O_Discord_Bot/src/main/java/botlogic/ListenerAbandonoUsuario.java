package botlogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
/**
 * @author Zyssk0
 * 
 * Clase que representa el comportamiento del bot a la hora de que un usuario abandone un servidor
 * */
public class ListenerAbandonoUsuario implements ServerMemberLeaveListener{

	private static Logger logger = LogManager.getLogger(ListenerAbandonoUsuario.class);
	
	@Override
	public void onServerMemberLeave(ServerMemberLeaveEvent event) {
		logger.info("El usuario: " + event.getUser().getName() + " ha abandonado el servidor: " + event.getServer().getName());

	}
}
