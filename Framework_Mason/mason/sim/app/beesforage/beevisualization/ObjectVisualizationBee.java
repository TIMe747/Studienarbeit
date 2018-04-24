package sim.app.beesforage.beevisualization;

import java.awt.Graphics2D;
import sim.app.beesforage.simulation.IVisualAgent;
import sim.portrayal.DrawInfo2D;

public class ObjectVisualizationBee extends ObjectVisualization {

	private static final long serialVersionUID = -2619183105102143817L;

	public ObjectVisualizationBee(IVisualAgent agent) {
		super(agent);
	}

	public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		
		double radiusX = info.draw.width * agent.getSphereRadius();
		double radiusY = info.draw.height * agent.getSphereRadius();

		graphics.setColor(getColor());

		graphics.fillRect((int) (info.draw.x - radiusX),
				(int) (info.draw.y - radiusY), (int) (radiusX * 3),
				(int) (radiusY * 3));
	}
}
