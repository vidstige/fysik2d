import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import fysik2d.*;

public class Demo extends Applet implements AdjustmentListener {
	
	private SimulationView simview;
	
	public void init() {
		
		Simulation sim = new Simulation();
		
		RigidBody a = new RigidRectangle(
			new Vec2(0.0,-2.5), 0.0, Double.POSITIVE_INFINITY, 8.0, 0.5);
		
		RigidBody b = new RigidRectangle(
			new Vec2(1.6,0.9),Math.PI/6,new Vec2(-0.05, -0.5),0.0,0.5,0.5, 0.5);
		
       RigidBody c = new RigidRectangle(
        new Vec2(-1.6,0.9),-Math.PI/8,new Vec2(0.05, -0.5),0.0,0.5,0.5, 0.5);
       
		Force2 gravity = new Gravity2(new Vec2(0, -1));
		b.addForce(gravity);
		c.addForce(gravity);
		
		sim.add(a);
		sim.add(b);
		sim.add(c);
		
		setLayout(new BorderLayout());
		
		simview = new SimulationView(sim, 1.0);
		add(simview, BorderLayout.NORTH);
		
				
		Scrollbar myScrollbar =
			new Scrollbar(Scrollbar.HORIZONTAL, 100, 10, -100, 500);
		
		myScrollbar.addAdjustmentListener(this);
		add(myScrollbar, BorderLayout.SOUTH);
		
	}
	
	public void adjustmentValueChanged(AdjustmentEvent ae) {
		double relativeTime = ((double)ae.getValue()) / 100.0;
		simview.setSpeed(relativeTime);
	}
	
	public void start() {
		simview.start();
	}
	
}
