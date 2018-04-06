package sim.app.antsforage;

import sim.engine.*;
import sim.field.grid.*;
import sim.util.*;

public class AntsForage extends SimState {
	
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

    public static final int NO_OBSTACLES = 0;
    public static final int ONE_OBSTACLE = 1;
    public static final int TWO_OBSTACLES = 2;
    public static final int ONE_LONG_OBSTACLE = 3;
        
    public static final int ALGORITHM_VALUE_ITERATION = 1;
    public static final int ALGORITHM_TEMPORAL_DIFERENCE = 2;
    public static final int ALGORITHM = ALGORITHM_VALUE_ITERATION;
        
    public static final double IMPOSSIBLY_BAD_PHEROMONE = -1;
    public static final double LIKELY_MAX_PHEROMONE = 3;
        
    public static final int HOME = 1;
    public static final int FOOD = 2;
        
    public int numObstacles = 2;
    public int numAnts = 1000;
    public double evaporation = 0.9;
    public double reward = 1.0;
    public double updateCutDown = 0.9;
    public double diagonalCutDown = computeDiagonalCutDown();
    public double computeDiagonalCutDown() { return Math.pow(updateCutDown, Math.sqrt(2)); }
    public double momentumProbability = 0.9;
    public double randomActionProbability = 0.1;

    
    public int getNumAnts() { return numAnts; }
    public void setNumAnts(int val) {if (val > 0) numAnts = val; }
    
    public int getNumObstacles() {return numObstacles; }
    public void setNumObstacles(int val) {if (val >= 0 && val <= 2) numObstacles = val; }
    public Object domNumObstacles() { return new Interval(0, 2); }
    
    public double getEvaporationCon() { return evaporation; }
    public void setEvaporationCon(double val) {if (val >= 0 && val <= 1.0) evaporation = val; }
    public Object domEvaporationCon() { return new Interval(0.0, 1.0); }

    public double getMomentumProbability() { return momentumProbability; }
    public void setMomentumProbability(double val) {if (val >= 0 && val <= 1.0) momentumProbability = val; }
    public Object domMomentumProbability() { return new Interval(0.0, 1.0); }

    public double getRandomActionProbability() { return randomActionProbability; }
    public void setRandomActionProbability(double val) {if (val >= 0 && val <= 1.0) randomActionProbability = val; }
    public Object domRandomActionProbability() { return new Interval(0.0, 1.0); }

    public IntGrid2D sites = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public DoubleGrid2D toFoodGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public DoubleGrid2D toHomeGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public SparseGrid2D buggrid = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
    public IntGrid2D obstacles = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);

    public AntsForage(long seed) { 
        super(seed);
    }
        
    public void start() {
    	
        super.start();

        sites = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
        toFoodGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
        toHomeGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
        buggrid = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
        obstacles = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, 0);

        switch(numObstacles) {
        
            case NO_OBSTACLES:
                break;
                
            case ONE_OBSTACLE:
                for( int x = 0 ; x < GRID_WIDTH ; x++ )
                    for( int y = 0 ; y < GRID_HEIGHT ; y++ )
                        {
                        obstacles.field[x][y] = 0;
                        if( ((x-55)*0.707+(y-35)*0.707)*((x-55)*0.707+(y-35)*0.707)/36+
                            ((x-55)*0.707-(y-35)*0.707)*((x-55)*0.707-(y-35)*0.707)/1024 <= 1 )
                            obstacles.field[x][y] = 1;
                        }
                break;
                
            case TWO_OBSTACLES:
                for( int x = 0 ; x < GRID_WIDTH ; x++ )
                    for( int y = 0 ; y < GRID_HEIGHT ; y++ )
                        {
                        obstacles.field[x][y] = 0;
                        if( ((x-45)*0.707+(y-25)*0.707)*((x-45)*0.707+(y-25)*0.707)/36+
                            ((x-45)*0.707-(y-25)*0.707)*((x-45)*0.707-(y-25)*0.707)/1024 <= 1 )
                            obstacles.field[x][y] = 1;
                        if( ((x-35)*0.707+(y-70)*0.707)*((x-35)*0.707+(y-70)*0.707)/36+
                            ((x-35)*0.707-(y-70)*0.707)*((x-35)*0.707-(y-70)*0.707)/1024 <= 1 )
                            obstacles.field[x][y] = 1;
                        }
                break;
        }

        // initialize the grid with the home and food sites
        for( int x = HOME_XMIN ; x <= HOME_XMAX ; x++ ) {
            for( int y = HOME_YMIN ; y <= HOME_YMAX ; y++ ) {
                sites.field[x][y] = HOME;
            }
        }
        for( int x = FOOD_XMIN ; x <= FOOD_XMAX ; x++ ) {
            for( int y = FOOD_YMIN ; y <= FOOD_YMAX ; y++ ) {
                sites.field[x][y] = FOOD;
            }
        }
        for(int x=0; x < numAnts; x++) {
            Ant ant = new Ant(reward);
            buggrid.setObjectLocation(ant,(HOME_XMAX),(HOME_YMAX));
            schedule.scheduleRepeating(Schedule.EPOCH + x, 0, ant, 1);
        }

	    schedule.scheduleRepeating(Schedule.EPOCH,1, new Steppable() {
		    public void step(SimState state) { 
		    	toFoodGrid.multiply(evaporation); toHomeGrid.multiply(evaporation); 
		    }
	    }, 1);

    }

    public static void main(String[] args){
        doLoop(AntsForage.class, args);
        System.exit(0);
    }    
}
    
    
    
    
    
