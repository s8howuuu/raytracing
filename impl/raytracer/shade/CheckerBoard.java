package raytracer.shade;

import raytracer.core.Hit;
import raytracer.core.Shader;
import raytracer.core.Trace;
import raytracer.math.Color;
import raytracer.math.Vec2;

public class CheckerBoard implements Shader {
	private Shader first;
	private Shader second;
	private final float scale;
	public CheckerBoard(Shader a,Shader b,final float scale) {
		this.first = a;
		this.second = b;
		this.scale = scale;
	}
	@Override
	public Color shade(Hit hit, Trace trace) {
		// TODO Auto-generated method stub
		Vec2 uv;
		uv = hit.getUV();
		
		float x = uv.x();
		float y = uv.y();
		float tmp1;
		float tmp2;
		tmp1 = x/this.scale;
		tmp2 = y/this.scale;
		int tmp01 = (int) Math.floor(tmp1);
		int tmp02 = (int) Math.floor(tmp2);
		int tmp3 = tmp01 + tmp02;
		boolean tmpB = (tmp3%2==0);
		if(tmpB) {
			return this.first.shade(hit, trace);
		}else {
			return this.second.shade(hit, trace);
		}
		
	}

}
