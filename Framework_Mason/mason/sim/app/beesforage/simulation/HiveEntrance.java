package sim.app.beesforage.simulation;

import java.awt.Color;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.engine.SimState;

public class HiveEntrance extends AbstractMovingAgent {

	private static final long serialVersionUID = -2120598277487796447L;

	public static final Color STD_COLOR = new Color(0x00, 0xa0, 0xa0, 0xa0);

	Hive hive;

	public HiveEntrance(ForagingBeeSimulation simulation,
			Hive hive, double direction) {
		super(simulation, new Point3d(), new Vector3d(), 10,
				STD_COLOR);
		this.hive = hive;

		setSize(hive.getSize() / 5);
		double r = hive.getSize() / 2;
		double x = Math.cos(direction) * r + hive.getLocation().x;
		double y = Math.sin(direction) * r + hive.getLocation().y;
		double z = hive.getLocation().z;
		this.setLocation(x, y, z);
	}

	public void step(SimState state) {
	}
}
