package raytracer.math;

public final class Point extends Vec4<Point, Vec3> {

	/**
	 * Ein Punkt am Ursprung.
	 */
	public static final Point ORIGIN = new Point(0, 0, 0);

	/**
	 * Erzeugt einen Punkt an den gegebenen Koordinaten.
	 *
	 * @param x X-Position
	 * @param y Y-Position
	 * @param z Z-Position
	 */
	public Point(final float x, final float y, final float z) {
		super(x, y, z, 1.0f);
	}

	@Override
	protected Point create(final float x, final float y, final float z, final float w) {
		return new Point(x, y, z);
	}

	/**
	 * Berechnet einen Vektor von p zu diesem Punkt.
	 *
	 * @param p Startpunkt
	 * @return Vektor von p zu diesem Punkt.
	 */
	public final Vec3 sub(final Point p) {
		return new Vec3(x - p.x, y - p.y, z - p.z);
	}

	@Override
	public String toString() {
		return String.format("[%f %f %f]", x, y, z);
	}
}
