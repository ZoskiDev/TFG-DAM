package graphicalUserInterface;

import botlogic.O5O;

/**
 * @author Zyssk0
 * Clase encargada de representar la logica de la interfaz en el modelo vista controlador del proyecto
 * 
 * */
public class GraphicalUserInterfaceLogic {

	O5O bot = new O5O();
	
	public GraphicalUserInterfaceLogic() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Metodo para comprobar si el bot ha inicializado correctamente
	 * @return true si esta inicializado
	 * @return false si no se ha inicializado correctamente
	 * */
	public boolean isBotReady() {
		if(bot.getApi() != null)
			return true;
		return false;
	}
}
