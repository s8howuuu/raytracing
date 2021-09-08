package raytracer.geom;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.core.def.LazyHitTest;
import raytracer.math.Constants;
import raytracer.math.Point;
import raytracer.math.Ray;
import raytracer.math.Vec2;
import raytracer.math.Vec3;

class Plane extends BBoxedPrimitive {
	private final Vec3 ne;
	private final float distancePlane;
	private Point supp;
	
	public Plane(Point a,Point b,Point c) {
		this.supp = a;
		Vec3 ue;
		Vec3 ne;
		ue = b.sub(a);
		ne = c.sub(a);
		if(ue.cross(ne).normalized().dot(supp)>Constants.EPS) {
			this.ne=ue.cross(ne).normalized();
		}else{
			this.ne = ue.cross(ne).normalized().scale(-1);
		}
		this.distancePlane = a.dot(this.ne);
		
	}
	public Plane(Vec3 gne,Point aufp) {
		this.ne = gne.normalized();
		this.distancePlane = aufp.dot(this.ne);
		this.supp = aufp;
		
	}
	@Override
	public Hit hitTest(Ray ray, Obj obj, float tmin, float tmax) {
		// TODO Auto-generated method stub
		return new LazyHitTest(obj) {
			private float distance;
			private Point point = null;
			@Override
			public float getParameter() {
				// TODO Auto-generated method stub
				return distance;
			}

			@Override
			public Point getPoint() {
				// TODO Auto-generated method stub
				if(point==null) {
					point = ray.eval(distance).add(ne.scale(0.0001f));
				}
				return point;
			}

			@Override
			public Vec3 getNormal() {
				// TODO Auto-generated method stub
				return ne;
			}

			@Override
			public Vec2 getUV() {
				// TODO Auto-generated method stub
				Vec2 toReturn;
				toReturn =  Util.computePlaneUV(ne,supp, point);
				return toReturn;
			}

			@Override
			protected boolean calculateHit() {
				// TODO Auto-generated method stub
				float tmp1 = (distancePlane - ray.base().dot(ne))/ray.dir().dot(ne);
				Vec3 tmpv3 = ray.dir().scale(tmp1);
				if(tmp1<tmin||tmp1>tmax) {
					return false;
				}
				if(tmp1>Constants.EPS) {
				point = ray.base().add(tmpv3);
				distance = tmp1;
				}
				return tmp1>Constants.EPS;
			}
			
		};
	}
	
	@Override
	public int hashCode() {
		return this.ne.hashCode()^( Float.valueOf(this.distancePlane).hashCode());
	}
	@Override
	public boolean equals(final Object other) {
		if(other instanceof Plane) {
			final Plane tmpPlane = (Plane)other;
			return Constants.isEqual(tmpPlane.distancePlane, this.distancePlane)&&(this.ne.equals(tmpPlane.ne));
		}
		return false;
	}

}
