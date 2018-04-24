package sim.app.beesforage.simulation;

import java.awt.Color;
import java.util.Random;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.app.beesforage.IIterationAgent;
import sim.app.beesforage.InterfaceObjectVisualization;
import sim.app.beesforage.utils.Filter;
import sim.app.beesforage.utils.Geometric;
import sim.app.beesforage.utils.J3dPolar;

public abstract class AbstractMovingAgent implements IIterationAgent,
		IVisualAgent {

	private static final long serialVersionUID = 7815716363922534915L;

	static public Random r = new Random();

	InterfaceObjectVisualization visual;
	Object schedulerInformation;

	private ForagingBeeSimulation simulation;
	private Point3d location = new Point3d();
	private Vector3d velocity = new Vector3d();
	private J3dPolar heading = new J3dPolar();
	private double size;
	private double radius;

	public AbstractMovingAgent(ForagingBeeSimulation simulation,
			Point3d location, Vector3d velocity, double size,
			Color color) {

		setSize(size);
		this.simulation = simulation;
		this.setVisualizationObject(this.simulation.createVisual(this));
		this.getVisualizationObject().setColor(color);
		setLocation(location);
		setVelocityVector(velocity);
	}

	public final ForagingBeeSimulation getSimulation() {
		return simulation;
	}

	public double getSphereRadius() {
		return radius;
	}

	public double getSize() {
		return this.size;
	}

	public void setSize(double size) {
		this.size = size;
		this.radius = size / 2;
	}

	public final J3dPolar getHeading() {
		return heading;
	}

	public final void setHeading(J3dPolar heading) {
		this.heading = heading;
	}

	public Point3d getLocation() {
		return location;
	}

	public void setLocation(double x, double y, double z) {

		this.location.set(x, y, 0);
		updateLocation();
	}

	public void setLocation(Tuple3d location) {
		setLocation(location.x, location.y, location.z);
	}

	public final Vector3d getVelocityVector() {
		return velocity;
	}

	public final void setVelocity(double speed) {
		this.velocity.normalize();
		this.velocity.scale(speed);
		this.heading.set(velocity);
	}

	public final void setVelocityVector(double x, double y, double z) {
		velocity.set(x, y, z);
		heading.set(velocity);
	}

	public final void setVelocityVector(Vector3d v) {
		setVelocityVector(v.x, v.y, v.z);
	}

	static final J3dPolar directionTo(IMovingAgent agent1, IMovingAgent agent2) {
		return J3dPolar.createFrom(agent1.getLocation(), agent2.getLocation());
	}

	public final double distance(IMovingAgent agent) {
		return distance(agent.getLocation());
	}

	public final double distance(Point3d p) {
		return location.distance(p);
	}

	public final double distanceSquared(IMovingAgent agent) {
		return distanceSquared(agent.getLocation());
	}

	public final double distanceSquared(Point3d p) {
		return location.distanceSquared(p);
	}

	public final void forward(Vector3d direction) {
		location.add(direction);
		location.z = 0.0d;
		updateLocation();
	}

	public final void forward() {
		forward(velocity);
	}

	public IMovingAgent[] getObjectsWithinMyDistance(double radius,
			boolean useMySphere, boolean useTheirSpheres, boolean includeMySelf) {

		return getObjectsWithinMyDistance(radius, useMySphere, useTheirSpheres,
				Double.MIN_VALUE, includeMySelf, null);
	}

	public IMovingAgent[] getObjectsWithinMyDistance(double radius,
			boolean useMySphere, boolean useTheirSpheres, double maxSphere,
			boolean includeMySelf, Class<?> type) {
		double distanceCorrection = 0;
		double correction = 0;

		if (useMySphere) {
			distanceCorrection += this.getSphereRadius();
			correction = distanceCorrection;
		}

		if (useTheirSpheres)
			distanceCorrection += maxSphere;

		// get all objects which centers lies within a certain distance
		Object[] objects = simulation.getObjectsWithinDistance(this, radius
				+ distanceCorrection);

		if (type != null)
			objects = Filter.filter(objects, type);

		IMovingAgent agent;
		int i, k;
		for (i = 0, k = 0; i < objects.length; i++) {
			agent = (IMovingAgent) objects[i];

			// continue if the agent itself is included or the agent is not
			// myself
			if (includeMySelf || (agent != this)) {
				// get the distance from center to center
				double dist = this.distance(agent);
				// correct the distance according to the spheres
				dist -= correction;
				if (useTheirSpheres)
					dist -= agent.getSphereRadius();

				if (dist < radius) {
					objects[k] = agent;
					k++;
				}
			}
		}

		IMovingAgent[] neighbours = new IMovingAgent[k];
		System.arraycopy(objects, 0, neighbours, 0, k);

		return neighbours;
	}

	public final void headTo(IMovingAgent agent) {
		headTo(agent.getLocation());
	}

	public final void headTo(Tuple3d p) {
		J3dPolar direction = J3dPolar.createFrom(this.getLocation(), p);
		turnTo(direction.azimuth, direction.elevation);
	}

	public final boolean isInSphere(IMovingAgent agent) {
		return isInSphere(agent, false);
	}

	public final boolean isInSphere(IMovingAgent agent, boolean useSphere) {
		return (distanceToSphere(agent, true) <= this.getSphereRadius());
	}

	public final double distanceToSphere(IMovingAgent agent, boolean useSphere) {
		double d = distance(agent);
		if (useSphere)
			d += agent.getSphereRadius();
		return d;
	}

	public final void turnBy(double dAzimuth, double dElevation) {
		turnTo(heading.azimuth + dAzimuth, heading.elevation + dElevation);
	}

	public final void turnTo(double azimuth, double elevation) {
		elevation = 0.0d;
		heading.azimuth = Geometric.clampAngleRadians(azimuth);
		heading.elevation = Geometric.clampAngleRadians(elevation);
		velocity.set(heading.toCartesian());
	}

	final private void updateLocation() {
		simulation.updateLocation(this);
	}

	public final Vector3d vectorTo(IMovingAgent agent) {
		Vector3d v = new Vector3d();
		v.sub(agent.getLocation(), location);

		return v;
	}

	public InterfaceObjectVisualization getVisualizationObject() {
		return visual;
	}

	public void setVisualizationObject(InterfaceObjectVisualization visual) {
		this.visual = visual;
	}

	public Object getSchedulerInformation() {
		return schedulerInformation;
	}

	public void setSchedulerInformation(Object o) {
		schedulerInformation = o;
	}

	public final void setColor(Color color) {
		visual.setColor(color);
	}
}
