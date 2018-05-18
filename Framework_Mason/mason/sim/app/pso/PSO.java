package sim.app.pso;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.util.Interval;
import sim.util.MutableDouble2D;

public class PSO extends SimState 
    {       
    private static final long serialVersionUID = 1;

    public Continuous2D space;
    public static int STEPS = -1;
    public double width = 10.24;
    public double height = 10.24;
    public Particle[] particles;
    int prevSuccessCount = -1; 
    public double lokalerVertrauenskoeffizient = 0.5;
    public double globalerVertrauenskoeffizient = 0.5;
    
    // public modifier values
    public int numParticles = 100;
    public int getNumParticles() { return numParticles; }
    public void setNumParticles(int val) { if (val >= 0) numParticles = val; }
    
    public double getlokalerVertrauenskoeffizient() { return lokalerVertrauenskoeffizient; }
    public void setLlokalerVertrauenskoeffizient(double val) {if (val >= 0 && val <= 1.0) lokalerVertrauenskoeffizient = val; }
    public Object domLlokalerVertrauenskoeffizient() { return new Interval(0.0, 1.0); }
    
    public double getGlobalglobalerVertrauenskoeffizient() { return globalerVertrauenskoeffizient;}
    public void setglobalerVertrauenskoeffizient(double val) {if (val >= 0 && val <= 1.0) globalerVertrauenskoeffizient = val; }
    public Object domglobalerVertrauenskoeffizient() { return new Interval(0.0, 1.0); }
    
    public double initialVelocityRange = 1.0;

    public double velocityScalar = 2.7;

    public int fitnessFunction = 0;
    public int getFitnessFunction() { return fitnessFunction; }
    public void setFitnessFunction(int val) { fitnessFunction = val; }
    public Object domFitnessFunction() 
        { 
        return new String[] {"Rastrigin"};
        }
            
    private Evaluatable mapFitnessFunction(int val)
        {
        	return new Rastrigin();
        }

    public double[] fitnessFunctionLowerBound = 
        {
        920,
        950,
        998,
        200
        };
    
    public double successThreshold = 1.0e-8; 
    public double bestVal = 0;
    MutableDouble2D bestPosition = new MutableDouble2D();
        
    public PSO(long seed)
        {
        super(seed);
        }
        
    public void updateBest(double currVal, double currX, double currY)
        {
        if (currVal > bestVal)
            {
            bestVal = currVal;
            bestPosition.setTo(currX, currY);
            }               
        }
        
    
        
    public void start()
        {
        // reset the global best
        bestVal = 0;
                
        super.start();
        particles = new Particle[numParticles];
        space = new Continuous2D(height, width, height);
        Evaluatable f = mapFitnessFunction(fitnessFunction);            
                        
        for (int i = 0; i < numParticles; i++)
            {
            double x = (random.nextDouble() * width) - (width * 0.5);
            double y = (random.nextDouble() * height) - (height * 0.5);
            double vx = (random.nextDouble() * initialVelocityRange) - (initialVelocityRange * 0.5);
            double vy = (random.nextDouble() * initialVelocityRange) - (initialVelocityRange * 0.5);
                        
            final Particle p = new Particle(x, y, vx, vy, this, f, i);
            particles[i] = p;
                        
            schedule.scheduleRepeating(Schedule.EPOCH,1,new Steppable()
                {
                public void step(SimState state) { p.stepUpdateFitness();}
                });
                        
            schedule.scheduleRepeating(Schedule.EPOCH,2,new Steppable()
                {
                public void step(SimState state) { p.stepUpdateVelocity(); }
                });
                        
            schedule.scheduleRepeating(Schedule.EPOCH,3,new Steppable()
                {
                public void step(SimState state) { 
                	p.stepUpdatePosition(); 
                	}
                });
            }                       
                
        schedule.scheduleRepeating(Schedule.EPOCH, 4, new Steppable()
            {
        	
            public void step(SimState state)
                {
            	STEPS = STEPS+1;
            	if(STEPS == 1000) {
            		System.out.println(bestPosition);
        	    	System.exit(0);
            	}
            	
                int successCount = 0;
                for (int i = 0; i < space.allObjects.numObjs; i++)
                    {
                    Particle p = (Particle)space.allObjects.get(i);
                                                        
                    if (Math.abs(p.getFitness() - 1000) <= successThreshold)
                        successCount++;                                                 
                    }
                if (successCount != prevSuccessCount)
                    {
                    prevSuccessCount = successCount;
                    //System.out.println("SuccessCount = " + successCount);                                         
                    if (successCount == numParticles)
                        state.kill();
                    }
                }
            });             
        }

    public static void main(String[] args) {
        doLoop(PSO.class, args);
        System.exit(0);
    }

}
