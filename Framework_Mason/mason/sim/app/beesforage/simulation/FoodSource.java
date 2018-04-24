package sim.app.beesforage.simulation;

import java.awt.Color;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.engine.SimState;

public class FoodSource extends AbstractMovingAgent {

	private static final long serialVersionUID = -2605739555833872334L;

	int beeCount;


	public FoodSource(ForagingBeeSimulation simulation, Point3d location, double size, Color color) {
		super(simulation, location, new Vector3d(), size, color);
	}

	
	public void beeLock() {
		beeCount++;
	}

	public void beeUnlock() {
		beeCount--;
	}


	public void step(SimState state) {}
}

