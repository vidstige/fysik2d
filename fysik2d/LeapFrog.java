package fysik2d;

public class LeapFrog implements Integrator {
	public State step(double t, double dt, State y, DiffEquation ode) {
		State k1 = ode.diffEq(t, y).mul(dt);
		return y.add( ode.diffEq(t+dt*0.5, y.add(k1.mul(0.5))).mul(dt) );
	}
}
