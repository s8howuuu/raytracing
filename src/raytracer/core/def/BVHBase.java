package raytracer.core.def;

import java.util.List;

import raytracer.core.Obj;
import raytracer.math.Pair;
import raytracer.math.Point;
import raytracer.math.Vec3;

/**
 * Represents a base class for bounding volume hierarchies
 */
public abstract class BVHBase extends Accelerator {
	/**
	 * Distribute the objects in sub-BHVs if the current BVH contains more
	 * elements than this number describes
	 */
	public static final int THRESHOLD = 4;

	/**
	 * Returns the inner objects
	 *
	 * @return The inner objects
	 */
	public abstract List<Obj> getObjects();

	/**
	 * Builds the actual bounding volume hierarchy
	 */
	public abstract void buildBVH();

	/**
	 * Calculates the minimum and the maximum points of the box
	 *
	 * @return The computed minimum and maximum of points
	 */
	public abstract Pair<Point, Point> calculateMinMax();

	/**
	 * Calculates the split dimension used for splitting the box
	 *
	 * @param size
	 *            The size of the box
	 * @return The computed split dimension
	 */
	public abstract int calculateSplitDimension(Vec3 size);

	/**
	 * Distributes the current objects into two bvhs according to the given
	 * splitdimension and split position
	 *
	 * @param a
	 *            The first bvh
	 * @param b
	 *            The second bvh
	 * @param splitdim
	 *            The split dimension in which to split
	 * @param splitpos
	 *            The position used for splitting
	 */
	public abstract void distributeObjects(BVHBase a, BVHBase b,
			int splitdim, float splitpos);
}
