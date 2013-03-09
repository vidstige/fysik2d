package fysik2d;

/**
 * This class represents a imagined gravity in the 2D world. It is ussually
 * directed down and has a magnitude of about 10 on earth, ie (0, -9.8).
 * The gravity always acts at the center of mass and therefore does not cause
 * any moment.
 */
public class Gravity2 extends Force2{
	
	private Vec2 g;
	
	private Vec2 _at;
	private Vec2 mg;
		
	/**
	 * The value of the gravity in my hometown Umeå, Sweden. This value is
	 * used if gravity is omitted.
	 */
	protected final Vec2 DEFAULT_GRAVITY = new Vec2(0, -9.82);
	
	/**
	 * Creates a gravity.
	 */
	public Gravity2() {
		g = DEFAULT_GRAVITY;
	}
	
	/**
	 * Creates a gravity. The size and direction is specefied
	 * by gravity.
	 */
	public Gravity2(Vec2 gravity) {
		g = gravity;
	}

	/**
	@param	x	The position of the RigidBody at time t.
			v	The velocity of the RigidBody at time t.
			o	The orientation of the RigidBody at time t. (Given in radians)
			w	The angular velocity of the RigidBody at time t. (Given in radians/s)
	*/
	public void prepare(double t, State s, RigidBody b) {
		mg = g.mul( b.getMass() );
		_at = s.getPosition();
	}
	
	/**
	Returns the force acting on the body at time t.
	*/
	public Vec2 get() {
		return mg;
	}
	
	/**
	Returns the point on wich the force acts.
	Gravity always acts at the center of mass.
	*/
	public Vec2 at() {
		return _at;
	}
	
	public boolean dispell() {
		return false;
	}
	
	/**
	Returns the gravity constant of this gravity force.
	*/
	protected Vec2 getGravity() {
		return g;
	}

}


