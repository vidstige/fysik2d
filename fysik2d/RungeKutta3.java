package fysik2d;

public class RungeKutta3 implements Integrator {
	public State step(double t, double dt, State y, DiffEquation ode) {
		State k1 = ode.diffEq(t, y).mul(dt);
		State k2 = ode.diffEq(t+0.5*dt, y.add(k1.mul(0.5))).mul(dt);
		State k3 = ode.diffEq(t+dt, (y.sub(k1)).add(k2.mul(2))).mul(dt);
		
		return y.add( (k1.add(k2.mul(4)).add(k3) ).mul(1.0/6.0) );
	}
}
