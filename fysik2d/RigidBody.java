package fysik2d;

import java.awt.Graphics;
import java.awt.Color;

/**
Represents a rigid body in R^2. This is a very central class in this
physics package.
<p>
It holds information about the position, veclotity, orientation,
angular velocity, mass, moment of inertia and the shape of the
body.
<p>
The shape of the body is determinded by a polygon.
<p>
Collisions between bodies is simulated by a spring-model.
<p>
This class uses a Runge-kutta 4 integrator to compromise accuracy with
speed.
*/
public class RigidBody implements DiffEquation {
	
	/* all linesegments of this body */
	private LineSegment[] lines;
	private int numlines;
	
	private Vec2[] points;
	private int npts;

	/* all forces acting on this body */	
	private Force2[] forces;
	private int numforces;
	
	private double m; // mass 
	private double I; // moment of inertia

	private State state;
	
	private double maxradius;
		
	private double threshold;
	public static final double DEFAULT_THRESHOLD = 0.25;

	/**
	 *
	 */
	public RigidBody(Vec2 position, double theta, double mass) {
		this(position, theta, Vec2.NULL, 0.0, mass);
	}

	public RigidBody(Vec2 position, double theta, Vec2 velocity, double av, double mass) {
	
		points = new Vec2[10];
		npts = 0;
		
		forces = new Force2[4];
		numforces = 0;
		
		state = new State(position, velocity, theta, av);
		
		m = mass;
		I = 1.0;
		
		
		maxradius = 0;
		
		threshold = DEFAULT_THRESHOLD;
	}
	
	/**
	 * Adds a point to this rigidbody. The points descibes the outline of
	 * an #RigidBody. The points must be added in either clockwise or
	 * counter-clockwise order. When all points are added you <i>must</i>
	 * call #closePolygon
	 *
	 * @param	p	The local coordinate of a corner.
	 * @return	This rigid body
	 * @see		#closePolygon
	 */
	public RigidBody addPoint(Vec2 p) {
		if (npts < points.length) {
			points[npts] = p;
			npts++;
		} else {
			System.err.println("Too many points in rigid body.");
			return null;
		}
		
		return this;
	}
	
	/**
	 * 
	 */
	public void closePolygon() {
		// compute center of mass. Assume even distributed mass...
		Vec2 sum = Vec2.NULL;
		for (int i=0; i<npts; i++) {
			sum = sum.add( points[i] );
		}
		Vec2 cm = sum.div(npts);
		
		// fix the center of mass if it is malplaced.
		// TODO: add a little tolerance, epsilon.
		if (!cm.equals(Vec2.NULL)) {
			System.err.println("Center of Mass malplaced. Fixing...");
			System.err.println("CM = " + cm + " should be: (0, 0)");
			for (int i=0; i<npts; i++)
				points[i] = points[i].sub(cm);
		}
		
		// create a vector of lines, instead of points.
		lines = new LineSegment[npts];
		numlines = npts;
		
		double maxlen = 0;
		for (int i=0; i<numlines-1; i++) {
			lines[i] = new LineSegment(points[i], points[i+1]);
			double len = lines[i].length();
			if (len > maxlen)
				maxlen = len;
		}
		lines[numlines-1] =	new LineSegment(points[npts-1], points[0]);
		
		//threshold = maxlen / 5;
		
		// compute maxradius, used in broad phase of collision detection.
		maxradius = 0;
		for (int i=0; i<npts; i++) {
			double r = points[i].length();
			if (r > maxradius) maxradius = r;
		}
		maxradius += threshold;
		
		// compute moment of inertia
		I = 0;
		for (int i=0; i<npts; i++)
			I += points[i].length2();

		I = I*m/(npts+1); // one point in the middle.

	}
	
	/**
	 * Adds a force to this body.
	 * @param	force	The force to be added.
	 * @return	This rigid body.
	 * @see		Gravity2
	 */
	public RigidBody addForce(Force2 force) {
		if (numforces < forces.length) {
			forces[numforces] = force;
			numforces++;
		} else {
			//System.err.println("Too many forces acting on rigid body");
			return null;
		}
		
		return this;
	}
	
	public RigidBody removeForce(Force2 force) {
		int p = -1;
		int i = 0;
		while (i<numforces && p<0) {
			if (forces[i] == force) p = i;
			i++;
		}

		if (p<0) return null; // force not found...
		if (numforces < 1)
			return null; // nothing can be removed (shouldn't happen)
		
		numforces--;
		forces[p] = forces[numforces];
		forces[numforces] = null;
		
		return this;
	}
	
	
	public State diffEq(double t, State _state) {
		Vec2 Fsum = Vec2.NULL;
		double Tsum = 0;
		
		for (int i = 0; i<numforces; i++) {
			// retrive direction, size and point of interaction for force #i.
			forces[i].prepare(t, state, this);
			Vec2 F  = forces[i].get();
			Vec2 at = forces[i].at();
			
			Vec2 Fcm, FM;
			
			// a vector from at to _x..
			Vec2 r = (_state.getPosition()).sub(at);
			
			if (at.equals(_state.getPosition())) {
				// if the force acts at the center of mass.
				Fcm = F;
				FM = Vec2.NULL;
			} else {
				Vec2 e = r.normal();
				
				// project force onto the r-vector to get the force acting at
				// the center of mass.
				Fcm = e.mul( e.dot(F) );
				
				// subtract to get the other composant, the one that contributes
				// to the moment.
				FM = F.sub(Fcm);
			}
			
			// add the force acting at center of mass to total sum..
			Fsum = Fsum.add( Fcm );
			
			
			// compute the torque.
			double torque = FM.cross(r);
			
			Tsum = Tsum + torque;
			
		}
		
		
		return new State(
			_state.getVelocity(),
			Fsum.div(m),
			_state.getAngularVelocity(),
			Tsum / I);
	}
	
	
	/**
	Takes one step in the simulation time. This function uses the RK4
	integrator.
	@param	t	The current simulated time.
	@param	dt	Specefies the timestep that will be used in this step.
				Smaller timestep produces more accurate results, but
				will slow down your application.
				Negative values are allowed wich means the body takes a
				step <i>back</i> in time.
	*/
	public void step(double t, double dt, Integrator integrator) {
		state = integrator.step(t, dt, state, this);
	}

	/**
	 * This method loops though all forces acting on this body and
	 * removes any that wants to be removed.
	 */
	public void deleteForces() {

		int i=0;
		while (i<numforces) {
		
			if (forces[i].dispell())
				removeForce(forces[i]);
			else
				i++;
		}
	}
	
	/**
	 * Checks if the two bodies are about to collide, and if so, two
	 * forces are inserted, one cornerforce and one edgeforce.  Those
	 * two forces act only in a short time and forces the bodies apart.
	 *
	 * @see		CornerForce
	 * @see		EdgeForce
	 */
	public void handleCollision(RigidBody b) {
		// do circle-circle detection
		double distance2  = (getPosition().sub(b.getPosition())).length2();
		double maxradiussum2 = (b.maxradius+maxradius)*(b.maxradius+maxradius);
		if (distance2 < maxradiussum2) {
			// circles intersected..
			double thr2 = (threshold+b.threshold)*(threshold+b.threshold);
			this.checkCorners(b, thr2);
			b.checkCorners(this, thr2);
		}
	}
	
	private void checkCorners(RigidBody b, double threshold2) {
		
		for (int i=0; i<npts; i++) {
			// get local coordinate for corner.
			Vec2 lc = points[i];
			// get world coordinate for this corner.
			Matrix2x2 rot = getRotation();
			Vec2 wc = (rot.mul( lc )).add(getPosition());
			
			// check all edges of body b.
			for (int j=0; j<b.numlines; j++) {
				// transform line #j to worldspace
				LineSegment world_line =
					(b.lines[j]).worldTransform(b.getRotation(),
					b.getPosition());
				
				Vec2 online = world_line.pointOn( wc );
				double dist2 = (wc.sub(online)).length2();

				if ( dist2 < threshold2 ) {
					// add a cornerforce and a sideforce.
					// this body has the corner
					// body b has the edge..
					
					//System.out.println("krock!");
					
					addForce(new CornerForce(b.lines[j],lc,b,threshold2));
					b.addForce(new EdgeForce(b.lines[j],lc,this,threshold2));
				}
			
			}
		
		}
		
	}
	
	/**
	 * Draws this body...
	 */
	public void paint(Graphics page) {
		
		drawFilled(page);
		drawForces(page);
		
		//drawOutline(page);
	}
	
	public void drawFilled(Graphics page) {
		page.setColor(Color.black);
		int[] sx = new int[npts];
		int[] sy = new int[npts];
		for (int i=0; i<npts; i++) {
			Vec2 p = (getRotation().mul(points[i])).add(getPosition());
			sx[i] = 320+(int)(p.getX()*30);
			sy[i] = 240-(int)(p.getY()*30);
		}
		page.fillPolygon(sx,sy, npts);
	}
	
	public void drawForces(Graphics page) {
		page.setColor(Color.red);
		for (int i=0; i<numforces; i++) {
			forces[i].prepare(0.0, state, this);
			Vec2 F  = forces[i].get();
			Vec2 at = forces[i].at();
			
			page.drawLine(
				320+(int)(at.getX()*30),
				240-(int)(at.getY()*30),
				320+(int)(at.getX()*30 + F.getX()*3),
				240-(int)(at.getY()*30 + F.getY()*3));
				
			Vec2 p1 = F.cross(-0.4);
			Vec2 p2 = F.cross( 0.4);

			page.drawLine(
				320+(int)(at.getX()*30 + F.getX()*3),
				240-(int)(at.getY()*30 + F.getY()*3),
				320+(int)(at.getX()*30 + F.getX()*2.5 + p1.getX()),
				240-(int)(at.getY()*30 + F.getY()*2.5 + p1.getY()));
			page.drawLine(
				320+(int)(at.getX()*30 + F.getX()*3),
				240-(int)(at.getY()*30 + F.getY()*3),
				320+(int)(at.getX()*30 + F.getX()*2.5 + p2.getX()),
				240-(int)(at.getY()*30 + F.getY()*2.5 + p2.getY()));

						
		}
	}
	
	public void drawOutline(Graphics page) {
		page.setColor(Color.black);
		int[] sx = new int[npts];
		int[] sy = new int[npts];
		for (int i=0; i<npts; i++) {
			Vec2 p = (getRotation().mul(points[i])).add(getPosition());
			sx[i] = 320+(int)(p.getX()*30);
			sy[i] = 240-(int)(p.getY()*30);
		}
		page.drawPolygon(sx,sy, npts);
		
	}
	
	/**
	 * Checks if a points lies within the bounding polygon of
	 * this rigid body.
	 * 
	 * The algoritm is developed by some Randolph Franklin.
	 * @return	True if the point p is inside this #RigidBody.
	 */
	public boolean contains(Vec2 p) {
		double tx = p.getX();
		double ty = p.getY();
		
		Matrix2x2 r = getRotation();
				
		boolean c = false;
		int i = 0;
		int j = npts-1;
		while (i<npts) {
			Vec2 pi = (r.mul(points[i])).add(getPosition());
			Vec2 pj = (r.mul(points[j])).add(getPosition());
			if (
				(((pi.getY() <= ty) && (ty < pj.getY())) ||
				((pj.getY() <= ty) && (ty < pi.getY()))) &&
				(tx < (pj.getX() - pi.getX()) *
				(ty - pi.getY()) /
				(pj.getY() - pi.getY()) + pi.getX()))
			{
				c = !c;
			}
			
			j = i;
			i++;
		}
		
		return c;
	}
	
	/**
	 * Returns the position of this #RigidBody.
	 */
	public Vec2 getPosition() { return state.getPosition(); }
	
	/**
	 *
	 */
	public Vec2 getVelocity() { return state.getVelocity(); }
	
	/**
	 *
	 */
	public double getOrientation() { return state.getOrientation(); }
			
	/**
	 *
	 */
	public double getAngularVelocity() { return state.getAngularVelocity(); }
	
	/**
	 *
	 */
	public double getMass() { return m; }
	
	/**
	 *
	 */
	public Matrix2x2 getRotation() {
		return Matrix2x2.makeRotate(state.getOrientation());
	}
	
	
	/**
	 *
	 */
	public double getMomentOfInertia() { return I; }
	
	/**
	 *
	 */
	public double getMomentum() {
		return (state.getVelocity()).length()*m;
	}
	
	
	/**
	 * 
	 */
	public double getKineticEnergy() {
		return m*(state.getVelocity()).length2() / 2.0;
	}

	/**
	 * 
	 */
	public double getRotationEnergy() {
		double w = state.getAngularVelocity();
		return I*w*w / 2.0;
	}


}
