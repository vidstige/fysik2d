package fysik2d;

import java.awt.Graphics;

public class LineSegment {

	protected Vec2 a;
	protected Vec2 b;
			
	/**
	
	*/
	// TODO: remove inside argument.
	public LineSegment(Vec2 p1, Vec2 p2) {
		a = p1;
		b = p2;
		if (a.equals(b))
			System.err.println("Illegal line! p1 == p2");
	}
	
	public LineSegment worldTransform(double o, Vec2 pos) {
		return worldTransform(Matrix2x2.makeRotate(o), pos);
	}
	
	public LineSegment worldTransform(Matrix2x2 rm, Vec2 pos) {
		Vec2 na = (rm.mul(a)).add(pos);
		Vec2 nb = (rm.mul(b)).add(pos);
		return (new LineSegment(na, nb));
	}
	
	/**
	Computes the closest point on this linesegment to the point pt.
	
	This can be used to fetch the hook-point of a collisionforce.
	The other hook-point will be a corner.
	*/
	public Vec2 pointOn(Vec2 pt) {
		
		Vec2 k = b.sub(a);

		//double u = k.dot(pt.sub(a)) / k.length2();
		
		double u =
			(
			(pt.getX()-a.getX())*(b.getX()-a.getX()) +
			(pt.getY()-a.getY())*(b.getY()-a.getY())
			) / k.length2();
		
		// clip u to 0..1
		if (u < 0) u = 0;
		if (u > 1) u = 1;
		
		return a.add( k.mul(u) );
	}

	/** Returns intersection of the two linesegments or null
	if they dont intersect. */
	// TODO: will this method ever be used!!?
	public Vec2 intersects(LineSegment ls) {
		
		double x1 = a.getX();
		double y1 = a.getY();
		double x2 = b.getX();
		double y2 = b.getY();
		double x3 = ls.a.getX();
		double y3 = ls.a.getY();
		double x4 = ls.b.getX();
		double y4 = ls.b.getY();
		
		double ua_upp = (x4-x3)*(y1-y3) - (x4-y3)*(x1-x3);
		double ub_upp = (x2-x1)*(y1-y3) - (y2-y1)*(x1-x3);
		double ner = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
		
		if (ner == 0) // linesegments parallell..
			return null;
		
		double ua = ua_upp / ner;
		double ub = ub_upp / ner;
		
		if (ua < 0 || ua > 1) // outside linesegment.
			return null;
		
		if (ub < 0 || ub > 1) // outside linesegment.
			return null;
			
		System.out.println("au, ub: " + ua +", "+ ub);
		
		// return intersection point, usng ua. (ub could be used too)
		return new Vec2(x1 + ua*(x2-x1), y1 + ua*(y2-y1));
	}
	
	public Vec2 getPoint1() { return a; }
	public Vec2 getPoint2() { return b; }
	
	public double length() {
		return (b.sub(a)).length();
	}
	
	
}
