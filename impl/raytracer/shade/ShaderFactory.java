package raytracer.shade;

import raytracer.core.Shader;
import raytracer.math.Color;
import raytracer.math.Constants;

public final class ShaderFactory {
	private ShaderFactory() {}

	/**
	 * Generates a checkerboard structured shader. Two shaders are "placed"
	 * alternately on the whole structure.
	 *
	 * @param a
	 *            The first shader structure (x + y even)
	 * @param b
	 *            The second shader structure (x + y odd)
	 * @param scale
	 *            The size of the tiles
	 * @throws IllegalArgumentException
	 *             If the scale value is not a valid number (must be positive,
	 *             representing a number, not infinity). In addition to that the
	 *             two shaders must be instance values (not null).
	 * @throws UnsupportedOperationException
	 *             If the scale factor is equal to zero (with respect to
	 *             epsilon)
	 */
	public static Shader createCheckerBoard(final Shader a, final Shader b, final float scale){
		// TODO Implement this method
		boolean as = !Constants.isZero(scale)&&!(scale>Constants.EPS);
		if(as||scale==(Float.POSITIVE_INFINITY)) {
			throw new IllegalArgumentException();
		}
		if(a==null||b==null){
			throw new IllegalArgumentException();
		}
		if(Constants.isZero(scale)) {
			throw new UnsupportedOperationException();
		}
		return new CheckerBoard(a,b,scale);
	}

	/**
	 * Generates a Phong (http://en.wikipedia.org/wiki/Phong_shading) shader.
	 *
	 * @param inner
	 *            The base shader of this Phong shader
	 * @param ambient
	 *            Color of the ambient light emitted to the scene
	 * @param diffuse
	 *            The ratio of reflection of the diffuse term of incoming light
	 * @param specular
	 *            The ratio of reflection of the specular term of incoming light
	 * @param shininess
	 *            A shininess constant defining this material. The larger the
	 *            value the more "mirror-like" is the structure
	 * @throws IllegalArgumentException
	 *             If the diffuse, specular or shininess parts are not valid
	 *             numbers (must be positive, representing a number, not
	 *             infinity). In addition to that the shader and the ambient
	 *             color must be instance values (not null).
	 */
	public static Shader createPhong(final Shader inner, final Color ambient, final float diffuse, final float specular, final float shininess) {
		// TODO Implement this method
		boolean ad = !Constants.isZero(diffuse)&&!(diffuse>Constants.EPS);
		boolean asp = !Constants.isZero(specular)&&!(specular>Constants.EPS);
		boolean ash = !Constants.isZero(shininess)&&!(shininess>Constants.EPS);
		if(ad||diffuse==(Float.POSITIVE_INFINITY)) {
			throw new IllegalArgumentException();
		}
		if(asp||specular==(Float.POSITIVE_INFINITY)) {
			throw new IllegalArgumentException();
		}
		if(ash||shininess==(Float.POSITIVE_INFINITY)) {
			throw new IllegalArgumentException();
		}
		
		if(ambient==null) {
			throw new IllegalArgumentException();
		}
		if(inner==null) {
			throw new IllegalArgumentException();
		}
		return new Phong(inner, ambient, diffuse, specular, shininess);
	}
}
