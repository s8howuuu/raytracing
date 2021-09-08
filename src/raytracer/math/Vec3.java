package raytracer.math;

public final class Vec3 extends Vec4<Vec3, Vec3> {

	/**
	 * Nullvektor [0, 0, 0].
	 */
	public static final Vec3 ZERO = new Vec3(0.0f, 0.0f, 0.0f);

	public static final Vec3 INF = new Vec3(Float.POSITIVE_INFINITY,
			Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

	/**
	 * Einheitsvektor in X-Richtung [1, 0, 0].
	 */
	public static final Vec3 /* normalized */X = new Vec3(1.0f, 0.0f, 0.0f);

	/**
	 * Einheitsvektor in Y-Richtung [0, 1, 0].
	 */
	public static final Vec3 /* normalized */Y = new Vec3(0.0f, 1.0f, 0.0f);

	/**
	 * Einheitsvektor in Z-Richtung [0, 0, 1].
	 */
	public static final Vec3 /* normalized */Z = new Vec3(0.0f, 0.0f, 1.0f);

	protected final float sdot;

	/**
	 * Erzeugt einen dreidimensionalen Vektor.
	 *
	 * @param x
	 *            X-Verschiebung
	 * @param y
	 *            Y-Verschiebung
	 * @param z
	 *            Z-Verschiebung
	 */
	public Vec3(final float x, final float y, final float z) {
		super(x, y, z, 0);
		this.sdot = x * x + y * y + z * z;
	}

	@Override
	protected Vec3 create(final float x, final float y, final float z, final float w) {
		return new Vec3(x, y, z);
	}

	/**
	 * Berechnet einen Vektor, der senkrecht zu beiden Vektoren ist. Die
	 * Vektoren dürfen nicht linear abhängig sein.
	 *
	 * @param b
	 *            Der zweite Vektor
	 * @return Vektor, der senkrekt zu beiden Vektoren ist.
	 */
	public final Vec3 cross(final Vec3 b) {
		final float nx = y * b.z - z * b.y;
		final float ny = z * b.x - x * b.z;
		final float nz = x * b.y - y * b.x;
		return new Vec3(nx, ny, nz);
	}

	public final float sdot() {
		return sdot;
	}

	/**
	 * Berechnet die Länge des Vektors.
	 *
	 * @return Länge des Vektors.
	 */
	public final float norm() {
		return sdot == 1.0f ? 1.0f : (float) Math.sqrt(sdot);
	}

	/**
	 * Berechnet die Normalisierung (Länge 1) des Vektors.
	 *
	 * @return Normalisierter Vektor
	 */
	public final Vec3 /* normalized */normalized() {
		final float factor = 1.0f / norm();
		return new Vec3(factor * x, factor * y, factor * z);
	}

	/**
	 * Berechnet den Cosinus des Winkels, den die beiden Vektoren einschließen.
	 *
	 * @param a
	 *            Zweiter Vektor
	 * @return Cosinus des eingeschlossenen Winkels
	 */
	public final float angle(final Vec3 a) {
		return dot(a) / (norm() * a.norm());
	}

	/**
	 * Reflektiert den Vektor an einer Oberfläche mit der Normalen normal.
	 *
	 * @param normal
	 *            Reflektionsnormale (Einheitsvektor)
	 * @return Reflektierter Vektor
	 */
	public final Vec3 reflect(final Vec3 /* normalized */normal) {
		return sub(normal.scale(2 * dot(normal)));
	}

	@Override
	public String toString() {
		return String.format("[%f %f %f]", x, y, z);
	}
}
