package fysik2d;

public class Euler implements Integrator {
	public State step(double t, double dt, State y, DiffEquation ode) {
		return y.add( ode.diffEq(t, y).mul(dt) );
	}
}
