package sim.app.pso;

import sim.app.antsforage.AntsForage;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Particle
    {       
    private static final long serialVersionUID = 1;

    double bestVal = 0;     
    MutableDouble2D bestPosition = new MutableDouble2D();

    MutableDouble2D position = new MutableDouble2D();
    MutableDouble2D velocity = new MutableDouble2D();       
                
    PSO pso;
    Evaluatable fitnessFunction;
    int index;
        
    public Particle() {
        super();
    }
        
    public Particle(double x, double y, double vx, double vy, PSO pso, Evaluatable f, int index) {
    	
        super();

        this.position.setTo(x, y);
        this.velocity.setTo(vx, vy);
                
        this.pso = pso;
        this.fitnessFunction = f;
        pso.space.setObjectLocation(this,new Double2D(position));
        this.index = index;
        
    }

    public void updateBest(double currVal, double currX, double currY) {
        if (currVal > bestVal){
            bestVal = currVal;
            bestPosition.setTo(currX, currY);          
            pso.updateBest(currVal, currX, currY);
        }
    }
        
    public double getFitness() {
        return fitnessFunction.calcFitness(position.x,position.y);
    }
        
    public void stepUpdateFitness() {
        updateBest(getFitness(), position.x, position.y);
    }

    public void stepUpdateVelocity() {
    	
        double x = position.x;
        double y = position.y;
    
        // calc x component
        double inertia = velocity.x;
        double pDelta = bestPosition.x - x;
        double gDelta = pso.bestPosition.x - x;
        double pWeight = pso.lokalerVertrauenskoeffizient;
        double gWeight = pso.globalerVertrauenskoeffizient;
        double vx = (0.9*inertia + pWeight*pDelta + gWeight*gDelta) / (1+pWeight+gWeight);
                 
        // calc y component
        inertia = velocity.y;
        pDelta = bestPosition.y - y;
        gDelta = pso.bestPosition.y - y;
        pWeight = pso.lokalerVertrauenskoeffizient;
        gWeight = pso.globalerVertrauenskoeffizient;
        double vy = (0.9*inertia + pWeight*pDelta +  gWeight*gDelta) / (1+pWeight+gWeight);

        vx *= pso.velocityScalar;
        vy *= pso.velocityScalar;
                
        // update velocity
        velocity.setTo(vx, vy);         
        }
        
    public void stepUpdatePosition()
        {
        //System.out.println(
        //              "Best: " + n.bestVal + " (" + n.bestPosition.x + ", " + n.bestPosition.y + ")");
        position.addIn(velocity);
        pso.space.setObjectLocation(this, new Double2D(position));
        
        }

    }
