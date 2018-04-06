package sim.app.beesforage;

import javax.swing.JFrame;
import sim.display.GUIState;
import sim.engine.SimState;

public abstract class ForagingBeeGUI extends GUIState {
	
	/*Display der Simulation*/
	public JFrame displayFrame;
	
	/*Hält alle Informationen der Simulation*/
	public ForagingBeeGUI(ForagingBeeSimulation sim) {
		super(sim);
		sim.prepareSimulation();
	}
	
	/*Visualisierung der Simulation*/
	public ForagingBeeGUI(SimState state) {
		super(state);
	}
	
	/*Erlaubt das Inspizieren von Objekten*/
	public Object getSimulationInspectedObject() {
		return state;
	}

	/*Titel der Simulation*/
	public static String getName() {
		return "BCO";
	}
	
	/*Konfiguration des Displays der Simulation*/
	public abstract void setupPortrayals();

	public void start() {
		super.start();
		setupPortrayals();
	}

	/*Wird von der Konsole aufgerufen um die Simulation in einen neuen Zustand zu versetzen*/
	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}
	
	/*Aufräumen*/
	public void quit() {
		super.quit();

		if (displayFrame != null)
			displayFrame.dispose();
		displayFrame = null;
	}
}
