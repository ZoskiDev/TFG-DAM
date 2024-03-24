package botlogic;

import java.util.concurrent.CancellationException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
/**
 * @author Zyssk0
 * Clase creada para representar al bot de discord, encargado de llevar a cabo las ordenes de la aplicacion
 * */
public class O5O {
	private String token = "MTIyMTQ4MzYyOTMzMTQ4MDc2Ng.Gk7Uod.JwjKrwqSQk_auy1XlFFFOoHqf-bpnKtY-7PN8c";

	private DiscordApi api;
	
	public O5O() {
		startBot();
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
		//TODO programar Logs para la excepcion quizas un log
			System.out.println("He petao tope guapo");
		}
	}

	public DiscordApi getApi() {
		return api;
	}
	
}
