package fysik2d;

public class AngularDamper extends Force2 {
	
	private boolean _remove;
	
	private Vec2 _at;
	private Vec2 _get;
	
	private double k;
	
	public AngularDamper(double constant) {
		k = constant;
		_remove = false;
	}	
	
	public void prepare(double t, State s, RigidBody b) {
		double o = -s.getOrientation();
		Vec2 r = new Vec2(Math.cos(o), Math.sin(o));
		_at = s.getPosition().add(r);
		
		_get = r.cross(-k * s.getAngularVelocity());
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
