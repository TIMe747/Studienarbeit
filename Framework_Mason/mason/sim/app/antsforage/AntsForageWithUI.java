package sim.app.antsforage;

import sim.engine.*;
import sim.display.*;
import sim.portrayal.grid.*;
import java.awt.*;


import javax.swing.*;

public class AntsForageWithUI extends GUIState {
	
    public static long startnanoseconds = 0;
    public Display2D display;
    public JFrame displayFrame;

    FastValueGridPortrayal2D homePheromonePortrayal = new FastValueGridPortrayal2D("Home Pheromone");
    FastValueGridPortrayal2D foodPheromonePortrayal = new FastValueGridPortrayal2D("Food Pheromone");
    FastValueGridPortrayal2D sitesPortrayal = new FastValueGridPortrayal2D("Site", true); 
    FastValueGridPortrayal2D obstaclesPortrayal = new FastValueGridPortrayal2D("Obstacle", true);
    SparseGridPortrayal2D bugPortrayal = new SparseGridPortrayal2D();
                
    public static void main(String[] args){
    	new AntsForageWithUI().createController();
    }
    
    public AntsForageWithUI() { super(new AntsForage(System.currentTimeMillis())); }
    public AntsForageWithUI(SimState state) { super(state); }
    
    public Object getSimulationInspectedObject() { return state; }

    public static String getName() { return "ACO"; }
    
    public void setupPortrayals() {
    	
        AntsForage af = (AntsForage)state;

        homePheromonePortrayal.setField(af.toHomeGrid);
        homePheromonePortrayal.setMap(new sim.util.gui.SimpleColorMap(0, AntsForage.LIKELY_MAX_PHEROMONE, Color.white, new Color(0,255,0,255))
        { public double filterLevel(double level) { return Math.sqrt(Math.sqrt(level)); } } );

        foodPheromonePortrayal.setField(af.toFoodGrid);
        foodPheromonePortrayal.setMap(new sim.util.gui.SimpleColorMap(0, AntsForage.LIKELY_MAX_PHEROMONE, new Color(139,026,026,0), new Color(139,026,026,255) )
        { public double filterLevel(double level) { return Math.sqrt(Math.sqrt(level)); } } );
        
        sitesPortrayal.setField(af.sites);
        sitesPortrayal.setMap(new sim.util.gui.SimpleColorMap(0, 1, new Color(0,0,0,0), new Color(255,0,0,255)));
        
        obstaclesPortrayal.setField(af.obstacles);
        obstaclesPortrayal.setMap(new sim.util.gui.SimpleColorMap(0, 1, new Color(0,0,0,0), new Color(130,130,130,255)));
        
        bugPortrayal.setField(af.buggrid);
            
        display.reset();

        display.repaint();
    }
    
    public void start() {
        super.start();
        setupPortrayals();
        startnanoseconds = System.nanoTime();
    }
            
    public void load(SimState state) {
        super.load(state);
        setupPortrayals();
    }

    public void init(Controller c) {
        
    	super.init(c);
        
        display = new Display2D(400,400,this); 
        displayFrame = display.createFrame();
        c.registerFrame(displayFrame);  
        displayFrame.setVisible(true);

        display.attach(homePheromonePortrayal,"Pheromones To Home");
        display.attach(foodPheromonePortrayal,"Pheromones To Food");
        display.attach(sitesPortrayal,"Site Locations");
        display.attach(obstaclesPortrayal,"Obstacles");
        display.attach(bugPortrayal,"Agents");
        
        // specify the backdrop color  -- what gets painted behind the displays
        display.setBackdrop(Color.white);
    }
        
    public void quit() {
        super.quit();
        
        // disposing the displayFrame automatically calls quit() on the display,
        // so we don't need to do so ourselves here.
        if (displayFrame!=null) displayFrame.dispose();
        displayFrame = null;  // let gc
        display = null;       // let gc
    }
}
    
    
    
    
