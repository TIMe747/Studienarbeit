package sim.app.beesforage.utils;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class Geometric {

	public static final double MINUS_PI = -Math.PI;
	public static final double PI_HALF = Math.PI / 2;
	public static final double MINUS_PI_HALF = -PI_HALF;
	public static final double PI2 = Math.PI * 2;
	public static final double MINUS_PI2 = -PI2;
	
	public static double azimuth(Tuple3d a) {
		return Math.atan2(a.y, a.x);
	}

	public static final double azimuth(Tuple3d a, Tuple3d b) {
		double dx = b.x - a.x;
		double dy = b.y - a.y;
		return Math.atan2(dy, dx);
	}

	public static J3dPolar toPolar(double x, double y, double z) {
		return J3dPolar.createFrom(x, y, z);
	}

	public static final double clampAngleDegree(double angle) {
		if (angle >= 360.0)
			angle %= 360.0d;
		if (angle < 0.0) {
			angle %= 360.0d;
			angle += 360;
		}
		return angle;
	}

	public static final double clampAngleRadians(double angle) {
		if (angle >= Math.PI) {
			angle %= PI2;
			if (angle > Math.PI)
				angle -= PI2;
		} else {
			if (angle < 0.0) {
				angle %= PI2;
				if (angle < MINUS_PI)
					angle += PI2;
			}
		}
		return angle;
	}

	public static final double distance(Tuple3d p1, Tuple3d p2) {
		double dx = p1.x - p2.x;
		double dy = p1.y - p2.y;
		double dz = p1.z - p2.z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public static final void normalize(Vector3d v) {
		if ((v.x == 0.0) && (v.y == 0.0) && (v.z == 0.0))
			return;

		normalize(v, v.length());
	}

	public static final void normalize(Vector3d v, double length) {
		if ((v.x == 0.0) && (v.y == 0.0) && (v.z == 0.0))
			return;

		v.scale(1 / length);
	}
}
