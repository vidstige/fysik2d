package fysik2d;

/**
This class represents a matrix. That is a mathematical entity often used in
both computer graphics and fysics. This class can hold matrices with floating
point values.

You should have some knowledge in linear algebra before using this class
efficently.
*/
public class Matrix2x2 {
	/**
	This is the identity matrix, I.
	*/
	public final static Matrix2x2 IDENTITY = new Matrix2x2();
	
	/**
	Holds the row vectors that builds up this matrix. This storage model
	is used because is simplifies some computations.
	*/
	protected Vec2[] rows;
	
	/**
	Creates the identity matsrix.
	*/
	public Matrix2x2() {
		rows = new Vec2[2];
		rows[0] = new Vec2(1,0);
		rows[1] = new Vec2(0,1);
	}
	
	/**
	Creates a matrix with <i>rowvectors</i> a and b.
	*/
	public Matrix2x2(Vec2 a, Vec2 b) {
		rows = new Vec2[2];
		rows[0] = a;
		rows[1] = b;
	}
	
	/**
	Creates a rotation matrix with theta radians. Matrix returned by
	this method rotates around the orogin. To rotate around another
	point, first move the point with a vector addision.
	*/
	static public Matrix2x2 makeRotate(double theta) {
		double cos = Math.cos(theta), sin = Math.sin(theta);
				
		Vec2 a = new Vec2(cos, sin);
		Vec2 b = new Vec2(-sin, cos);

		return new Matrix2x2(a, b);
	}
	
	/**
	Creates a scale matrix with s.getX() in x direction and s.getY() in y.
	*/
	static public Matrix2x2 makeScale(Vec2 s) {
		return makeScale(s.getX(), s.getY());
	}

	/**
	Creates a scale matrix with sxin x direction and sy in y.
	*/
	static public Matrix2x2 makeScale(double sx, double sy) {
		return new Matrix2x2(
			new Vec2(sx, 0),
			new Vec2(0, sy));
	}
	
	public Matrix2x2 add(Matrix2x2 m) {
		return new Matrix2x2(
			rows[0].add(m.rows[0]),
			rows[1].add(m.rows[1])
		);
	}

	public Matrix2x2 mul(double s) {
		return new Matrix2x2(
			rows[0].mul(s),
			rows[1].mul(s)
		);
	}
	
	public Vec2 mul(Vec2 v) {
		return new Vec2(rows[0].dot(v), rows[1].dot(v));
	}
	
	/**
	Multiplies this matrix with the matrix m, using normal matrix
	multiplication.
	*/
	public Matrix2x2 mul(Matrix2x2 m) {
		Vec2 vv1 = new Vec2(m.rows[0].getX(), m.rows[1].getX());
		Vec2 vv2 = new Vec2(m.rows[0].getY(), m.rows[1].getY());

		Vec2 hv1 = rows[0];
		Vec2 hv2 = rows[1];

		return new Matrix2x2(
			new Vec2(hv1.dot(vv1), hv1.dot(vv2)),
			new Vec2(hv2.dot(vv1), hv2.dot(vv2))
		);
	}

	/**
	Computes the determinant of this matrix and returns it.
	*/
	public double determinant() {
		return
			rows[0].getX() - rows[0].getY() -
			rows[1].getX() + rows[1].getY();
	}
	
	/**
	Supposed to compute the inverse of this matrix. <b>NOTE</b> Not implemented
	yet! Always returns null.
	
	If you need a invers matrix of a rotationmatrix created with
	makeRotate(theta) use makeRotate(-theta) to get the inverse.
	*/
	public Matrix2x2 inverse() {
		return null;
	}
	
	/**
	Supposed to compute the transpose of this matrix. That is, all the columns
	has been rows.
	
	<b>NOTE</b> Not implemented yet! Always returns null
	*/
	public Matrix2x2 transpose() {
		return null;
	}
	
	public String toString() {
		return
			rows[0].toString() +	"\n" +
			rows[1].toString();
	}
	
}
