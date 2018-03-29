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
import sim.app.beesforage.simulation.IVisualAgent;
import sim.app.beesforage.simulation.Obstacle;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Interval;

public abstract class ForagingBeeSimulation extends SimState implements
		Steppable, IAgentLocator {

	private static final long serialVersionUID = -2187248402977949189L;
	
	/*Maße des Displays 400x400*/
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
	
	/*Durchmesser des Bienenstocks*/
	static public final double HIVE_DIAMETER = 80;
	
	/*Hindernissen ausweichen j/n*/
	public boolean avoidObstacles = true;
	
	/*Anzahl der Bienen*/
	public int maxBees = 800;
	public int numObstacles = 1;
	public double comNoise = 0.01;
	public double pForgettingSource = 30E-6;
	public double pStartScouting = 39E-6;
	public double pForagingAgain = 500E-6;
	public double colonyNectarNeed = 0.5;
	public int maxSearchSteps = 100;
	double maxAgentSphereRadius = Double.MIN_VALUE;
	
	/*Objekte der Simulation*/
	Vector<IMovingAgent> agents = new Vector<IMovingAgent>();
	Vector<Hive> hives = new Vector<Hive>();
	Vector<FoodSource> foodSources = new Vector<FoodSource>();
	Vector<Obstacle> obstacles = new Vector<Obstacle>();
	Vector<Bee> bees = new Vector<Bee>();

	/*Seed für Zufallszahl*/
	public ForagingBeeSimulation(long seed) {
		super(seed);
	}

	/*Starten der Simulation*/
	public void start() {
		super.start();

		initSimulation();
		schedule.scheduleRepeating(this);
	}
	
	/*Schritt von Zustand zu Zustand*/
	public void step(SimState state) {
		
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
		// create the hive
		Hive hive = new Hive(this, new Point3d(50, 50, MIDDLE_LENGTH), HIVE_DIAMETER, 2000);
		addAgent(hive);
		// create the entrance
		HiveEntrance entrance = new HiveEntrance(this, hive, 0);
		hive.setEntrance(entrance);
		addAgent(entrance);
					
		FoodSource f1 = new FoodSource(this, new Point3d(300, 300,
				MIDDLE_LENGTH), 20, new Color(0xd0, 0x00, 0x00), 100);
		
		
//		addAgent(f1);
//		FoodSource f2 = new FoodSource(this, new Point3d(50, 350,
//				MIDDLE_LENGTH), 15, new Color(0xc0, 0xc0, 0x00), 200);
//		addAgent(f2);
//		FoodSource f3 = new FoodSource(this, new Point3d(200, 200,
//				MIDDLE_LENGTH), 35, new Color(0xd0, 0x00, 0xd0), 300);
//		addAgent(f3);
//		FoodSource f4 = new FoodSource(this, new Point3d(180, 250,
//				MIDDLE_LENGTH), 18, new Color(0x00, 0xd0, 0xd0), 300);
//		addAgent(f4);
	}

	private void initSimulation() {
		/*
		 * If a hive exists create the maximum number of bees.
		 */
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
		case 0: 
			break;	
        case 1:
        	Obstacle o;
    		o = new Obstacle(this, new Point3d(155, 130, MIDDLE_LENGTH),
    				20);
    		addAgent(o);
            break;
        case 2:
    		Obstacle o1;
    		o1 = new Obstacle(this, new Point3d(155, 130, MIDDLE_LENGTH),
   				20);
    		addAgent(o1);
    		Obstacle o2;
    		o2 = new Obstacle(this, new Point3d(200, 50, MIDDLE_LENGTH), 20);
    		addAgent(o2);
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

	public boolean removeAgent(IMovingAgent agent) {
		boolean status = agents.remove(agent);
		if (agent instanceof Bee) {
			bees.remove(agent);
		} else {
			if (agent instanceof Hive) {
				hives.remove(agent);
			} else {
				if (agent instanceof FoodSource) {
					foodSources.remove(agent);
				} else {
					if (agent instanceof Obstacle) {
						obstacles.remove(agent);
					}
				}
			}
		}

		if (status) {
			double radius = agent.getSphereRadius();
			if (radius >= maxAgentSphereRadius) {
				maxAgentSphereRadius = computeMaxAgentSphereRadius();
			}
		}

		if (agent instanceof IIterationAgent) {
			IIterationAgent a = (IIterationAgent) agent;
			Stoppable stop = (Stoppable) a.getSchedulerInformation();
			stop.stop();
		}

		if (agent instanceof IVisualAgent) {
			Object o = removeFromVisualization((IVisualAgent) agent);
			System.out.println("removeFromVisualization: " + o);
		}

		return status;
	}

	private double computeMaxAgentSphereRadius() {
		double max = Double.MIN_VALUE;
		double r;

		ListIterator<IMovingAgent> li = agents.listIterator();
		while (li.hasNext()) {
			r = li.next().getSphereRadius();
			if (r > max)
				max = r;
		}

		return max;
	}

	public abstract Object[] getObjectsWithinDistance(IMovingAgent agent,
			double distance);

//	public double getMaxSphereRadius() {
//		return this.maxAgentSphereRadius;
//	}

	public int getNumBees() {
		return maxBees;
	}

	public void setNumBees(int value) {
		maxBees = value;
	}

//	public Interval domNumberOfBees() {
//		return new Interval(1, 2000);
//	}
	
	public int getNumberOfObstacles() {
		return numObstacles;
	}
	
	public void setNumberOfObstacles(int value) {
		numObstacles = value;
	}
	
	public Interval domNumberOfObstacles() {
		return new Interval(0, 2);
	}

	public boolean getAvoidObstacles() {
		return avoidObstacles;
	}

	public void setAvoidObstacles(boolean value) {
		avoidObstacles = value;
	}

//	public int getMaxSearchSteps() {
//		return maxSearchSteps;
//	}
//
//	public void setMaxSearchSteps(int value) {
//		maxSearchSteps = value;
//	}
//
//	public Interval domMaxSearchSteps() {
//		return new Interval(1, 200);
//	}
//
//	public double getCommunicationNoise() {
//		return comNoise;
//	}
//
//	public void setCommunicationNoise(double value) {
//		comNoise = value;
//	}
//
//	public Interval domCommunicationNoise() {
//		return new Interval(0, .2);
//	}
//
//	public double getpForgettingSource() {
//		return pForgettingSource;
//	}
//
//	public void setpForgettingSource(double value) {
//		pForgettingSource = value;
//	}
//
//	public Interval dompForgettingSource() {
//		return new Interval(1E-6, 100E-6);
//	}
//
//	public double getpForagingAgain() {
//		return pForagingAgain;
//	}
//
//	public void setpForagingAgain(double value) {
//		pForagingAgain = value;
//	}
//
//	public Interval dompForagingAgain() {
//		return new Interval(0, 500E-6);
//	}
//
//	public double getpStartScouting() {
//		return pStartScouting;
//	}
//
//	public void setpStartScouting(double value) {
//		pStartScouting = value;
//	}
//
//	public Interval dompStartScouting() {
//		return new Interval(0, 250E-6);
//	}
//
//	public double getColonyNectarNeed() {
//		return colonyNectarNeed;
//	}
//
//	public void setColonyNectarNeed(double value) {
//		colonyNectarNeed = value;
//	}
//
//	public Interval domColonyNectarNeed() {
//		return new Interval(0, 1.0);
//	}
//
//	public Vector<Hive> getHives() {
//		return hives;
//	}
//
//	public Vector<FoodSource> getFoodSources() {
//		return foodSources;
//	}
//
//	public Vector<Obstacle> getObstacles() {
//		return obstacles;
//	}
}
