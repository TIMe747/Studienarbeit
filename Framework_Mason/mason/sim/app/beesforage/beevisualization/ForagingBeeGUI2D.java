/**
 * 
 */
package sim.app.beesforage.beevisualization;

import java.awt.Color;
import sim.app.beesforage.ForagingBeeGUI;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.display.Controller;
import sim.display.Display2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;


/**
 * Creation of this class for a 2D visual display of the simulation.
 * <p>
 * Copyright 2009 Joerg Hoehne
 * 
 * @author hoehne (<a href="mailto:hoehne@thinktel.de">J&ouml;rg H&ouml;hne</a>)
 * 
 */
public class ForagingBeeGUI2D extends ForagingBeeGUI {
	/**
	 * The display that is embedded in a window. The displays shows the visuals
	 * of the simulation.
	 */
	public Display2D display;

	/**
	 * Where to display the agents.
	 */
	ContinuousPortrayal2D vidPortrayal = new ContinuousPortrayal2D();


	public ForagingBeeGUI2D() {
		super(new SimulationVisualization(System.currentTimeMillis()));
	}

	/**
	 * Setting up the visuals.
	 */
	public void init(Controller c) {
		super.init(c);

		// make the displayer, using the maximum values of the simulation (if
		// not, some stretching may occur)
		display = new Display2D(ForagingBeeSimulation.WIDTH,
				ForagingBeeSimulation.HEIGHT, this);

		displayFrame = display.createFrame();
		displayFrame.setTitle("Honey bee playground");
		c.registerFrame(displayFrame); // register the frame so it appears in
		// the "Display" list
		displayFrame.setVisible(true);
		display.attach(vidPortrayal, "Agents");
	}

	/**
	 * Set up the displays.
	 */
	public void setupPortrayals() {
		SimulationVisualization beeSimulation = (SimulationVisualization) state;
		// tell the portrayals what to portray and how to portray them
		vidPortrayal.setField(beeSimulation.environment);
		// reschedule the displayer
		display.reset();
		display.setBackdrop(Color.black);

		// redraw the display
		display.repaint();
	}

	/**
	 * Tidying up when simulation is to be quit.
	 */
	public void quit() {
		super.quit();
		display = null;
	}
}