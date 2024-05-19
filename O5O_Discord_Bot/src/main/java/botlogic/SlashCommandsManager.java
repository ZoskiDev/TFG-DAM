package botlogic;

import java.time.Duration;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

/**
 * @author Zyssk0
 * Clase SlashCommandsManager, esta clase representa el comportamiento (behaviour) que debe tomar cada slash al ser invocado mediante
 * el cliente de discord
 * @see O5O.java para los Slash Existentes
 * */
public class SlashCommandsManager {
/* TODO Refactorizar el if con un switch dentro (no tiene mucho sentido...)
 * TODO Encapsular herramientas de moderacion (timeout, delete etc... en una subclase para evitar lineas largas como la 38)
 * */
	private static Logger logger = LogManager.getLogger(SlashCommandsManager.class);
	/**
	 * slashManager, este metodo esta creado para el behaviour de cada slash
	 * 
	 * @param SlashCommandInteraction como comando ejecutado
	 * 
	 * @param DiscordApi como api del bot que debe ejecutar el programa
	 * */
	public static void slashManager(SlashCommandInteraction command, DiscordApi api) {
		
		
		if(command.getCommandName().equals("servidor")) {
			Optional <Server>	servidorComandoEjecutado = null;
			Optional <User>		usuarioArgumento = null;
			try {
				if(command.getServer().isPresent() && command.getArgumentByIndex(0).isPresent()) {
					servidorComandoEjecutado = command.getServer();
					usuarioArgumento = command.getArgumentUserValueByIndex(0);
				}
				else {
					logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE CARGAR EL SERVIDOR Y LOS ARGUMENTOS DEL SLASH COMMAND:");
					return;
				}
			}catch(Exception e) {
				logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE CARGAR LOS ARGUMENTOS DEL SLASH COMMAND: " + e.toString());
				return;
			}
			
			
			
			switch (command.getFullCommandName()){
			case "servidor moderacion silenciar":
				//Implementar en una clase que sea exclusivamente comandos de moderacion??
				User usuarioTimeout = usuarioArgumento.get();
				final String nombreUsuario = usuarioTimeout.getName();
				Long minutos = command.getArgumentLongValueByIndex(1).orElse((long)-3);
				Server servidorTimeout = servidorComandoEjecutado.get();
				
				//Si podemos silenciar al usuario y si la cantidad es valida:
				if(servidorTimeout.canYouTimeoutUser(usuarioTimeout) && (minutos > 0 && minutos < 10081)){
					
					usuarioTimeout.timeout(servidorTimeout, Duration.ofMinutes(command.getArgumentLongValueByIndex(1).get()))
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
				}
				else {
					logger.error("El usuario: " + usuarioTimeout.getName() + " no puede ser silenciado");
					command.createImmediateResponder().setContent("Error! no se ha podido silenciar al usuario: " + usuarioTimeout.getName() + " es probable que no posea los permisos necesarios en este servidor o se hayan introducido mal los minutos")
					.setFlags(MessageFlag.EPHEMERAL).respond();
				}
				break;
			}
		}
		
		
		else if(command.getCommandName().equals("borrarbloque")) {
			if(command.getServer().get().canYouManage()) {
				Long cantidadBorrar = command.getArgumentLongValueByIndex(0).orElse((long)-3);
				TextChannel canalBorrar = null;
				if(!(cantidadBorrar > 0 && cantidadBorrar < Integer.MAX_VALUE)) {
					logger.fatal("ABORTANDO SLASH: la cantidad de minutos no ha sido valida");
					command.createImmediateResponder().setContent("Debes introducir una cantidad de mensajes a borrar valida!")
					.setFlags(MessageFlag.EPHEMERAL).respond();
				}
				try {		
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
					mset = canalBorrar.getMessages((int) (cantidadBorrar + 1)).get();
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
			else {
				logger.error("Error a la hora de borrar mensajes en: " + command.getServer().get() + " Permisos insuficientes");
				command.createImmediateResponder().setContent("Error! no se pueden borrar mensajes, es probable que no posea los permisos necesarios en este servidor")
				.setFlags(MessageFlag.EPHEMERAL).respond();
			}
			
		}
		
		
		
		else if(command.getCommandName().equals("conseguirpfp")) {
			if (command.getArgumentUserValueByIndex(0).isEmpty()) {
				System.out.println("NO HA ENCONTRADO USUARIO");
				command.createImmediateResponder().setContent("No se ha proporcionado usuario objetivo")
				.setFlags(MessageFlag.EPHEMERAL)
				.respond();
			}
			else {
				User usuarioObjetivo = command.getArgumentUserValueByIndex(0).get();
				
				String toSend = command.getUser().getName() + " aqui tienes la imagen de: " + usuarioObjetivo.getName();
				
				//TODO que no sea una bazofia de respond inmediato y se quede pensando hasta que tengas bien mandada la imagen
				command.createImmediateResponder().setContent("Recibido, en breves tendras la imagen!").setFlags(MessageFlag.EPHEMERAL).respond();
					EmbedBuilder emb = new EmbedBuilder()
					.setTitle(toSend)
					.setImage(usuarioObjetivo.getAvatar());
				command.getChannel().get().sendMessage(emb);
				
				logger.info("Invocado conseguirPFP:" +
						" Invocador: " + command.getUser().getName() + 
						" Objetivo " + usuarioObjetivo.getName());
			}
		}
	}//slashmanager()
}//clase 
