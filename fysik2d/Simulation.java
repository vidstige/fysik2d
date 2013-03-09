package fysik2d;

public class Simulation {

	private RigidBody[] bodies;
	private int numbodies;
	
	private double t;
	
	public Simulation() {
		bodies = new RigidBody[10];
		numbodies = 0;
		
		t = 0.0;
	}
	
	public void beginCapture() {
	}
	
	public Replay endCapture() {
		return null;
	}
	
	public void add(RigidBody rb) {
		bodies[numbodies] = rb;
		numbodies++;
	}
	
	public void step(double dt, Integrator integrator) {
		// step all bodies forward
		for (int i=0; i<numbodies; i++)
			bodies[i].step(t, dt, integrator);


		// removes all forces that are not relevant anymore.
		for (int i=0; i<numbodies; i++)
			bodies[i].deleteForces();

		
		// check for collisions and add penalty forces..
		for (int i=0; i<numbodies; i++)
			for (int j=i+1; j<numbodies; j++)
				bodies[i].handleCollision( bodies[j] );
		
		t += dt;
	}
	
	/**
	 * Fetches the body at point p. If there is no object at p, null is
	 * returned.
	 * @param	p	A point.
	 * @return The body at p, or null if none is found.
	 */
	public RigidBody getBody(Vec2 p) {
		for (int i=0; i<numbodies; i++) {
			if (bodies[i].contains(p))
				return bodies[i];
		}
		
		return null;
	}
	
	public RigidBody getBody(int index) {
		return bodies[index];
	}

	public int numBodies() {
		return numbodies;
	}
	
	public double getTime() {
		return t;
	}

	public double getEnergy() {
		double E = 0;
		
		for (int i=0; i<numbodies; i++) {
			E +=
				bodies[i].getKineticEnergy() +
				bodies[i].getRotationEnergy();

		}
		return E;
	}
}
