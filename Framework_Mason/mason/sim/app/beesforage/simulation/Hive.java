package sim.app.beesforage.simulation;

import java.awt.Color;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.engine.SimState;

public class Hive extends AbstractMovingAgent {

	private static final long serialVersionUID = -4592228162041859561L;

	public static final Color STD_COLOR = new Color(0x00, 0x00, 0xa0, 0xa0);

	private HiveEntrance entrance;

	public Hive(ForagingBeeSimulation simulation, Point3d location, double size) {
		super(simulation, location, new Vector3d(), size, STD_COLOR);
	}

	public void setEntrance(HiveEntrance entrance) {
		this.entrance = entrance;
	}

	public HiveEntrance getEntrance() {
		return this.entrance;
	}

	public void step(SimState state) {
	}
}
