package sim.app.beesforage;

import sim.engine.Steppable;

public interface IIterationAgent extends Steppable {
	
	Object getSchedulerInformation();
	
	void setSchedulerInformation(Object o);
}
