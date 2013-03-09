
import fysik2d.*;

public class MouseForce extends Force2 {
	
	private Vec2 mp; // mouse position
	private Vec2 handtag;
	// var i det lokala kordinatsystemet som man klickade med musen

	private boolean mouse_up;
	
	private Vec2 _at;
	private Vec2 _get;
	
	public MouseForce(Vec2 mouse_p, RigidBody b) {
		Matrix2x2 ir = Matrix2x2.makeRotate(-b.getOrientation());
		handtag = ir.mul( mouse_p.sub(b.getPosition()) );
		
		mp = mouse_p;
		
		mouse_up = false;
	}
	
	public void setMousePosition(Vec2 mouse_p) {
		mp = mouse_p;
	}
	
	public void mouseUp() {
		mouse_up = true;
	}
	
	public void prepare(double t, State s, RigidBody b) {
		_at = (b.getRotation().mul(handtag)).add(b.getPosition());
		
		double k = 10.0;
		_get = mp.sub(_at).mul(k);
		
	}
	
	public Vec2 get() {
		return _get;
	}
	
	public Vec2 at() {
		return _at;
	}
	
	public boolean dispell() {
		return mouse_up;
	}

}
