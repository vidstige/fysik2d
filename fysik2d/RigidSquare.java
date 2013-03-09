package fysik2d;

/**
This class represents a rigid body with the shape of a square.
*/
public class RigidSquare extends RigidBody {
	
	/**
	Creates a RigidSquare
	*/
	public RigidSquare(Vec2 pos, double theta, double mass, double size) {
		super(pos, theta, mass);
		addMyPoints(size);
	}
	
	public RigidSquare(Vec2 pos, double th, Vec2 vel, double av, double mass, double size) {
		super(pos, th, vel, av, mass);
		addMyPoints(size);
	}
	
	private void addMyPoints(double size) {
		addPoint(new Vec2(-size,  size));
		addPoint(new Vec2( size,  size));
		addPoint(new Vec2( size, -size));
		addPoint(new Vec2(-size, -size));
		closePolygon();
	}
	
}
