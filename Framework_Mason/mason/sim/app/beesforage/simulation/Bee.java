package sim.app.beesforage.simulation;

import java.awt.Color;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import sim.app.beesforage.ForagingBeeSimulation;
import sim.app.beesforage.utils.Filter;
import sim.app.beesforage.utils.Geometric;
import sim.app.beesforage.utils.J3dPolar;
import sim.engine.SimState;

public class Bee extends AbstractMovingAgent {

	private static final long serialVersionUID = -6693732612451071979L;

	public enum State {
		inHiveWithoutInfo, inHiveWithInfo, leaveHive, dancing, foraging, searching, returnWithInfo, returnWithInfoAndLoad, unloadQueue, scouting, returnWithoutInfo
	}

	static final int valueObstacle = (1 << 0);
	static final int valueBee = (1 << 1);
	static final int valueHive = (1 << 2);
	static final int valueEntrance = (1 << 3);
	static final int valueFoodSource = (1 << 4);

	Hive hive;
	FoodSource foodSource;

	J3dPolar sourceDirection = new J3dPolar();
	Point3d currentTargetLocation = new Point3d();

	private State state;
	boolean receptive;
	double dancingThreshold;
	int dancingTime;
	int searchSteps;
	int scoutSteps = 5000;

	public Bee(ForagingBeeSimulation simulation, Hive hive, Point3d location) {
		
		super(simulation, location, new Vector3d(), 2, Color.black);
		this.hive = hive;
		
		Vector3d v = new Vector3d(1 - r.nextDouble() * 2,
				1 - r.nextDouble() * 2, 0);
		if (v.length() != 0)
			v.scale(1 / v.length());
		else
			v.set(1.0d, 1.0d, 0);

		setVelocityVector(v);
		setColor(Color.yellow);
		setState(State.inHiveWithoutInfo);
	}

	final public void step(SimState state) {
		iterate();
	}

	public void iterate() {
		State state = getState();

		switch (state) {
		case inHiveWithoutInfo:
			doStateInHiveWithoutInfo();
			break;
		case inHiveWithInfo:
			doStateInHiveWithInfo();
			break;
		case leaveHive:
			doStateLeaveHive();
			break;
		case dancing:
			doStateDancing();
			break;
		case foraging:
			doStateForaging();
			break;
		case searching:
			doStateSearching();
			break;
		case returnWithInfo:
			System.err.println("State " + state + " is not implemented.");
			System.exit(5);
			break;
		case returnWithInfoAndLoad:
			doStateReturnWithInfoAndLoad();
			break;
		case unloadQueue:
			doStateUnloadQueue();
			break;
		case scouting:
			doStateScouting();
			break;
		case returnWithoutInfo:
			doStateReturnWithoutInfo();
			break;
		default:
			System.err.println("iterate(): State " + state
					+ " is not implemented.");
			System.exit(5);
			break;
		}
	}

	protected void doStateInHiveWithoutInfo() {
		
		setColor(Color.yellow);
		receptive = true;
		double pStartScouting = getSimulation().pStartScouting;

		if ((pStartScouting) >= r.nextDouble()) {
			forgetSource(null, State.leaveHive);
			receptive = false;
		}
		goInHive();
	}

	protected void doStateInHiveWithInfo() {
		
		double pForgettingSource = getSimulation().pForgettingSource;
		double pForagingAgain = getSimulation().pForagingAgain;

		setColor(Color.yellow);
		receptive = true;

		double threshold_forgetting = pForgettingSource;
		if (threshold_forgetting > r.nextDouble()) {
			forgetSource(Color.yellow, State.inHiveWithoutInfo);
			goInHive();
			return;
		}

		if ((dancingThreshold > -1)
				&& (dancingThreshold) > (r.nextDouble() * 10000)) {
			setColor(getFoodSource().getVisualizationObject().getColor());
			setState(State.dancing);

			receptive = false;
			dancingTime = (int) Math.round(10 + dancingThreshold * .33);
			dancingThreshold *= .75;

			if (dancingThreshold < 1) {
				dancingThreshold = -1;
				receptive = true;
			}
			goInHive();
			return;
		}

		if ((pForagingAgain) > r.nextDouble()) {
			setState(State.leaveHive);
			goInHive();
			return;
		}

		goInHive();
	}

	protected void doStateLeaveHive() {
		
		HiveEntrance entrance = hive.getEntrance();

		if (entrance.isInSphere(this)) {
			forward();
			return;
		}

		if (hive.isInSphere(this)) {
			headTo(hive.getEntrance());
			forward();
			return;
		}

		if (getFoodSource() != null) {
			setColor(getFoodSource().getVisualizationObject().getColor());
			setState(State.foraging);
		}

		if (getFoodSource() == null) {
			setColor(Color.black);
			setState(State.scouting);
		}
	}

	protected void doStateDancing() {
		dancingTime--;
		if (dancingTime <= 0) {
			setState(State.inHiveWithInfo);
		}
		goInHive();
	}

	protected void doStateForaging() {
		doStepOutgoing(getTargetLocation());
		sourceDirection.radius--;

		if (sourceDirection.radius <= 0) {
			int maxSearchSteps = getSimulation().maxSearchSteps;
			searchSteps = maxSearchSteps - (r.nextInt(maxSearchSteps) / 10);
			setState(State.searching);
		}
	}

	protected void doStateSearching() {
		searchSteps--;
		if (searchSteps <= 0) {
			forgetSource(Color.red, State.returnWithoutInfo);
		}
		turnBy(Math.toRadians(searchSteps - 2 * r.nextDouble() * searchSteps),
				Math.toRadians(searchSteps - 2 * r.nextDouble() * searchSteps));
		doStepOutgoing(null);
		foundSource();
	}

	protected void doStateReturnWithInfoAndLoad() {
		doCommonReturnToHive();
	}

	protected void doStateUnloadQueue() {

		double individuality = .8 + ((r.nextDouble() * 20) / 100);
		dancingThreshold = (individuality) + (20);
		dancingThreshold = Math.max(0, dancingThreshold);
		
		headTo(hive);
		receptive = true;

		if (getFoodSource() != null) {
			setColor(getFoodSource().getVisualizationObject().getColor());
			setState(State.inHiveWithInfo);
		}

		if (getFoodSource() == null) {
			setState(State.inHiveWithoutInfo);
		}
	}

	protected void doStateScouting() {
		if (r.nextInt(5) == 0) {
			turnBy(Math.toRadians(r.nextInt(40) - 20), Math.toRadians(r
					.nextInt(40) - 20));
		}
		doStepOutgoing(null);
		foundSource();
		scoutSteps--;
		if (scoutSteps <= 0) {
			forgetSource(Color.red, State.returnWithoutInfo);
		}
	}

	protected void doStateReturnWithoutInfo() {
		doCommonReturnToHive();
	}

	private void doCommonReturnToHive() {
		AbstractMovingAgent entrance = hive.getEntrance();
		doStepReturning(entrance.getLocation());
		if (entrance.isInSphere(this)) {
			setState(State.unloadQueue);
		}
	}

	private void goInHive() {
		doStepWalking();
		// if this bee may listen to a dancing bee
		if ((receptive))
			listenToDancingBee();
	}

	private void doStepReturning(Tuple3d targetLocation) {
		doStepFlying(targetLocation, true, false);
		sourceDirection.radius += getVelocityVector().length();
	}

	private void doStepOutgoing(Tuple3d targetLocation) {
		doStepFlying(targetLocation, true, true);
	}

	private void doStepFlying(Tuple3d targetLocation, boolean checkBoundaries,
			boolean checkHive) {


		if (targetLocation != null) {
			headTo(targetLocation);
		}

		boolean hiveCollision = checkHive & hive.isInSphere(this);
		if (hiveCollision) {
			turnBy(Math.toRadians(180 + (r.nextDouble() * 10) - 5), Math
					.toRadians(180 + (r.nextDouble() * 10) - 5));
			forward();
			return;
		} else {
			if (checkBoundaries) {
				boolean boundCollisionXY = getSimulation().isOutsideXY(this);
				boolean boundCollisionZ = false
						& getSimulation().isOutsideZ(this);

				double turnAzimuth = 0.0d;
				double turnElevation = 0.0d;

				if (boundCollisionXY)
					turnAzimuth = Math
							.toRadians(180 + (r.nextDouble() * 10) - 5);

				if (boundCollisionZ)
					turnElevation = Math
							.toRadians(180 + (r.nextDouble() * 10) - 5);

				if (boundCollisionXY | boundCollisionZ) {
					turnBy(turnAzimuth, turnElevation);
					forward();
					return;
				}
			}
		}
		if (getSimulation().avoidObstacles) {
			Vector3d av = computeAvoidance(targetLocation);
			av.scale(5);
			av.add(getVelocityVector());
			av.normalize();
			forward(av);
		} else {
			forward();
		}
	}

	private Vector3d computeAvoidance(Tuple3d targetLocation) {
		double sphereRadius = getSphereRadius();
		double observationRadius = sphereRadius * 5;
		double avoidRadius = sphereRadius * 5;

		boolean useMyRadius = true;
		boolean useTheirRadius = true;

		double targetDistance = Double.MAX_VALUE;
		if (targetLocation != null) {
			targetDistance = Geometric.distance(this.getLocation(),
					targetLocation);
			if (useMyRadius)
				targetDistance -= sphereRadius;
		}
		
		IMovingAgent[] agents = this.getObjectsWithinMyDistance(
				observationRadius, useMyRadius, useTheirRadius, Double.MIN_VALUE, false, null);

		Vector3d avoidVector = new Vector3d();
		int i = 0;
		for (i = 0; i < agents.length; i++) {
			IMovingAgent agent = agents[i];

			if (checkToAvoid(agent)) {
				J3dPolar sphericVector = J3dPolar.createFrom(
						this.getLocation(), agent.getLocation());

				double azimuth = sphericVector.azimuth
						- this.getHeading().azimuth;
				double elevation = sphericVector.elevation
						- this.getHeading().elevation;

				if (!((azimuth < Geometric.MINUS_PI_HALF)
						| (azimuth > Geometric.PI_HALF)
						| (elevation < Geometric.MINUS_PI_HALF) | elevation > Geometric.PI_HALF)) {
					double distance = sphericVector.radius;
					if (useTheirRadius)
						distance -= agent.getSphereRadius();

					if (useMyRadius)
						distance -= sphereRadius;

					if (distance <= avoidRadius) {
						if (distance < targetDistance) {

							if (sphericVector.radius != 0.0) {
								double s = 1 / sphericVector.radius;
								if (distance == 0.0)
									distance = .00000001;
								s /= (distance * distance);
								s *= (agent.getSphereRadius() * agent
										.getSphereRadius());
								sphericVector.radius *= s;
							}
							sphericVector.azimuth += (azimuth > 0.0) ? Geometric.MINUS_PI_HALF
									: Geometric.PI_HALF;
							
							Vector3d v1 = new Vector3d(sphericVector
									.toCartesian());

							avoidVector.add(v1);
						}
					}
				}
			}
		}
		
		J3dPolar v = J3dPolar.createFrom(avoidVector);
		double angleNoise;
		angleNoise = Math.toRadians(10 * r.nextGaussian());
		v.azimuth = Geometric.clampAngleRadians(v.azimuth + angleNoise);
		avoidVector = new Vector3d(v.toCartesian());
		Geometric.normalize(avoidVector);

		return avoidVector;
	}

	private boolean checkToAvoid(IMovingAgent agent) {
		State state = getState();
		int mask = 0;
		int value = 0;

		if (agent instanceof Obstacle)
			value |= valueObstacle;
		if (agent instanceof Bee)
			value |= valueBee;
		if (agent instanceof Hive)
			value |= valueHive;
		if (agent instanceof HiveEntrance)
			value |= valueEntrance;
		if (agent instanceof FoodSource)
			value |= valueFoodSource;

		switch (state) {
		case foraging:
		case searching:
			mask = valueObstacle | valueHive | valueEntrance;
			break;
		case returnWithInfo:
		case returnWithInfoAndLoad:
			mask = valueObstacle | valueHive;
			break;
		case scouting:
			mask = valueObstacle | valueBee | valueHive | valueEntrance;
			break;
		case returnWithoutInfo:
			mask = valueObstacle | valueHive;
			break;
		case leaveHive:
		case inHiveWithoutInfo:
		case inHiveWithInfo:
		case dancing:
		case unloadQueue:
			System.err.println("checkToAvoid(): State " + state
					+ " is not expected for testing to avoid.");
			return true;

		default:
			System.err.println("checkToAvoid(): State " + state
					+ " is not implemented.");
			System.exit(-7);
			break;
		}
		return (value & mask) != 0;
	}

	private void doStepWalking() {
		if ((hive.distanceToSphere(this, true) + this.getSphereRadius()) >= hive
				.getSphereRadius()) {
			headTo(hive);
			forward();
		} else {
			turnBy(Math.toRadians(45 - r.nextDouble() * 90), Math
					.toRadians(45 - r.nextDouble() * 90));
			forward();
		}
	}

	private FoodSource foundSource() {

		FoodSource fs = nearFoodSource();

		if (fs != null) {
			setFoodSource(fs);
			sourceDirection = directionTo(hive.getEntrance(), fs);
			sourceDirection.radius = 0.0;
			setColor(fs.getVisualizationObject().getColor());
			setState(State.returnWithInfoAndLoad);
			ForagingBeeSimulation.FOODUNITS = ForagingBeeSimulation.FOODUNITS + 1;
		}

		return fs;
	}

	private FoodSource nearFoodSource() {
		
		Object[] objects = this.getObjectsWithinMyDistance(.5, true, true,
				false);

		objects = Filter.filter(objects, FoodSource.class);
		FoodSource fs = null;
		if (objects.length > 0) {
			fs = (FoodSource) objects[r.nextInt(objects.length)];
		}

		return fs;
	}

	private void forgetSource(Color newColor, State nextState) {
		dancingThreshold = -1;
		setFoodSource(null);


		if (newColor != null)
			setColor(newColor);
		setState(nextState);
	}

	private void copySourceInformationFrom(Bee src) {
		copySourceInformation(this, src);
	}

	private void copySourceInformation(Bee dest, Bee src) {
		dest.sourceDirection = new J3dPolar(src.sourceDirection);
		dest.setColor(src.getVisualizationObject().getColor());
		dest.setFoodSource(src.foodSource);
	}

	private void listenToDancingBee() {
		IMovingAgent[] agents = this.getObjectsWithinMyDistance(1.0, true,
				true, this.getSphereRadius(), false, null);
		agents = (IMovingAgent[]) Filter.filter(agents, Bee.class);

		if (agents.length > 0) {
			int index = r.nextInt(agents.length);
			Bee b = (Bee) agents[index];
			if (b.getState() == Bee.State.dancing) {
				double comNoise = getSimulation().comNoise;
				copySourceInformationFrom(b);
				sourceDirection.radius += Math.round(r.nextGaussian()
						* comNoise * sourceDirection.radius);
				double angle;
				angle = sourceDirection.azimuth;
				angle += (r.nextGaussian() * comNoise) * Math.PI;
				Geometric.clampAngleRadians(angle);
				sourceDirection.azimuth = angle;

				angle = sourceDirection.elevation;
				angle += (r.nextGaussian() * comNoise) * Math.PI;
				Geometric.clampAngleRadians(angle);
				sourceDirection.elevation = angle;

				setTargetLocation(sourceDirection);

				receptive = false;
				
				setState(State.leaveHive);
			}
		}
	}

	public final FoodSource getFoodSource() {
		return foodSource;
	}

	public final void setFoodSource(FoodSource foodSource) {

		if (this.foodSource != null)
			this.foodSource.beeUnlock();

		this.foodSource = foodSource;

		if (this.foodSource != null)
			this.foodSource.beeLock();
	}

	private State getState() {
		return this.state;
	}

	private void setState(State state) {
		this.state = state;
	}

	private Point3d getTargetLocation() {
		return currentTargetLocation;
	}

	private void setTargetLocation(J3dPolar direction) {
		Point3d p = direction.toCartesian();
		p.add(hive.getEntrance().getLocation());

		currentTargetLocation.set(p);
	}
}
