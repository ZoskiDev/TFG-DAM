package botlogic;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

public class SlashCommandsManager {
/* TODO Refactorizar el if con un switch dentro (no tiene mucho sentido...)
 * TODO Encapsular herramientas de moderacion (timeout, delete etc... en una subclase para evitar lineas largas como la 38)
 * */
	private static Logger logger = LogManager.getLogger(SlashCommandsManager.class);
	public static void slashManager(SlashCommandInteraction command, DiscordApi api) {
		if(command.getCommandName().equals("servidor")) {
			Optional <Server>	servidorComandoEjecutado = null;
			//ServerUpdater		updaterServidor = null;
			Optional <User>		usuarioArgumento = null;
			try {
				servidorComandoEjecutado = command.getServer();
				//updaterServidor = new ServerUpdater(servidorComandoEjecutado.get());
				usuarioArgumento = command.getArgumentUserValueByIndex(0);
			}catch(Exception e) {
				logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE CARGAR LOS ARGUMENTOS DEL SLASH COMMAND: " + e.toString());
				return;
			}
			switch (command.getFullCommandName()){
			case "servidor moderacion silenciar":
				//Implementar en una clase que sea exclusivamente comandos de moderacion??
				final String nombreUsuario = usuarioArgumento.get().getName();
				usuarioArgumento.get().timeout(servidorComandoEjecutado.get(), Duration.ofMinutes(command.getArgumentLongValueByIndex(1).get()))
				.thenAccept(message -> {
					String argumento = "";
					Optional <String> optional_argumento = command.getArgumentStringRepresentationValueByIndex(2);
					
					if(optional_argumento.isPresent()) 
						argumento = " Razon: " + optional_argumento.get() + ".";
					
					command.createImmediateResponder()
					.setContent("Usuario: " + nombreUsuario + " ha sido silenciad@ correctamente" + argumento)
					.respond();
					
					logger.info("Invocado canal moderacion silenciar:" +
							" Invocador: " + command.getUser().getName() + 
							" Usado contra: " + nombreUsuario + 
							" tiempo: " + command.getArgumentLongValueByIndex(1).get() + " minutos");
				});
				break;
			}
		}
		else if(command.getCommandName().equals("borrarbloque")) {
			long cantidadBorrar = 0;
			TextChannel canalBorrar = null;
			
			try {
				cantidadBorrar = command.getArgumentLongValueByIndex(0).get();
				if(command.getArgumentChannelValueByIndex(1).isPresent())
					canalBorrar = api.getTextChannelById(command.getArgumentChannelValueByIndex(1).get().getId()).get();
				else
					canalBorrar = command.getChannel().get();
			}catch(Exception e) {
				logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE CARGAR LOS ARGUMENTOS DEL SLASH COMMAND: " + e.toString());
				return;
			}
			MessageSet mset = null;
			try {
				mset = canalBorrar.getMessages((int) cantidadBorrar + 1).get();
			} catch (Exception e) {
				logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE RECUPERAR LOS MENSAJES DEL SERVIDOR: " + e.toString());
				return;
			}
			String idCanal = canalBorrar.getIdAsString();
			String cantBorrados = "" + cantidadBorrar;
			canalBorrar.deleteMessages(mset).thenAccept(message -> {
				command.createImmediateResponder()
				.setContent(cantBorrados + " Mensajes eliminados correctamente")
				.setFlags(MessageFlag.EPHEMERAL)
				.respond();
				logger.info("Invocado borrar bloque:" +
						" Invocador: " + command.getUser().getName() + 
						" cantidad a borrar: " + cantBorrados +
						" en canal: " + idCanal);
			});
			
		}
		
		else if(command.getCommandName().equals("conseguirpfp")) {
			User usuarioObjetivo = command.getArgumentUserValueByIndex(0).get();
			if (usuarioObjetivo == null) {
				System.out.println("NO HA ENCONTRADO USUARIO");
				command.createImmediateResponder().setContent("No se ha proporcionado usuario objetivo")
				.setFlags(MessageFlag.EPHEMERAL)
				.respond();
			}
			else {
				String toSend = command.getUser().getName() + " aqui tienes la imagen de: " + usuarioObjetivo.getName();
				//TODO que no sea una bazofia de respond inmediato y se quede pensando hasta que tengas bien mandada la imagen
				command.createImmediateResponder().setContent("Recibido, en breves tendras la imagen!").setFlags(MessageFlag.EPHEMERAL).respond();
					EmbedBuilder emb = new EmbedBuilder()
					.setTitle(toSend)
					.setImage(usuarioObjetivo.getAvatar());
				command.getChannel().get().sendMessage(emb);
				logger.info("Invocado borrar bloque:" +
						" Invocador: " + command.getUser().getName() + 
						" Objetivo " + usuarioObjetivo.getName());
			}
		}
	}
}
