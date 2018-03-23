/**
 * 
 */
package sim.app.beesforage.beevisualization;

import java.awt.Graphics2D;

import sim.app.beesforage.simulation.IVisualAgent;
import sim.portrayal.DrawInfo2D;


/**
 * This class provides the ability to draw itself as an cube in a 2D world.
 * 
 * @author hoehne
 * 
 */
public class ObjectVisualizationBee extends ObjectVisualization {
	/**
	 * The constructor that takes a visual agent interface as an argument.
	 * 
	 * @param agent
	 */
	public ObjectVisualizationBee(IVisualAgent agent) {
		super(agent);
	}

	// ========== SimplePortrayal2D ==========

	/**
	 * Drawing this visualization object as a square that is much more faster
	 * than drawing circles.
	 * 
	 * @param object
	 *            The object itself.
	 * @param graphics
	 *            Where (output device) to draw.
	 * @param info
	 *            Where (location) to draw.
	 */
	public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		double diamx = info.draw.width * 2;
		double diamy = info.draw.height * 2;
		double radiusX = info.draw.width * agent.getSphereRadius();
		double radiusY = info.draw.height * agent.getSphereRadius();

		graphics.setColor(getColor());

		graphics.fillRect((int) (info.draw.x - radiusX),
				(int) (info.draw.y - radiusY), (int) (radiusX * 2),
				(int) (radiusY * 2));
	}
}
