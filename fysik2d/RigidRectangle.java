package fysik2d;

/**
This class represents a rigid body with the shape of a square.
*/
public class RigidRectangle extends RigidBody {
	
	/**
	Creates a RigidRectangle
	*/
	public RigidRectangle(Vec2 pos,double theta,double mass,double b,double h) {
		super(pos, theta, mass);
		addMyPoints(b,h);
	}
	
	public RigidRectangle(Vec2 pos, double th, Vec2 vel, double av, double mass, double b,double h) {
		super(pos, th, vel, av, mass);
		addMyPoints(b,h);
	}
	
	private void addMyPoints(double b, double h) {
		addPoint(new Vec2(-b,  h));
		addPoint(new Vec2( b,  h));
		addPoint(new Vec2( b, -h));
		addPoint(new Vec2(-b, -h));
		closePolygon();
	}
	
}
