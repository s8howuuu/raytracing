package raytracer.core;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

import raytracer.core.def.Accelerator;
import raytracer.core.def.StandardObj;
import raytracer.math.Point;
import raytracer.geom.*;
import raytracer.math.Vec3;

/**
 * Represents a model file reader for the OBJ format
 */
public class OBJReader {

	/**
	 * Reads an OBJ file and uses the given shader for all triangles. While
	 * loading the triangles they are inserted into the given acceleration
	 * structure accelerator.
	 *
	 * @param filename
	 *            The file to read the data from
	 * @param accelerator
	 *            The target acceleration structure
	 * @param shader
	 *            The shader which is used by all triangles
	 * @param scale
	 *            The scale factor which is responsible for scaling the model
	 * @param translate
	 *            A vector representing the translation coordinate with which
	 *            all coordinates have to be translated
	 * @throws IllegalArgumentException
	 *             If the filename is null or the empty string, the accelerator
	 *             is null, the shader is null, the translate vector is null,
	 *             the translate vector is not finite or scale does not
	 *             represent a legal (finite) floating point number
	 */
	public static void read(final String filename,
			final Accelerator accelerator, final Shader shader, final float scale,
			final Vec3 translate) throws FileNotFoundException {
		read(new BufferedInputStream(new FileInputStream(filename)), accelerator, shader, scale, translate);
	}


	/**
	 * Reads an OBJ file and uses the given shader for all triangles. While
	 * loading the triangles they are inserted into the given acceleration
	 * structure accelerator.
	 *
	 * @param in
	 *            The InputStream of the data to be read.
	 * @param accelerator
	 *            The target acceleration structure
	 * @param shader
	 *            The shader which is used by all triangles
	 * @param scale
	 *            The scale factor which is responsible for scaling the model
	 * @param translate
	 *            A vector representing the translation coordinate with which
	 *            all coordinates have to be translated
	 * @throws IllegalArgumentException
	 *             If the InputStream is null, the accelerator
	 *             is null, the shader is null, the translate vector is null,
	 *             the translate vector is not finite or scale does not
	 *             represent a legal (finite) floating point number
	 */
	public static void read(final InputStream in,
			final Accelerator accelerator, final Shader shader, final float scale,
			final Vec3 translate) throws FileNotFoundException {
		// TODO Implement this method
		if(in == null) {
			throw new IllegalArgumentException();
		}
		if(accelerator==null) {
			throw new IllegalArgumentException();
		}
		if(shader == null) {
			throw new IllegalArgumentException();
		}
		if(translate == null||!translate.isFinite()) {
			throw new IllegalArgumentException();
		}
		if(scale == Float.POSITIVE_INFINITY||scale == Float.NEGATIVE_INFINITY) {
			throw new IllegalArgumentException();
		}
			Scanner scan = new Scanner(in);
			String regex1 = "v [+-]?(\\d+([.]\\d*)?(e[+-]?\\d+)?|[.]\\d+(e[+-]?\\d+)?) [+-]?(\\d+([.]\\d*)?(e[+-]?\\d+)?|[.]\\d+(e[+-]?\\d+)?) [+-]?(\\d+([.]\\d*)?(e[+-]?\\d+)?|[.]\\d+(e[+-]?\\d+)?)";
			String regex2 = "f [0-9]+ [0-9]+ [0-9]+";
			scan.useLocale(Locale.ENGLISH);
			String tmpStr = null;
			ArrayList<Point> PA = new ArrayList<Point>();
			ArrayList<TmpTria> TA = new ArrayList<TmpTria>();
			while(scan.hasNextLine()) {
			tmpStr = scan.nextLine();
			if(tmpStr.matches(regex1)) {
				String[] mpc = tmpStr.split(" ");
				float x = Float.valueOf(mpc[1]).floatValue();
				float y = Float.valueOf(mpc[2]).floatValue();
				float z = Float.valueOf(mpc[3]).floatValue();
				Point tmpP = new Point(x, y, z);
				tmpP = tmpP.scale(scale).add(translate);
				PA.add(tmpP);
			}
			if(tmpStr.matches(regex2)) {
				String[] mpc = tmpStr.split(" ");
				int x = Integer.valueOf(mpc[1]).intValue();
				int y = Integer.valueOf(mpc[2]).intValue();
				int z = Integer.valueOf(mpc[3]).intValue();
				TmpTria tmpTri = new TmpTria(x,y,z);
				TA.add(tmpTri);
			}
			}
			Iterator<TmpTria> Tai = TA.iterator();
			while(Tai.hasNext()) {
				TmpTria tttmp = Tai.next();
				int i1 = tttmp.getX();
				int i2 = tttmp.getY();
				int i3 = tttmp.getZ();
				Point tmpP1 = PA.get(i1-1);
				Point tmpP2 = PA.get(i2-1);
				Point tmpP3 = PA.get(i3-1);
				Primitive tmpTri = GeomFactory.createTriangle(tmpP1, tmpP2, tmpP3);
				StandardObj sobjTmp = new StandardObj(tmpTri, shader);
				accelerator.add(sobjTmp);
			}
			scan.close();
	}
}
