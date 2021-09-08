package raytracer.geom;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.core.def.LazyHitTest;
import raytracer.math.Constants;
import raytracer.math.Point;
import raytracer.math.Ray;
import raytracer.math.Vec2;
import raytracer.math.Vec3;

public class Sphere extends BBoxedPrimitive {
	private final Point center;
	private final float radius;
	public Sphere(Point c,float r) {
		super(BBox.create(c.add(new Vec3(0,0,-r)).add(new Vec3(0,-r,0)).add(new Vec3(-r,0,0)),c.add(new Vec3(0,0,r)).add(new Vec3(0,r,0)).add(new Vec3(r,0,0))));
		this.center = c;
		this.radius = r;
	}
	@Override
	public Hit hitTest(Ray ray, Obj obj, float tmin, float tmax) { 
		// TODO Auto-generated method stub
		return new LazyHitTest(obj) {
			private float distance;
			private Point hitP = null;
			@Override
			public float getParameter() {
				// TODO Auto-generated method stub
				return distance;
			}
			
			@Override
			public Point getPoint() {
				// TODO Auto-generated method stub
				return hitP;
			}

			@Override
			public Vec3 getNormal() {
				// TODO Auto-generated method stub
				Vec3 tmpNorm;
				Vec3 hpC = center.sub(hitP);
				Vec3 hitO = Point.ORIGIN.sub(hitP);
				if(hpC.dot(hitO)>Constants.EPS) {
					tmpNorm = hpC.normalized();
				}else {
					tmpNorm = hpC.scale(-1).normalized();
				}
				return tmpNorm;
			}

			@Override
			public Vec2 getUV() {
				// TODO Auto-generated method stub
				Vec3 radial = center.sub(hitP).scale(-1);
				return Util.computeSphereUV(radial);
			}

			@Override
			protected boolean calculateHit() {
				// TODO Auto-generated method stub
				float b; 
				b = 2.0f*ray.dir().dot(ray.base().sub(center));
				float c;
				c = (ray.base().sub(center)).dot(ray.base().sub(center)) - radius*radius;
				float tmp1 = b*b-4.0f*c;
				double tmp = (Math.sqrt((double)(b*b-4.0f*c)));
				float lmd1 = (-b+(float)tmp)/2.0f;
				float lmd2 = (-b-(float)tmp)/2.0f;
				assert(!(lmd1<Constants.EPS));
				assert(!(lmd2<Constants.EPS));
				if(lmd2<tmin||lmd2>tmax) {
					return false;
				}
				hitP = ray.base().add(ray.dir().scale(lmd2));
				distance = lmd2;
				return tmp1 >Constants.EPS||Constants.isZero(tmp1);
			}
			
		};
	}
	@Override
	public int hashCode() {
		return center.hashCode()^Float.valueOf(radius).hashCode();
	}
	@Override
	public boolean equals(final Object other) {
		if(other instanceof Sphere) {
			Sphere tmpSp = (Sphere) other;
			return tmpSp.center.equals(this.center)&&Constants.isEqual(this.radius, tmpSp.radius);
		}
		return false;
	}

}
