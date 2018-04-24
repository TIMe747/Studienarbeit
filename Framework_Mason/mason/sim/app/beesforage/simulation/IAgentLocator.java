package sim.app.beesforage.simulation;

import sim.app.beesforage.InterfaceObjectVisualization;

public interface IAgentLocator {

	void updateLocation(IVisualAgent agent);
	InterfaceObjectVisualization createVisual(IVisualAgent agent);
	Object removeFromVisualization(IVisualAgent agent);
}
