/**
 * 
 */
package sim.app.beesforage.beevisualization;

import java.awt.Graphics2D;
import sim.app.beesforage.simulation.IVisualAgent;
import sim.portrayal.DrawInfo2D;

public class ObjectVisualizationHiveFood extends ObjectVisualization {

	private static final long serialVersionUID = -4810867234012860955L;

	public ObjectVisualizationHiveFood(IVisualAgent agent) {
		super(agent);
	}

	public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		double radiusX = info.draw.width * agent.getSphereRadius();
		double radiusY = info.draw.height * agent.getSphereRadius();

		graphics.setColor(getColor());

		graphics.fillOval((int) (info.draw.x - radiusX),
				(int) (info.draw.y - radiusY), (int) (radiusX * 2),
				(int) (radiusY * 2));
	}
}
