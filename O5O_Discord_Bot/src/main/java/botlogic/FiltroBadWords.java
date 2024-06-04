package botlogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class FiltroBadWords implements MessageCreateListener{

	private File badWordsTXT;
	private static Logger logger = LogManager.getLogger(FiltroBadWords.class);

	public FiltroBadWords() {
		super();
	}
	private boolean compruebaSiExisteArchivo(MessageCreateEvent event) {
		
		event.getServer().ifPresent(x  ->
		badWordsTXT = new File("badWords/" + event.getServer().get().getName() + "badWords.txt"));
		if (!badWordsTXT.exists())
			return false;
		return true;
	}
	private void compruebaBadWords(File listaNegra, MessageCreateEvent event) {
		Pattern pat;
		Matcher mat;
		String message = event.getMessageContent();
		
		try(BufferedReader lector = new BufferedReader(new FileReader(listaNegra))){
			String linea;
			while((linea = lector.readLine()) != null) {
				linea.replaceAll("\\r?\\n", "");
				pat = Pattern.compile(linea, Pattern.CASE_INSENSITIVE);
				mat = pat.matcher(message);
				if(mat.find()) {
					borraEInforma(event);
					return;
				}				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void borraEInforma(MessageCreateEvent event) {
		event.getMessage().delete();
		logger.info("Mensaje con badWord detectado y eliminado, causante:" + event.getMessageAuthor().getName() + " contenido: " + event.getMessageContent());
		try {
			if(!event.getMessageAuthor().isYourself())
				event.getMessageAuthor().asUser().get().openPrivateChannel().get().sendMessage("Se te ha eliminado el mensaje porque contenia una palabra no permitida en el servidor");
		} catch (InterruptedException e) {
			logger.error("Error al intentar mandar mensaje privado al usuario" + event.getMessageAuthor().getDisplayName());
			e.printStackTrace();
		} catch (ExecutionException e) {
			logger.error("Error al intentar mandar mensaje privado al usuario" + event.getMessageAuthor().getDisplayName());
			e.printStackTrace();
		}
		
	}
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		if(!compruebaSiExisteArchivo(event))
			return;
		compruebaBadWords(badWordsTXT, event);
		
	}

}
