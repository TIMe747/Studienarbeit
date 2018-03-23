package sim.app.beesforage;

import java.awt.Color;

import sim.app.beesforage.simulation.IVisualAgent;

public interface InterfaceObjectVisualization {

	IVisualAgent getAgent();
	
	public Color getColor();
	void setColor(Color color);
}
