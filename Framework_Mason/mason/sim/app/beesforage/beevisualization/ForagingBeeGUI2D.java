package sim.app.beesforage.beevisualization;

import java.awt.Color;
import sim.app.beesforage.ForagingBeeGUI;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.display.Controller;
import sim.display.Display2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;

public class ForagingBeeGUI2D extends ForagingBeeGUI {

	public Display2D display;
	ContinuousPortrayal2D vidPortrayal = new ContinuousPortrayal2D();

	public ForagingBeeGUI2D() {
		super(new SimulationVisualization(System.currentTimeMillis()));
	}

	public void init(Controller c) {
		
		super.init(c);
		display = new Display2D(ForagingBeeSimulation.WIDTH,
				ForagingBeeSimulation.HEIGHT, this);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Futtersuche Bienen");
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(vidPortrayal, "Agents");
	}

	public void setupPortrayals() {
		
		SimulationVisualization beeSimulation = (SimulationVisualization) state;
		vidPortrayal.setField(beeSimulation.environment);
		display.reset();
		display.setBackdrop(Color.white);
		display.repaint();
	}


	public void quit() {
		super.quit();
		display = null;
	}
}
