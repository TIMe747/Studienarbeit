package sim.app.beesforage.beevisualization;

import java.awt.Color;
import sim.app.beesforage.InterfaceObjectVisualization;
import sim.app.beesforage.simulation.IVisualAgent;
import sim.portrayal.SimplePortrayal2D;

public abstract class ObjectVisualization extends SimplePortrayal2D implements
		InterfaceObjectVisualization {

	private static final long serialVersionUID = 1901646087125046773L;
	IVisualAgent agent;
	Color color;

	public ObjectVisualization(IVisualAgent agent) {
		this.agent = agent;
	}

	public IVisualAgent getAgent() {
		return agent;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
