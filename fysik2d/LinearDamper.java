package fysik2d;

public class LinearDamper extends Force2 {

	private boolean _remove;

	private Vec2 _at;
	private Vec2 _get;
	private double k;
	
	public LinearDamper(double constant) {
		k = constant;
		_remove = false;
	}
	
	public void prepare(double t, State s, RigidBody b) {
		_at = s.getPosition();
		_get = s.getVelocity().mul(-k);
	}
	
	public Vec2 get() {
		return _get;
	}
	
	public Vec2 at() {
		return _at;
	}
	
	
	public boolean dispell() {
		return _remove;
	}
	
	public void remove() {
		_remove = true;
	}
}
