package sim.app.beesforage;

import java.awt.Color;
import java.util.ListIterator;
import java.util.Vector;
import javax.vecmath.Point3d;

import sim.app.beesforage.simulation.Bee;
import sim.app.beesforage.simulation.FoodSource;
import sim.app.beesforage.simulation.Hive;
import sim.app.beesforage.simulation.HiveEntrance;
import sim.app.beesforage.simulation.IAgentLocator;
import sim.app.beesforage.simulation.IMovingAgent;
import sim.app.beesforage.simulation.Obstacle;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Interval;

public abstract class ForagingBeeSimulation extends SimState implements
		Steppable, IAgentLocator {

	private static final long serialVersionUID = -2187248402977949189L;
	
    public static int FOODUNITS = 0;
    public static int STEPS = -1;
    
	static public final int X_MIN = 0;
	static public final int X_MAX = 400;
	static public final int Y_MIN = 0;
	static public final int Y_MAX = 400;
	
	/*Display ist nicht 3D Z-Koordinate wird für Point3D benötigt*/
	static public final int Z_MIN = 0;
	static public final int Z_MAX = 400;
	static public final int WIDTH = X_MAX - X_MIN;
	static public final int MIDDLE_WIDTH = X_MIN + (WIDTH / 2);
	static public final int HEIGHT = Y_MAX - Y_MIN;
	static public final int MIDDLE_HEIGHT = Y_MIN + (HEIGHT / 2);
	static public final int LENGTH = Z_MAX - Z_MIN;
	static public final int MIDDLE_LENGTH = Z_MIN + (LENGTH / 2);
	
	static public final double HIVE_DIAMETER = 40;
	public boolean avoidObstacles = true;
	
	public int maxBees = 800;
	public int numObstacles = 1;
	public double comNoise = 0.01;
	public double pForgettingSource = 30E-6;
	public double pStartScouting = 39E-6;
	public double pForagingAgain = 500E-6;
	public int maxSearchSteps = 100;
	double maxAgentSphereRadius = Double.MIN_VALUE;
	
	Vector<IMovingAgent> agents = new Vector<IMovingAgent>();
	Vector<Hive> hives = new Vector<Hive>();
	Vector<FoodSource> foodSources = new Vector<FoodSource>();
	Vector<Obstacle> obstacles = new Vector<Obstacle>();
	Vector<Bee> bees = new Vector<Bee>();

	public ForagingBeeSimulation(long seed) {
		super(seed);
	}

	public void start() {
		super.start();
		initSimulation();
		schedule.scheduleRepeating(this);
	}
	
	public void step(SimState state) {
		STEPS = STEPS + 1;
		if(STEPS == 5000)
		{
    		System.out.println(FOODUNITS);
	    	System.exit(0);
		}
		ListIterator<IMovingAgent> li = agents.listIterator();
		while (li.hasNext()) {
			IMovingAgent a = li.next();
			if (Math.abs(a.getLocation().z) > 0.000001) {
				System.err.println("Agent " + a + "  z:"
						+ a.getLocation().z);
			}
		}
	}
	
	public void prepareSimulation() {
		
		Hive hive = new Hive(this, new Point3d(100, 100, MIDDLE_LENGTH), HIVE_DIAMETER);
		addAgent(hive);
		
		HiveEntrance entrance = new HiveEntrance(this, hive, 0);
		hive.setEntrance(entrance);
		addAgent(entrance);
					
		FoodSource f1 = new FoodSource(this, new Point3d(300, 300, MIDDLE_LENGTH), 20, new Color(0xd0, 0x00, 0x00));
		addAgent(f1);
	}

	public void initSimulation() {
		
		Hive hive = hives.get(0);
		if (hive != null) {
			Point3d fsl = hive.getLocation();

			int i;
			for (i = 0; i < maxBees; i++) {
				Bee b = new Bee(this, hive, new Point3d(fsl.x, fsl.y,
						MIDDLE_LENGTH));
				addAgent(b);
			}
		}
		
		switch(numObstacles)
        {
        case 1:
//        	Obstacle o;
//    		o = new Obstacle(this, new Point3d(155, 130, MIDDLE_LENGTH),
//    				20);
//    		addAgent(o);
           break;
        case 2:
//    		Obstacle o1;
//    		o1 = new Obstacle(this, new Point3d(155, 130, MIDDLE_LENGTH),
//   				20);
//    		addAgent(o1);
//    		Obstacle o2;
//    		o2 = new Obstacle(this, new Point3d(200, 50, MIDDLE_LENGTH), 20);
//    		addAgent(o2);
           break;
        }

		ListIterator<IMovingAgent> li;
		li = agents.listIterator();
		while (li.hasNext()) {
			IIterationAgent agent = (IIterationAgent) li.next();
			if(numObstacles == 1) {
				Object o = schedule.scheduleRepeating(agent);
				agent.setSchedulerInformation(o);
			}
			if(numObstacles == 2) {
				Object o1 = schedule.scheduleRepeating(agent);
				agent.setSchedulerInformation(o1);
				Object o2 = schedule.scheduleRepeating(agent);
				agent.setSchedulerInformation(o2);
			}
		}	
	}

	public boolean isOutside(IMovingAgent agent) {
		Point3d l = agent.getLocation();

		return ((l.x < X_MIN) | (l.x >= X_MAX) | (l.y < Y_MIN) | (l.y >= Y_MAX)
				| (l.z < Z_MIN) | (l.z >= Z_MAX));
	}

	public boolean isOutsideXY(IMovingAgent agent) {
		Point3d l = agent.getLocation();
		return ((l.x < X_MIN) | (l.x >= X_MAX) | (l.y < Y_MIN) | (l.y >= Y_MAX));
	}

	public boolean isOutsideZ(IMovingAgent agent) {
		Point3d l = agent.getLocation();
		return (l.z < Z_MIN) | (l.z >= Z_MAX);
	}

	public boolean addAgent(IMovingAgent agent) {
		if (!agents.contains(agent)) {
			agents.add(agent);

			if (agent instanceof Bee) {
				bees.add((Bee) agent);
			} else {
				if (agent instanceof Hive) {
					hives.add((Hive) agent);
				} else {
					if (agent instanceof FoodSource) {
						foodSources.add((FoodSource) agent);
					} else {
						if (agent instanceof Obstacle) {
							obstacles.add((Obstacle) agent);
						}
					}
				}
			}

			double r = agent.getSphereRadius();
			if (r > maxAgentSphereRadius)
				maxAgentSphereRadius = r;

			return true;
		}

		return false;
	}

	public abstract Object[] getObjectsWithinDistance(IMovingAgent agent,
			double distance);


	public int getNumBees() {
		return maxBees;
	}

	public void setNumBees(int value) {
		maxBees = value;
	}
	
	public int getNumberOfObstacles() {
		return numObstacles;
	}
	
	public void setNumberOfObstacles(int value) {
		numObstacles = value;
	}
	
	public Interval domNumberOfObstacles() {
		return new Interval(1, 2);
	}

	public boolean getAvoidObstacles() {
		return avoidObstacles;
	}

	public void setAvoidObstacles(boolean value) {
		avoidObstacles = value;
	}
}
