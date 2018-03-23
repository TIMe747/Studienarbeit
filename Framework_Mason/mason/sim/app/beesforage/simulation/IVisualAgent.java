/**
 * 
 */
package sim.app.beesforage.simulation;

import sim.app.beesforage.InterfaceObjectVisualization;

/**
 * This interface provides visual information about an agent. This interface
 * extends the {@link IMovingAgent} interface because a visual agent needs a
 * location and size (provided by {@link IMovingAgent}) to be visualized.
 * 
 * @author hoehne
 * 
 */
public interface IVisualAgent extends IMovingAgent {

	/**
	 * Return the object that will be used for visualizing the this agent. The
	 * type of the object is not known due to different simulation environments
	 * so an interface {@link InterfaceObjectVisualization} is returned.
	 * 
	 * @return The visualization object.
	 */
	InterfaceObjectVisualization getVisualizationObject();

	/**
	 * Set the object that will be used for visualizing the this agent. The type
	 * of the object is not known due to different simulation environments so an
	 * interface {@link InterfaceObjectVisualization} is used.
	 * 
	 * @param visual
	 */
	void setVisualizationObject(InterfaceObjectVisualization visual);
}
