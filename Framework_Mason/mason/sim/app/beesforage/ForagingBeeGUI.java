package sim.app.beesforage;

import javax.swing.JFrame;

import sim.app.beesforage.simulation.FoodSource;
import sim.app.beesforage.simulation.Hive;
import sim.display.GUIState;
import sim.engine.SimState;

public abstract class ForagingBeeGUI extends GUIState {

	FoodSource foodSources[];
	Hive hives[];

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

	/**
	 * The name of the simulation.
	 * 
	 * @return The string containing the name of the simulation.
	 */
	public static String getName() {
		return "Foraging Bee Simulation in 2D";
	}

	public abstract void setupPortrayals();

	public void start() {
		super.start();
		setupPortrayals();

		ForagingBeeSimulation bs = (ForagingBeeSimulation) state;
		foodSources = new FoodSource[bs.foodSources.size()];
		hives = new Hive[bs.hives.size()];
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
