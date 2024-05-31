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
	public static void slashManager(SlashCommandInteraction comando, DiscordApi api) {
		
		
		if(comando.getCommandName().equals("servidor")) {
			Optional <Server>	servidorComandoEjecutado = null;
			Optional <User>		usuarioArgumento = null;
			try {
				if(comando.getServer().isPresent() && comando.getArgumentByIndex(0).isPresent()) {
					servidorComandoEjecutado = comando.getServer();
					usuarioArgumento = comando.getArgumentUserValueByIndex(0);
				}
				else {
					logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE CARGAR EL SERVIDOR Y LOS ARGUMENTOS DEL SLASH COMMAND:");
					return;
				}
			}catch(Exception e) {
				logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE CARGAR LOS ARGUMENTOS DEL SLASH COMMAND: " + e.toString());
				return;
			}
			
			
			
			switch (comando.getFullCommandName()){
			case "servidor moderacion silenciar":
				//Implementar en una clase que sea exclusivamente comandos de moderacion??
				User usuarioTimeout = usuarioArgumento.get();
				final String nombreUsuario = usuarioTimeout.getName();
				Long minutos = comando.getArgumentLongValueByIndex(1).orElse((long)-3);
				Server servidorTimeout = servidorComandoEjecutado.get();
				
				//Si podemos silenciar al usuario y si la cantidad es valida:
				if(servidorTimeout.canYouTimeoutUser(usuarioTimeout) && (minutos > 0 && minutos < 10081)){
					
					usuarioTimeout.timeout(servidorTimeout, Duration.ofMinutes(comando.getArgumentLongValueByIndex(1).get()))
					.thenAccept(message -> {
						String argumento = "";
						Optional <String> optionalArgumento = comando.getArgumentStringRepresentationValueByIndex(2);
						
						if(optionalArgumento.isPresent()) 
							argumento = " Razon: " + optionalArgumento.get() + ".";
						
						comando.createImmediateResponder()
						.setContent("Usuario: " + nombreUsuario + " ha sido silenciad@ correctamente" + argumento)
						.respond();
						logger.info("Invocado canal moderacion silenciar:" +
								" Invocador: " + comando.getUser().getName() + 
								" Usado contra: " + nombreUsuario + 
								" tiempo: " + comando.getArgumentLongValueByIndex(1).get() + " minutos");
					});
				}
				else {
					logger.error("El usuario: " + usuarioTimeout.getName() + " no puede ser silenciado");
					comando.createImmediateResponder().setContent("Error! no se ha podido silenciar al usuario: " + usuarioTimeout.getName() + " es probable que no posea los permisos necesarios en este servidor o se hayan introducido mal los minutos")
					.setFlags(MessageFlag.EPHEMERAL).respond();
				}
				break;
			}
		}
		
		
		else if(comando.getCommandName().equals("borrarbloque")) {
			if(comando.getServer().get().canYouManage()) {
				int cantidadBorrar = Math.toIntExact(comando.getArgumentLongValueByIndex(0).get());
				TextChannel canalBorrar = null;
				if((cantidadBorrar <= 0 || cantidadBorrar > Integer.MAX_VALUE)) {
					logger.fatal("ABORTANDO SLASH: la cantidad de minutos no ha sido valida");
					comando.createImmediateResponder().setContent("Debes introducir una cantidad de mensajes a borrar valida!")
					.setFlags(MessageFlag.EPHEMERAL).respond();
					return;
				}
				//chequea si el argumento indicando canal existe, si no selecciona el canal donde se ha invocado
				try {		
					if(comando.getArgumentChannelValueByIndex(1).isPresent())
						canalBorrar = api.getTextChannelById(comando.getArgumentChannelValueByIndex(1).get().getId()).get();
					else
						canalBorrar = comando.getChannel().get();
				}catch(Exception e) {
					logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE CARGAR LOS ARGUMENTOS DEL SLASH COMMAND: " + e.toString());
					return;
				}
				
				MessageSet mset = null;
				try {
					mset = canalBorrar.getMessages(cantidadBorrar).get();
				} catch (Exception e) {
					logger.fatal("ABORTANDO: ERROR FATAL A LA HORA DE RECUPERAR LOS MENSAJES DEL SERVIDOR: " + e.toString());
					return;
				}
				String idCanal = canalBorrar.getIdAsString();
				String cantBorrados = "" + cantidadBorrar;
				
				comando.createImmediateResponder().setContent("Borrando " + cantBorrados + " mensaje/s, esto puede tomar unos segundos").setFlags(MessageFlag.EPHEMERAL).respond();
				
				canalBorrar.deleteMessages(mset);
					logger.info("Invocado borrar bloque:" +
							" Invocador: " + comando.getUser().getName() + 
							" cantidad a borrar: " + cantBorrados +
							" en canal: " + idCanal);
			}
			else {
				logger.error("Error a la hora de borrar mensajes en: " + comando.getServer().get() + " Permisos insuficientes");
				comando.createImmediateResponder().setContent("Error! no se pueden borrar mensajes, es probable que no posea los permisos necesarios en este servidor")
				.setFlags(MessageFlag.EPHEMERAL).respond();
			}
			
		}
		
		
		
		else if(comando.getCommandName().equals("conseguirpfp")) {
			if (comando.getArgumentUserValueByIndex(0).isEmpty()) {
				System.out.println("NO HA ENCONTRADO USUARIO");
				comando.createImmediateResponder().setContent("No se ha proporcionado usuario objetivo")
				.setFlags(MessageFlag.EPHEMERAL)
				.respond();
			}
			else {
				User usuarioObjetivo = comando.getArgumentUserValueByIndex(0).get();
				
				String mensajeRespuesta = comando.getUser().getName() + " aqui tienes la imagen de: " + usuarioObjetivo.getName();
				
				//TODO que no sea una bazofia de respond inmediato y se quede pensando hasta que tengas bien mandada la imagen
				comando.createImmediateResponder().setContent("Recibido, en breves tendras la imagen!").setFlags(MessageFlag.EPHEMERAL).respond();
					EmbedBuilder emb = new EmbedBuilder()
					.setTitle(mensajeRespuesta)
					.setImage(usuarioObjetivo.getAvatar());
				comando.getChannel().get().sendMessage(emb);
				
				logger.info("Invocado conseguirPFP:" +
						" Invocador: " + comando.getUser().getName() + 
						" Objetivo " + usuarioObjetivo.getName());
			}
		}
	}//slashmanager()
}//clase 
