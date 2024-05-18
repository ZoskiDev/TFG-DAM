package botlogic;

import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
/**
 * @author Zyssk0
 * 
 * Clase que representa el behaviour del bot a la hora de que un usuario abandone un servidor
 * */
public class UserLeaveListenerManager implements ServerMemberLeaveListener{

	private static Logger logger = LogManager.getLogger(UserLeaveListenerManager.class);
	
	@Override
	public void onServerMemberLeave(ServerMemberLeaveEvent event) {
		try {
			logger.info("El usuario: " + event.getUser().getName() + " ha abandonado el servidor: " + event.getServer().getName());
			PrivateChannel pc = event.getUser().openPrivateChannel().get();
			pc.sendMessage(String.format("Lamentamos que hayas abandonado: " + event.getServer().getName()));
		} catch (InterruptedException e) {
			logger.error("Excepcion capturada" + e.toString());
		} catch (ExecutionException e) {
			logger.error("Excepcion capturada" + e.toString());
		}
	}
}
