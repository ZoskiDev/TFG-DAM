package botlogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

import graphicalUserInterface.GraphicalUserInterfaceLogic;
/**
 * @author Zyssk0
 * Clase creada para representar al bot de discord O5O, encargado de llevar a cabo las ordenes de la aplicacion
 * */
public class O5O {
	private static Logger logger = LogManager.getLogger(O5O.class);
	File tokenFile = new File("token.txt");
	private String token;
	private GraphicalUserInterfaceLogic logicaHija;

	private  DiscordApi api;
	private Server servidor_seleccionado;
	
	/**
	 * Constructor de la clase O5O, este constructor no recibe ningun parametro de entrada
	 * */
	public O5O(GraphicalUserInterfaceLogic logicaHija) {
		try(BufferedReader lector = new BufferedReader(new FileReader(tokenFile))){
			token = lector.readLine();
			this.logicaHija = logicaHija;
		} catch (FileNotFoundException e) {
			logger.fatal("ERROR FATAL, NO SE ENCUENTRA ARCHIVO TOKEN.TXT PARA INICIALIZAR BOT, ABORTANDO!!!");
			System.exit(1);
			e.printStackTrace();
		} catch (IOException e) {
			logger.fatal("ERROR FATAL, NO SE HA CONSEGUIDO LEER EL ARCHIVO TOKEN.TXT, ASEGURATE QUE SEA UNA UNICA LINEA CON EL TOKEN!!!");
			System.exit(1);
			e.printStackTrace();
		}
		startBot();
		startStartUpListeners();
		startSlashCommands();
	}
	
	/**
	 * Metodo startBot
	 * 
	 * este metodo se encarga de inicializar la api de discord para que el bot inicialice
	 * @throws CancellationException si la conexion no se ha realizado satisfactoriamente
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
				.setDefaultEnabledForPermissions(PermissionType.ADMINISTRATOR, PermissionType.MODERATE_MEMBERS)
				);
		builders.add(
				SlashCommand.with("conseguirpfp", "consigue la pfp del usuario que desees",
						Arrays.asList(
								SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "ocio", "consigue la foto de perfil del usuario que desees",
			                            Arrays.asList(
			                                SlashCommandOption.create(SlashCommandOptionType.USER, "usuario", "usuario al que deseas conseguir la pfp", true)                          
			        ))))
				);
		api.bulkOverwriteGlobalApplicationCommands(builders).join();
		api.addSlashCommandCreateListener(event -> {
			SlashCommandsManager.slashManager(event.getSlashCommandInteraction(), this.getApi());
		}
		);
	}
	public void startStartUpListeners() {
		ServerOnJoinOrLeaveListener srvrjoinleave = new ServerOnJoinOrLeaveListener(logicaHija);
		api.addServerMemberJoinListener(new UserJoinListenerManager());
		api.addServerMemberLeaveListener(new UserLeaveListenerManager());
		api.addMessageCreateListener(new BadWordsFilter());
		api.addServerJoinListener(srvrjoinleave);
		api.addServerLeaveListener(srvrjoinleave);
	}
	/**
	 * Retorna la clase api del bot
	 * 
	 * @return la api en cuestion
	 * */
	public  DiscordApi getApi() {
			return api;
	}
	/**
	 * Comprueba si existe un servidor seleccionado
	 * 
	 * @return true si hay servidor seleccionado
	 * @return false si no hay un servidor seleccionado
	 * */
	public boolean isServerSetted() {
		if (getServidor_seleccionado() != null)
			return true;
		return false;
	}
	
	/**
	 * Retorna el servidor seleccionado si es que lo hay
	 * 
	 * @return el servidor seleccionado para trabajar
	 * @return null si no hay un servidor seleccionado
	 * */
	public Server getServidor_seleccionado() {
		return servidor_seleccionado;
	}

	/**
	 * Metodo para cambiar el servidor a trabajar, si existe uno previamente se sobreescribe.
	 * 
	 * @param Server servidor a trabajar 
	 * */
	public void setServidor_seleccionado(Server servidor_seleccionado) {
			this.servidor_seleccionado = servidor_seleccionado;
	}
	
	/**
	 * Metodo para mandar un mensaje en un canal de un servidor especifico
	 * 
	 * @param Mensaje a mandar
	 * @param ID del canal al cual mandar el mensaje
	 * 
	 * */
	public void sendMessage(String toSend, String channelID) {
		api.getTextChannelById(channelID).ifPresentOrElse(x -> {
				new MessageBuilder()
				.setContent(toSend)
				.send(api.getTextChannelById(channelID).get());
				logger.info("mensaje correctamente enviado a canal con ID:" + channelID + " contenido: " + toSend);
				}
				,() ->  logger.error("Error a la hora de enviar mensaje, canal: " + channelID + " no encontrado"));
		
		
	}

	
}
