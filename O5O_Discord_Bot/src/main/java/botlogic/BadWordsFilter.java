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

public class BadWordsFilter implements MessageCreateListener{

	private File badWordsTXT;
	private static Logger logger = LogManager.getLogger(BadWordsFilter.class);

	public BadWordsFilter() {
		super();
	}
	private boolean checkIfFilterExists(MessageCreateEvent event) {
		
		event.getServer().ifPresent(x  ->
		badWordsTXT = new File("badWords/" + event.getServer().get().getName() + "badWords.txt"));
		if (!badWordsTXT.exists())
			return false;
		return true;
	}
	private void checkBadWords(File blackList, MessageCreateEvent event) {
		Pattern pat;
		Matcher mat;
		String message = event.getMessageContent();
		
		try(BufferedReader reader = new BufferedReader(new FileReader(blackList))){
			String linea;
			while((linea = reader.readLine()) != null) {
				linea.replaceAll("\\r?\\n", "");
				pat = Pattern.compile(linea, Pattern.CASE_INSENSITIVE);
				mat = pat.matcher(message);
				if(mat.find()) {
					deleteAndInformUser(event);
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
	private void deleteAndInformUser(MessageCreateEvent event) {
		event.getMessage().delete();
		logger.info("Mensaje con badWord detectado y eliminado, causante:" + event.getMessageAuthor().getDisplayName() + " contenido: " + event.getMessageContent());
		try {
			event.getMessageAuthor().asUser().get().openPrivateChannel().get().sendMessage("Se te ha eliminado el mensaje pues contenia una palabra no permitida en el servidor");
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
		if(!checkIfFilterExists(event))
			return;
		checkBadWords(badWordsTXT, event);
		
	}

}
