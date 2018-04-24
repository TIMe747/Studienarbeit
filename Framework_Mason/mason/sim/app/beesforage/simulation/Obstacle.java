package sim.app.beesforage.simulation;

import java.awt.Color;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.engine.SimState;

public class Obstacle extends AbstractMovingAgent {

	private static final long serialVersionUID = 5382655169564660698L;
	
	public final static Color STD_COLOR = new Color(0xff, 0x80, 0x00);
	public Obstacle(ForagingBeeSimulation simulation, Point3d location,
			double size) {
		super(simulation, location, new Vector3d(), size, STD_COLOR);
	}

	public void step(SimState state) {}
}
