package botlogic;

import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

/**
 * @author Zyssk0
 * 
 * Clase que representa el behaviour del bot a la hora de que se una un usuario a un servidor
 * */
public class UserJoinListenerManager implements ServerMemberJoinListener{

	private static Logger logger = LogManager.getLogger(UserJoinListenerManager.class);

	@Override
	public void onServerMemberJoin(ServerMemberJoinEvent event){
		try {
			logger.info("Se ha unido el usuario: " + event.getUser().getName() + " en el servidor: " + event.getServer().getName());
			PrivateChannel pc = event.getUser().openPrivateChannel().get();
			pc.sendMessage(String.format("Bienvenido a: " + event.getServer().getName() + " esperemos que disfrutes tu estancia, recuerda leer las normas (si existen...)"));
		} catch (InterruptedException e) {
			logger.error("Excepcion capturada" + e.toString());
		} catch (ExecutionException e) {
			logger.error("Excepcion capturada" + e.toString());
		}
	}
}
