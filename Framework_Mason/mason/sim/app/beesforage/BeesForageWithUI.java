package sim.app.beesforage;

import sim.app.beesforage.beevisualization.ForagingBeeGUI2D;
import sim.display.Console;
import sim.display.GUIState;

public class BeesForageWithUI {

	public static void main(String[] args) {
		
		Console c;
		GUIState gui = null;
		gui = new ForagingBeeGUI2D();

		if (gui != null) {
			c = new Console(gui);
			c.setVisible(true);
		}
	}
}
