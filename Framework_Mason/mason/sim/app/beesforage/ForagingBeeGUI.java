package sim.app.beesforage;

import javax.swing.JFrame;
import sim.display.GUIState;
import sim.engine.SimState;

public abstract class ForagingBeeGUI extends GUIState {

	public JFrame displayFrame;
	
	public ForagingBeeGUI(ForagingBeeSimulation sim) {
		super(sim);
		sim.prepareSimulation();
	}
	
	public ForagingBeeGUI(SimState state) {
		super(state);
	}

	public Object getSimulationInspectedObject() {
		return state;
	}

	public static String getName() {
		return "Futtersuche Bienen";
	}

	public abstract void setupPortrayals();

	public void start() {
		super.start();
		setupPortrayals();
	}

	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}

	public void quit() {
		super.quit();

		if (displayFrame != null)
			displayFrame.dispose();
		displayFrame = null;
	}
}
