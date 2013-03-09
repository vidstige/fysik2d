package fysik2d;

/**
 * This class represents a force in the 2D world. 
 */

abstract public class Force2 {
	
	/**
	 *Prepares this body to return the get and at values for this time.
	 Must always be called before the get and at methods.
	 */
	abstract public void prepare(double t, State s, RigidBody b);
	
	/**
	 * Returns the size and direction of this force acting at time t..
	 */
	abstract public Vec2 get();
	
	/**
	 * Returns the point on wich this force is acting at time t..
	 * Given in <i>world</i> coordinates.
	 */
	abstract public Vec2 at();
	
	/**
	 * Will be called last in each frame to see if this force wants to be
	 * removed. The default action is to not remove this force.
	 */
	public boolean dispell() {
		return false;
	}
}
