package sim.app.beesforage.utils;

import sim.app.beesforage.simulation.IMovingAgent;

public class Filter {

	public static final Object[] filter(Object[] objects, Class<?> theClass) {
		Object o;
		int i, k;
		for (i = 0, k = 0; i < objects.length; i++) {
			o = objects[i];
			if (o.getClass() == theClass) {
				objects[k] = o;
				k++;
			}
		}

		Object[] filteredObjects = objects;

		if (i != k) {
			filteredObjects = new IMovingAgent[k];
			System.arraycopy(objects, 0, filteredObjects, 0, k);
		}

		return filteredObjects;
	}
}
