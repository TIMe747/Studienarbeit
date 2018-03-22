/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package sim.app.tutorial4;

import java.util.Random;
import javax.swing.JOptionPane;
import sim.display.Console;
import sim.display.GUIState;
import de.thinktel.foragingBee.masonGlue.dimension2.ForagingBeeGUI2D;


public class Tutorial4WithUI {
	/**
	 * No arguments are evaluated by this application.
	 * 
	 * @param args
	 *            No argument will be evaluated.
	 */
	public static void main(String[] args) {
		Console c;

		// Custom button text
		Object[] options = { "Surprise me!", "3D mode", "2D mode", "Abort" };
		int n = JOptionPane.showOptionDialog(null,
				"Please choose the simulation mode.",
				"Simulation mode selection.", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		GUIState gui = null;
		gui = new ForagingBeeGUI2D();

		if (gui != null) {
			c = new Console(gui);
			c.setVisible(true);
		}
	}
}
    
    
    
