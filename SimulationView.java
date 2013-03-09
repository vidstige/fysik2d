import java.awt.*;
import java.awt.event.*;
import fysik2d.*;

class SimulationView extends Canvas
implements Runnable, MouseMotionListener, MouseListener {
	
	private Image doubleBuffer;
	private Graphics backpage;
	
	private Dimension pSize;
	private Simulation sim;
	
	private Thread thread;

	private final int DESIRED_FPS = 25;
	private final int DESIRED_DELAY = 1000 / DESIRED_FPS;
	
	private final double MAX_TIMESTEP = 0.05;
	
	private double timestep;
	private int N;
	
	private double speed;
	//2=doublespeed 1=realtime 0.5=slowmotion -1=backwards
	
	private MouseForce mf;
	
	private Integrator integrator;

	private LinearDamper ld;
	private AngularDamper ad;
	
	public SimulationView(Simulation sim, double relativeTime) {
		super();
		
		this.sim = sim;
		setSpeed(relativeTime);
		pSize = new Dimension(640,480);
		
		integrator = new RungeKutta4();
		
		
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	
	public Dimension getPreferredSize() {
		return pSize;
	}
	
	public void mouseDragged(MouseEvent e) {
		if (mf != null) {
			Vec2 p = new Vec2(
				((double)(e.getX()-320))/30.0,
				((double)(240-e.getY()))/30.0);
		
			mf.setMousePosition(p);
		}
	}
	
	public void mouseMoved(MouseEvent e) {
	}

	// MouseListener interface..
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	private boolean isSet(int bitfield, int bitmask) {
		return (bitfield & bitmask) == bitmask;
	}
	
	public void mousePressed(MouseEvent e) {
		if (isSet(e.getModifiers(), MouseEvent.BUTTON1_MASK)) {
			Vec2 p = new Vec2(
				((double)(e.getX()-320))/30.0,
				((double)(240-e.getY()))/30.0);
		
		
			RigidBody body = sim.getBody(p);
			if (body != null) {
				mf = new MouseForce(p, body);
				body.addForce(mf);
			}
		}
		
		if (isSet(e.getModifiers(), MouseEvent.BUTTON3_MASK)) {
			Vec2 p = new Vec2(
				((double)(e.getX()-320))/30.0,
				((double)(240-e.getY()))/30.0);

			RigidBody body = sim.getBody(p);
			if (ad == null || ld == null) {
				if (body != null) {
					ad = new AngularDamper(0.5);
					ld = new LinearDamper(0.5);
				
					body.addForce(ad);
					body.addForce(ld);
				}
			} else {
				ld.remove();
				ad.remove();
				ld = null;
				ad = null;
			}
			
		}
		
	}
	
	public void mouseReleased(MouseEvent e) {
		if (isSet(e.getModifiers(), MouseEvent.BUTTON1_MASK)) {
			if (mf != null) {
				mf.mouseUp();
				mf = null;
			}
		}
		
		if (isSet(e.getModifiers(), MouseEvent.BUTTON3_MASK)) {
		}

	}


	public void update(Graphics page) {
		if (backpage == null) {
			doubleBuffer = createImage(getWidth(), getHeight());
			backpage = doubleBuffer.getGraphics();
		}
		backpage.setColor(Color.white);
		backpage.fillRect(0,0, getWidth(), getHeight());
		
		paint(backpage);

		page.drawImage(doubleBuffer, 0,0, this);
		
	}

	public void paint(Graphics page) {
		for (int i=0; i<sim.numBodies(); i++) {
			RigidBody b = sim.getBody(i);
			b.paint(page);
		}
	}
	
	public void stop() {
		thread = null;
	}
	
	public void start() {
		thread = new Thread(this, "fysiktråd");
		thread.start();
	}
	
	public double setSpeed(double new_speed) {
		double old_speed = speed;
		speed = new_speed;
		
		timestep = (1.0 / DESIRED_FPS) * speed;
		N = 1;
		if (timestep > MAX_TIMESTEP) {
			N = (int)(timestep / MAX_TIMESTEP);
			timestep = timestep / N;
		}
		
		return old_speed;
	}
	
	public void run() {
		long tid1;
		long tid2;
		long tidkvar;
		long tidsSkillnad;
		
		double lastRepaint = sim.getTime();
		
		boolean skip = false;
		
		while (thread != null) {
			tid1 = System.currentTimeMillis();
			
			// update physics
			for (int i=0; i<N; i++)
				sim.step(timestep, integrator);
				
			// only paint one frame every now and then..
			//if (sim.getTime() - lastRepaint > 0.25) {
				// request repaint within 5ms
				if (!skip)
					repaint(5);

				lastRepaint = sim.getTime();
			//}
			
			// sleep the rest of this frame
			tid2 = System.currentTimeMillis();
			tidsSkillnad = tid2-tid1;
			if (tidsSkillnad < DESIRED_DELAY) {
				try {
					thread.sleep(DESIRED_DELAY - tidsSkillnad);
				} catch (InterruptedException ie) {
				}
				skip = false;
			} else {
				System.err.println("Lag detected. Skipping next frame.");
				skip = true;
			}

		}
	}
	
	public double getTimestep() {
		return timestep;
	}
	
}
