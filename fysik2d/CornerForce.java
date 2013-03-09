package fysik2d;


public class CornerForce extends Force2 {

	private LineSegment lls;
	private Vec2 lc;
	private RigidBody body;

	private double threshold2;
	private double threshold;
	
	private Vec2 _get;
	private Vec2 _at;
	
	public CornerForce(LineSegment ls,Vec2 lcorner,RigidBody b,double thres2) {
		lls = ls;
		lc = lcorner;
		body = b;
		
		threshold2 = thres2;
		threshold = Math.sqrt(thres2);
		
		
		_at = null;
		_get = null;
	}
	
	public void prepare(double t, State s, RigidBody b) {
		// get world line segment at time t. (assuming it stands still!!)
		LineSegment world_line =
			lls.worldTransform(body.getRotation(), body.getPosition());

		// get world coordinate for corner. 
		Matrix2x2 rot = Matrix2x2.makeRotate(s.getOrientation());
		Vec2 wc = (rot.mul(lc)).add(s.getPosition());
		
		
		Vec2 online = world_line.pointOn( wc );
		Vec2 dir = wc.sub(online);
		double dist = dir.length();
		
		
		_at = wc;
		double k = 20.0;
		
		double diff = threshold - dist;
		if (dist == 0 || diff < 0)
			_get = Vec2.NULL;
		else
			_get = dir.mul( diff * k / dist);

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
