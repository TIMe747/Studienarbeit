package sim.app.bee;


import sim.engine.*;
import sim.field.grid.*;
import sim.util.*;


public class BeesForage extends SimState
    {

	private static final long serialVersionUID = 1;

    public static final int GRID_HEIGHT = 100;
    public static final int GRID_WIDTH = 100;

    public static final int HOME_XMIN = 75;
    public static final int HOME_XMAX = 75;
    public static final int HOME_YMIN = 75;
    public static final int HOME_YMAX = 75;

    public static final int FOOD_XMIN = 25;
    public static final int FOOD_XMAX = 25;
    public static final int FOOD_YMIN = 25;
    public static final int FOOD_YMAX = 25;
        
    public static final int ALGORITHM_VALUE_ITERATION = 1;
    public static final int ALGORITHM_TEMPORAL_DIFERENCE = 2;
    public static final int ALGORITHM = ALGORITHM_VALUE_ITERATION;
        
    public static final int HOME = 1;
    public static final int FOOD = 2;
        
    public int numBees = 1000;
    public double reward = 1.0;
    public double updateCutDown = 0.9;
    public double diagonalCutDown = computeDiagonalCutDown();
    public double computeDiagonalCutDown() { return Math.pow(updateCutDown, Math.sqrt(2)); }
    public double momentumProbability = 0.8;
    public double randomActionProbability = 0.1;

        
        
    // some properties
    public int getNumBees() { return numBees; }
    public void setNumBees(int val) {if (val > 0) numBees = val; }

    public double getReward() { return reward; }
    public void setReward(double val) {if (val >= 0) reward = val; }

    public double getCutDown() { return updateCutDown; }
    public void setCutDown(double val) {if (val >= 0 && val <= 1.0) updateCutDown = val;  diagonalCutDown = computeDiagonalCutDown(); }
    public Object domCutDown() { return new Interval(0.0, 1.0); }

    public double getMomentumProbability() { return momentumProbability; }
    public void setMomentumProbability(double val) {if (val >= 0 && val <= 1.0) momentumProbability = val; }
    public Object domMomentumProbability() { return new Interval(0.0, 1.0); }

    public double getRandomActionProbability() { return randomActionProbability; }
    public void setRandomActionProbability(double val) {if (val >= 0 && val <= 1.0) randomActionProbability = val; }
    public Object domRandomActionProbability() { return new Interval(0.0, 1.0); }


    public IntGrid2D sites = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public DoubleGrid2D toFoodGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public DoubleGrid2D toHomeGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public SparseGrid2D bugGrid = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);

    public BeesForage(long seed)
        { 
        super(seed);
        }
        
    public void start()
        {
        super.start();  // clear out the schedule

        // make new grids
        sites = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
        toFoodGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
        toHomeGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
        bugGrid = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);

        // initialize the grid with the home and food sites
        for( int x = HOME_XMIN ; x <= HOME_XMAX ; x++ )
            for( int y = HOME_YMIN ; y <= HOME_YMAX ; y++ )
                sites.field[x][y] = HOME;
        for( int x = FOOD_XMIN ; x <= FOOD_XMAX ; x++ )
            for( int y = FOOD_YMIN ; y <= FOOD_YMAX ; y++ )
                sites.field[x][y] = FOOD;

        for(int x=0; x < numBees; x++)
            {
            Bee bee = new Bee(reward);
            bugGrid.setObjectLocation(bee,(HOME_XMAX+HOME_XMIN)/2,(HOME_YMAX+HOME_YMIN)/2);
            schedule.scheduleRepeating(Schedule.EPOCH + x, 0, bee, 1);
            }
        }

    public static void main(String[] args)
        {
        doLoop(BeesForage.class, args);
        System.exit(0);
        }    
    }
    
    

