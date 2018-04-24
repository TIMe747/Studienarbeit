package sim.app.beesforage.beevisualization;

import javax.vecmath.Point3d;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.app.beesforage.InterfaceObjectVisualization;
import sim.app.beesforage.simulation.Bee;
import sim.app.beesforage.simulation.IMovingAgent;
import sim.app.beesforage.simulation.IVisualAgent;
import sim.app.beesforage.simulation.Obstacle;
import sim.field.continuous.Continuous2D;
import sim.util.Bag;
import sim.util.Double2D;

public class SimulationVisualization extends ForagingBeeSimulation {

	private static final long serialVersionUID = -1024692455552577435L;

	public Continuous2D environment = new Continuous2D(100, WIDTH, HEIGHT);

	public SimulationVisualization(long seed) {
		super(seed);
	}

	public void updateLocation(IVisualAgent agent) {
		Point3d location = agent.getLocation();
		InterfaceObjectVisualization visual = (InterfaceObjectVisualization) agent
				.getVisualizationObject();

		environment.setObjectLocation(visual, new Double2D(location.x,
				location.y));
	}

	public InterfaceObjectVisualization createVisual(IVisualAgent agent) {
		if (agent instanceof Bee) {
			return new ObjectVisualizationBee(agent);
		}
		if(agent instanceof Obstacle) {
			return new ObjectVisualizationObstacle(agent);
		}
		
		return new ObjectVisualizationHiveFood(agent);
	}

	public Object removeFromVisualization(IVisualAgent agent) {
		return environment.remove(agent.getVisualizationObject());
	}

	public Object[] getObjectsWithinDistance(IMovingAgent agent, double distance) {
		Point3d location = agent.getLocation();
		Bag b = environment.getObjectsWithinDistance(new Double2D(location.x,
				location.y), distance, false, false);

		Object[] agents = new Object[b.numObjs];
		int i;
		for (i = 0; i < agents.length; i++)
			agents[i] = ((InterfaceObjectVisualization) b.objs[i]).getAgent();
		return agents;
	}
}
