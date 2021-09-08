package raytracer.shade;

import java.util.Iterator;

import raytracer.core.Hit;
import raytracer.core.LightSource;
import raytracer.core.Shader;
import raytracer.core.Trace;
import raytracer.math.Color;
import raytracer.math.Constants;
import raytracer.math.Ray;
import raytracer.math.Vec3;

public class Phong implements Shader {
	private Shader inner;
	private float diffuseL;
	private float specularL;
	private  float shininess;
	private Color ambient;
	public Phong(final Shader inner, final Color ambient, final float diffuse, final float specular, final float shininess) {
		this.inner = inner;
		this.ambient = ambient;
		this.diffuseL = diffuse;
		this.specularL = specular;
		this.shininess = shininess;
	}
	@Override
	public Color shade(Hit hit, Trace trace) {
		// TODO Auto-generated method stub
		Color diffuse = null;
		Color specular = null;
		Iterator<LightSource> il = trace.getScene().getLightSources().iterator(); 
		while(il.hasNext()) {
			LightSource curr = il.next();
			Vec3 nnorm = curr.getLocation().sub(hit.getPoint());
			Vec3 norm = nnorm.normalized();
			Ray schray = new Ray(hit.getPoint(), norm);
			boolean obadd = true;
			//trace.getScene().hit(schray).getPoint().sub(hit.getPoint()).dot(norm)>0;
			Hit tmphit = trace.getScene().hit(schray);
			if(tmphit.hits()) {
			if(!(tmphit.getPoint().sub(hit.getPoint()).dot(norm)<Constants.EPS)) {
					if(tmphit.getPoint().sub(hit.getPoint()).dot(norm)<=nnorm.dot(norm)) {
						obadd = false;
					}
				}
			}
			if(obadd) {
				float b = hit.getNormal().normalized().dot(norm);
				if(b<0) {
					b = hit.getNormal().scale(-1).normalized().dot(norm);
				}
				float a;
				Vec3 tmpNorm = hit.getNormal().normalized(); 
				Vec3 tmpSR = trace.getRay().dir().scale(-1);
				if(tmpNorm.dot(tmpSR)<Constants.EPS&&Constants.isZero(tmpNorm.dot(tmpSR))) {
					tmpNorm = tmpNorm.scale(-1);
				}
				Vec3 refl = trace.getRay().dir().reflect(tmpNorm);
				float c = nnorm.normalized().dot(refl);
				
				float d;
				if(Constants.isZero(b)) {
					a = 0;
				}else {
					a = b;
				}
				if((c<Constants.EPS)) {
					d = 0;
				}else {
					d = c;
				}
				if(diffuse==null||specular==null) {
					diffuse = curr.getColor().mul(inner.shade(hit, trace)).scale(a).scale(diffuseL);
					specular = curr.getColor().scale(specularL).scale((float)Math.pow(d, shininess));
				}else {
				diffuse = diffuse.add(curr.getColor().mul(inner.shade(hit, trace)).scale(diffuseL).scale(a));
				specular = specular.add(curr.getColor().scale(specularL).scale((float)Math.pow(d, shininess)));
				}
			}
			
		}
		if(specular!=null&&diffuse!=null) {
			 Color tmp = ambient.add(diffuse).add(specular);
			return tmp;
		}
		return this.ambient;
	}

}
