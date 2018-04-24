
package sim.app.beesforage.simulation;

import sim.app.beesforage.InterfaceObjectVisualization;

public interface IVisualAgent extends IMovingAgent {

	InterfaceObjectVisualization getVisualizationObject();
	void setVisualizationObject(InterfaceObjectVisualization visual);
}
