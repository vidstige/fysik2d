package fysik2d;

public interface Integrator {
	public State step(double t, double dt, State y, DiffEquation ode);
}
