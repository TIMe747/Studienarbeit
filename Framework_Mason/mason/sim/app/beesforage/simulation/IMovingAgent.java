package sim.app.beesforage.simulation;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public interface IMovingAgent {

	double distance(IMovingAgent agent);

	double distance(Point3d p);

	double distanceSquared(IMovingAgent agent);

	double distanceSquared(Point3d p);

	Vector3d vectorTo(IMovingAgent agent);

	public Point3d getLocation();

	public void setLocation(double x, double y, double z);

	public void setLocation(Tuple3d location);

	public double getSphereRadius();

	public Vector3d getVelocityVector();

	public void setVelocity(double speed);

	public void setVelocityVector(double x, double y, double z);

	public void setVelocityVector(Vector3d v);
}