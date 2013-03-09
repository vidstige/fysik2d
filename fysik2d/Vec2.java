package fysik2d;
/***********************************\
*                                   *
* av Samuel Carlsson                *
*                                   *
\***********************************/


/**
This class represents a vector in R^2. A vector can be used to represent a lot
of things, position, forces, velocities.
<p>
This class is mutable and can therefor be shared among other objects.
<p>
All methods returns a reference to a new vector with the computed value if
not otherwise stated.
*/
public class Vec2 {

	// holds the x and y values for this vector.
	private double x,y;

	/**
	 * This is the null-vector. It's value is (0, 0).
	 */
	public static final Vec2 NULL = new Vec2(0.0, 0.0);
	

	/**
	This constructor creates the null-vector, that is this == (0, 0).
	*/	
	public Vec2() {
		x = y = 0;
	}
	
	/**
	Creates a new copy of the vector v, that is this == v.
	*/
	protected Vec2(Vec2 v) {
		x = v.x;
		y = v.y;
	}
	
	/**
	Contructs a new vector with the cartesian coordinates nx and ny.
	*/
	public Vec2(double nx, double ny) {
		x=nx; y=ny;
	}

	/**
	 * Adds the vector v with this vector.
	 *	@param	v	The other vector.
	 *	@return		The sum of the two vectors.
	 */
	public Vec2 add(Vec2 v) {
		return new Vec2(x+v.x, y+v.y);
	}
	

	/**
	 *
	 */
	public Vec2 add(double vx, double vy) {
		return new Vec2(x+vx, y+vy);
	}
	
	/**
	Subtracts the vector v from this vector using normal vector subtraction.
	@param	v	The other vector.
	@return		The diffrence between this vector and v.
	*/
	public Vec2 sub(Vec2 v) {
		return new Vec2(x-v.x, y-v.y);
	}
	
	/**
	Multiplies this vector by the scalar s. The resulting vector has the
	same direction but the magnitude will be s times as big.
	@param	s	The scalar to multiply this vector with.
	@return		A new vector with the value of this vector times s.
	*/
	public Vec2 mul(double s) {
		return new Vec2(x*s, y*s);
	}
	/**
	Divides this vector with the scalar s. This operator is usually not defined
	on vectors, but will often come in handy. This is just a quicker way to
	say a.mul(1/s).
	The resulting vector will have the same direction as this vector.
	@param	s	The scalar to divide this vector with.
	@return		A new vector that is s times shorter than this vector.
	*/
	public Vec2 div(double s) {
		return new Vec2(x/s, y/s);
	}
	
	/**
	Computes the &quot;dot&quot; protuct between this vector and v. The dot
	product is defined as (v1,v2) dot (u1,u2) = v1*u1 + v2*u2
	@param	v	The other vector.
	@return		The dot-product between the two vectors.
	*/
	public double dot(Vec2 v) {
		return (x*v.x) + (y*v.y);
	}
	
	/**
	Computes the orthonogal vector to this vector. There are obviously two
	orthonogal vectors to any vector and the side parameter decides wich one
	of those two will be returned. <b>NOTE:</b> this name will probably
   change.
	
	Currently the return value is not welldefined but it returns a orthonogal
	vector, the value of the parameter side has to be tested.
	@param	side	Wich of the two orthonogal vectors will be returned.
	@return		A orthonogal vector to this vector.
	*/
	public Vec2 cross(boolean side) {
		if (side)
			return new Vec2(y, -x);
		else
			return new Vec2(-y, x);
	}

	/**
	Computes the orthonogal vector to this vector times a scalar.
	<b>NOTE:</b> this name will probably change.

	@param	mult	How much longer the normal will be.
	@return		A orthonogal vector to this vector.
	*/
	public Vec2 cross(double mult) {
		return new Vec2(mult*y, -mult*x);
	}
	
	public double cross(Vec2 v) {
		return y*v.x - x*v.y;
	}

	/**
	 * Computes the squared length of this vector. The squared lenth doesn't
	 * need to evaluate a square-root so it is a bit faster than length().
	 * @return		The squared length of this vector.
	 */	
	public double length2() {
		return (x*x + y*y);
	}
	
	/**
	 * Computes the length of this vector. The computations involve a
	 * square-root wich should be avoided when possible. In lots of
	 * formulas the squared length is used.
	 * @see		#length2
	 * @return		The length of this vector.
	 */
	public double length() {
		return Math.sqrt(length2());
	}
	
	/**
	 * Returns the vector with same direction as this vector but with unit(1)
	 * length.
	 * If the length is already known for some reason, like you have already
	 * called length on this vector, then a division is faster.
	 * @see #div
	 * @return	The normal vector of this vector.
	 */
	public Vec2 normal() {
		double l = length();
		if (l == 0) throw new RuntimeException("Division by zero");
		return this.div(l);
	}

	/**
	 * Returns a vector with same length as this vector but <i>opposite</i>
	 * direction.
	 * @return	The reverse of this.
	 */
	public Vec2 reverse() {
		return new Vec2(-x, -y);
	}
	
	/**
	 *
	 */
	public boolean equals(Vec2 v) {
		return (x==v.x && y==v.y);
	}
	
	/**
	 * Returns a string representation of this vector. The result will look
	 * something like this: "(1.000, 2.000)".
	 */
	public String toString() {
		return ("(" + x + ", " + y + ")");
	}
	
	public double getX() { return x; }
	public double getY() { return y; }
}
