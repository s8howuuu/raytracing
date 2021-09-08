package raytracer.core.def;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import raytracer.core.Hit;
import raytracer.core.Obj;
import raytracer.geom.BBox;
import raytracer.math.Pair;
import raytracer.math.Point;
import raytracer.math.Ray;
import raytracer.math.Vec3;

/**
 * Represents a bounding volume hierarchy acceleration structure
 */
public class BVH extends BVHBase {
	private BBox bvhBox;
	ArrayList<Obj> bvhObjList;
	int neb1;
	int neb2;
	ArrayList<BVH> bvHi;

	public BVH() {
		bvhObjList = new ArrayList<Obj>();
		bvhBox = null;
		neb1 = Integer.MAX_VALUE;
		neb2 = Integer.MAX_VALUE;
		bvHi = new ArrayList<BVH>();
		
	}
	public BVH(ArrayList<BVH> a) {
		bvhObjList = new ArrayList<Obj>();
		bvhBox = null;
		neb1 = Integer.MAX_VALUE;
		neb2 = Integer.MAX_VALUE;
		this.bvHi = a;
	}

	@Override
	public BBox bbox() {
		return bvhBox;
	}

	/**
	 * Adds an object to the acceleration structure
	 *
	 * @param prim
	 *            The object to add
	 */
	@Override
	public void add(final Obj prim) {
		bvhObjList.add(prim);
		if(bvhBox == null) {
			this.bvhBox = prim.bbox();
		}else {
			Point mm = prim.bbox().getMin();
			Point mx = prim.bbox().getMax();
			float nex = Float.max(mx.x(),bvhBox.getMax().x());
			float ney = Float.max(mx.y(),bvhBox.getMax().y());
			float nez = Float.max(mx.z(),bvhBox.getMax().z());
			float nex1 = Float.min(mm.x(),bvhBox.getMin().x());
			float ney1 = Float.min(mm.y(),bvhBox.getMin().y());
			float nez1 = Float.min(mm.z(),bvhBox.getMin().z());
			Point nemax = new Point(nex, ney, nez);
			Point nemin = new Point(nex1,ney1,nez1);
			this.bvhBox = BBox.create(nemax, nemin);
			
		}
		
	}

	/**
	 * Builds the actual bounding volume hierarchy
	 */
	@Override
	public void buildBVH() {
		if(this.bvhObjList.size()<=4) {
		}else {
		 BVH nebt1 = new BVH(this.bvHi);
		 BVH nebt2 = new BVH(this.bvHi);
		int tmp = this.calculateSplitDimension(this.bvhBox.getMax().sub(this.bvhBox.getMin()));
		float splitpos;
		if(tmp==0) {
			splitpos = this.calculateMinMax().b.avg(this.calculateMinMax().a).x();
		}else if(tmp==1) {
			splitpos = this.calculateMinMax().b.avg(this.calculateMinMax().a).y();
		}else {
			splitpos = this.calculateMinMax().b.avg(this.calculateMinMax().a).z();
			
		}
		this.distributeObjects(nebt1, nebt2, tmp, splitpos);
		this.bvHi.add(nebt1);
		this.neb1 = bvHi.indexOf(nebt1);
		this.bvHi.add(nebt2);
		this.neb2 = bvHi.indexOf(nebt2);
		nebt2.buildBVH();
		nebt1.buildBVH();

		}
	}

	@Override
	public Pair<Point, Point> calculateMinMax() {
		float nexax,neyax,nezax,nexin,neyin,nezin;
		Iterator<Obj> OI = this.bvhObjList.iterator();
		Obj tmpObj1 = OI.next();
		nexax = tmpObj1.bbox().getMin().x();
		neyax = tmpObj1.bbox().getMin().y();
		nezax = tmpObj1.bbox().getMin().z();
		nexin = tmpObj1.bbox().getMin().x();
		neyin = tmpObj1.bbox().getMin().y();
		nezin = tmpObj1.bbox().getMin().z();
		while(OI.hasNext()) {
			Obj tmpObj = OI.next();
			nexax = Float.max(nexax, tmpObj.bbox().getMin().x());
			neyax = Float.max(neyax, tmpObj.bbox().getMin().y());
			nezax = Float.max(nezax, tmpObj.bbox().getMin().z());
			nexin = Float.min(nexin, tmpObj.bbox().getMin().x());
			neyin = Float.min(neyin, tmpObj.bbox().getMin().y());
			nezin = Float.min(nezin, tmpObj.bbox().getMin().z());
		}
		
		Point returMax = new Point(nexax,neyax,nezax);
		Point returMin = new Point(nexin,neyin,nezin);
		return new Pair<Point, Point>(returMin,returMax);
	}

	@Override
	public int calculateSplitDimension(final Vec3 size) {
		
		float tmp = size.x();
		tmp = Float.max(tmp, size.y());
		tmp = Float.max(tmp, size.z());
		if(tmp == size.x()) {
			return 0;
		}else if(tmp == size.y()) {
			return 1;
		}else return 2;
	}

	@Override
	public void distributeObjects(final BVHBase a, final BVHBase b,
			final int splitdim, final float splitpos) {
		if(splitdim == 0) {
			Iterator<Obj> IO = this.bvhObjList.iterator();
			while(IO.hasNext()) {
				Obj tmp = IO.next();
				if(tmp.bbox().getMin().x()>splitpos) {
					b.add(tmp);
				}else {
					a.add(tmp);
				}
			}
		}
		if(splitdim == 1) {
			Iterator<Obj> IO = this.bvhObjList.iterator();
			while(IO.hasNext()) {
				Obj tmp = IO.next();
				if(tmp.bbox().getMin().y()>splitpos) {
					b.add(tmp);
				}else {
					a.add(tmp);
				}
			}
		}
		if(splitdim == 2) {
			Iterator<Obj> IO = this.bvhObjList.iterator();
			while(IO.hasNext()) {
				Obj tmp = IO.next();
				if(tmp.bbox().getMin().z()>splitpos) {
					b.add(tmp);
				}else {
					a.add(tmp);
				}
			}
		}
	}

	@Override
	public Hit hit(final Ray ray, final Obj obj, final float tmin, final float tmax) {
		if(this.bvhBox.hit(ray, tmin, tmax).hits()) {
		if(neb1==Integer.MAX_VALUE&&neb2==Integer.MAX_VALUE) {
			float tmax1 = tmax;
			Hit nearest = Hit.No.get();
			for(Obj p:this.bvhObjList) {
				final Hit hit = p.hit(ray, p, tmin, tmax1);
				if (hit.hits()) {
					final float t = hit.getParameter();
					if (t < tmax1) {
						nearest = hit;
						tmax1 = t;
					}
				}
			}
			return nearest;
		}else {
			BVH tmp1 = this.bvHi.get(neb1);
			BVH tmp2 = this.bvHi.get(neb2);
			if(tmp1.bbox().hit(ray, tmin, tmax).hits()&&tmp2.bbox().hit(ray, tmin, tmax).hits()) {
				Hit ttmp1 = tmp1.hit(ray, obj, tmin, tmax);
				if(!ttmp1.hits()) {
					return tmp2.hit(ray, obj, tmin, tmax);
				}
				Hit ttmp2 = tmp2.hit(ray, obj, tmin, tmax);
				if(!ttmp2.hits()) {
					return ttmp1;
				}
				if(ttmp1.getParameter()<ttmp2.getParameter()) {
					return ttmp1;
				}else return ttmp2;
				
			}else if(tmp2.bbox().hit(ray, tmin, tmax).hits()){
				return tmp2.hit(ray, obj, tmin, tmax);
			}else if(tmp1.bbox().hit(ray, tmin, tmax).hits()) {
				return tmp1.hit(ray, obj, tmin, tmax);
			}
		}
		}
		return Hit.No.get();
	}


	@Override
	public List<Obj> getObjects() {
		return this.bvhObjList;
		
	}
}
