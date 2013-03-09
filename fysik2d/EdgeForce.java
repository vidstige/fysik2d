package fysik2d;


public class EdgeForce extends Force2 {

	private LineSegment lls;
	private Vec2 lc;
	private RigidBody body;

	private double threshold2;
	private double threshold;
	
	private Vec2 _get;
	private Vec2 _at;
	
	public EdgeForce(LineSegment ls, Vec2 lcorner, RigidBody b, double thres2) {
		lls = ls;
		lc = lcorner;
		body = b;
		
		threshold2 = thres2;
		threshold = Math.sqrt(thres2);
		
		
		_at = null;
		_get = null;
	}
	
	public void prepare(double t, State s, RigidBody b) {
		// get world line segment at time t.
		LineSegment world_line =
			lls.worldTransform(s.getOrientation(), s.getPosition());
		
		// get world coordinate for corner. (assuming it stands still!!)
		Vec2 wc = ((body.getRotation()).mul(lc)).add(body.getPosition());
		
		Vec2 online = world_line.pointOn( wc );
		Vec2 dir = online.sub(wc);
		double dist = dir.length();
		
		_at = online;
		double k = 20.0;
		
		double diff = threshold - dist;
		if (dist == 0 || diff < 0) 
			_get = Vec2.NULL;
		else
			_get = (dir.div(dist)).mul( diff * k );
		

	}
	
	public Vec2 get() {
		return _get;
	}
	
	public Vec2 at() {
		return _at;
	}
	
	public boolean dispell() {
		return true;
	}
}
