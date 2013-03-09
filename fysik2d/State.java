package fysik2d;

/**
 * This represents a state that a RigidBody can take. To do this, the needed
 * parameters are position, velocity, orientation, angular velocity.
 * All the other parameters describing the body are constant over time.
 */
public class State {

	private Vec2 x;
	private Vec2 v;
	private double o;
	private double w;
	
	public State(Vec2 nx, Vec2 nv, double no, double nw) {
		x = nx;
		v = nv;
		o = no;
		w = nw;
	}

	public State add(State s) {
		return new State(x.add(s.x), v.add(s.v), o+s.o, w+s.w);
	}
	public State sub(State s) {
		return new State(x.sub(s.x), v.sub(s.v), o-s.o, w-s.w);
	}
	
	public State mul(double s) {
		return new State(x.mul(s), v.mul(s), o*s, w*s);
	}
	
	
	public Vec2 getPosition() {
		return x;
	}
	
	public Vec2 getVelocity() {
		return v;
	}

	public double getOrientation() {
		return o;
	}
	
	public double getAngularVelocity() {
		return w;
	}
	

}
