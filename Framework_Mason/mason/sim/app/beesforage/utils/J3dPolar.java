
package sim.app.beesforage.utils;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;

public class J3dPolar {
	
	public double radius;
	public double azimuth;
	public double elevation;

	public J3dPolar() {
		this(0, 0, 0);
	}

	public J3dPolar(J3dPolar src) {
		this(src.radius, src.azimuth, src.elevation);
	}

	public J3dPolar(double radius, double azimuth, double elevation) {
		this.radius = radius;
		this.azimuth = azimuth;
		this.elevation = elevation;
	}

	public final double getRadius() {
		return radius;
	}

	public final void setRadius(double radius) {
		this.radius = radius;
	}

	public final double getAzimuth() {
		return azimuth;
	}

	public final void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}

	public final double getElevation() {
		return elevation;
	}

	public final void setElevation(double elevation) {
		this.elevation = elevation;
	}

	public final void set(Tuple3d t) {
		set(t.x, t.y, t.z);
	}

	public void set(double x, double y, double z) {
		azimuth = 0;
		elevation = 0;
		radius = Math.sqrt(x * x + y * y + z * z);
		if (radius > 0) {
			azimuth = Math.atan2(y, x);
			elevation = Math.asin(z / radius);
		}
	}

	public static J3dPolar createFrom(Tuple3d p) {
		return createFrom(p.x, p.y, p.z);
	}

	public static J3dPolar createFrom(Tuple3d p1, Tuple3d p2) {
		return createFrom(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
	}

	public static J3dPolar createFrom(double x, double y, double z) {
		J3dPolar p = new J3dPolar();
		p.set(x, y, z);
		return p;
	}

	public Point3d toCartesian() {

		double cosElevation = Math.cos(elevation);
		double x = radius * Math.cos(azimuth) * cosElevation;
		double y = radius * Math.sin(azimuth) * cosElevation;
		double z = radius * Math.sin(elevation);

		return new Point3d(x, y, z);
	}
}
