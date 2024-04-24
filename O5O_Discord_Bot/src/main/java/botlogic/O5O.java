package botlogic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CancellationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
/**
 * @author Zyssk0
 * Clase creada para representar al bot de discord, encargado de llevar a cabo las ordenes de la aplicacion
 * */
public class O5O {
	private static Logger logger = LogManager.getLogger(O5O.class);
	private String token = "MTIyMTQ4MzYyOTMzMTQ4MDc2Ng.Gk7Uod.JwjKrwqSQk_auy1XlFFFOoHqf-bpnKtY-7PN8c";

	private  DiscordApi api;
	private Server servidor_seleccionado;
	
	public O5O() {
		startBot();
		startSlashCommands();
	}
	
	/**
	 * Metodo startBot
	 * 
	 * este metodo se encarga de inicializar la api de discord para que el bot inicialice
	 * */
	public void startBot() {
		api = null;
		try {
		api =  new DiscordApiBuilder()
				.setToken(token)
				.setAllIntents()
				.login().join();
		}catch(CancellationException cancE) {
			logger.fatal("ERROR FATAL EN LA CONEXIÓN CON LA API");
		}
	}
	/**
	 * Metodo startSlashCommands
	 * 
	 * este metodo es el encargado de inicializar de manera inmediata los slash commands globables bloqueando el hilo principal
	 * */
	public void startSlashCommands() {
		Set<SlashCommandBuilder> builders = new HashSet<>();
		builders.add(
		SlashCommand.with("servidor", "comandos dedicados al servidor",
	            Arrays.asList(
	                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND_GROUP, "moderacion", "comandos especificos moderacion",
	                    Arrays.asList(
	                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "silenciar", "silencia a un usuario durante un tiempo determinado",
	                            Arrays.asList(
	                            	SlashCommandOption.create(SlashCommandOptionType.USER, "usuario", "usuario al que silenciar", true),
		                            SlashCommandOption.create(SlashCommandOptionType.LONG, "tiempo", "tiempo en minutos a los que silenciar al usuario", true),
		                            SlashCommandOption.create(SlashCommandOptionType.STRING, "razon", "razon por la que se silencia al usuario", false)		   
	        ))))))
        	.setDefaultEnabledForPermissions(PermissionType.ADMINISTRATOR, PermissionType.MODERATE_MEMBERS)
        	);
		
		builders.add(
				SlashCommand.with("borrarbloque", "borrar mensajes en bloque",
						Arrays.asList(
								SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "mensajes", "borrar mensajes de un canal en especifico",
			                            Arrays.asList(
			                                SlashCommandOption.create(SlashCommandOptionType.LONG, "cantidad", "cantidad de mensajes que borrar desde el ultimo", true),
			                                SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "canal", "canal especifico que borrar los mensajes", false)		                                
			        ))))
				);
		api.bulkOverwriteGlobalApplicationCommands(builders).join();
		api.addSlashCommandCreateListener(event -> {
			SlashCommandsManager.slashManager(event.getSlashCommandInteraction(), this.getApi());
		}
		);
	}
	public  DiscordApi getApi() {
			return api;
	}

	public Server getServidor_seleccionado() {
		return servidor_seleccionado;
	}

	public void setServidor_seleccionado(Server servidor_seleccionado) {
		this.servidor_seleccionado = servidor_seleccionado;
	}
	
	public void sendMessage(String toSend, String channelID) {
		new MessageBuilder().setContent(toSend).send(api.getTextChannelById(channelID).get());
		logger.info("mensaje correctamente enviado a canal con ID:" + channelID + " contenido: " + toSend);
	}
	
}
