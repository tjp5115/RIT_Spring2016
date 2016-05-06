#include "PhongMaterial.h"

#include <algorithm>
#include <cmath>
PhongMaterial::PhongMaterial() :
Ax(),
Ka(0),
Dx(),
Kd(0),
Sx(),
n(0),
Ks(0),
Kr(0),
Kt(0),
N(0)
{
}
PhongMaterial::PhongMaterial(RGBColor _Ax, float _Ka, RGBColor _Dx, float _Kd, RGBColor _Sx, float _n, float _Ks) :
Ax(_Ax),
Ka(_Ka),
Dx(_Dx),
Kd(_Kd),
Sx(_Sx),
n(_n),
Ks(_Ks)
{
}

/**
 * @details Get the illumination for a ray
 * 
 * @param light
 * @param id data
 * @param int depth
 * @return RGB color for a given point
 */
RGBColor PhongMaterial::get_illumination(Light &light, const IntersectData &id, unsigned int depth){
	if (!id.hit_obj)
		return id.world.background;

	RGBColor retcolor = id.material->local_illumination(light, id);

	if (depth != 0) {
		depth -= 1;
		if (Kr > 0.0){
			retcolor += Kr * reflectColor(light, id, depth);
		}

		if (Kt > 0.0){
			retcolor += Kt * transColor(light, id, depth);
		}
	}
	return retcolor;
}

/**
 * @details get the transparency color for an object
 */
RGBColor PhongMaterial::transColor(Light &light, const IntersectData &id, unsigned int depth){
	Vector3D D = id.ray.d;
	RGBColor retcolor(0);
	Normal n = faceForward(id.n, -D);
	float cos = -D * n;
	float det = 1 + (N*N*(cos * cos - 1));
	if (det < 0){
		retcolor = reflectColor(light, id, depth);
	}else{
		Ray trans = Ray();
		trans.o = id.hit_pt;
		trans.d = N*D + (N * cos - sqrt(det))*n;
		IntersectData data3 = id.world.hit_objects(trans, id.obj_id);
		if (data3.hit_obj){
			retcolor = data3.material->get_illumination(light, data3, depth);
		}
		else{
			retcolor = id.world.background;
		}

	}
	return retcolor;
}


/**
 * @brief get the reclection color for an object
 */
RGBColor PhongMaterial::reflectColor(Light &light, const IntersectData &id, unsigned int depth){
	RGBColor retcolor(0);
	Ray reflect = Ray();
	reflect.o = id.hit_pt;
	reflect.d = id.ray.d - 2 * id.n * (id.ray.d * id.n);
	IntersectData data1 = id.world.hit_objects(reflect, id.obj_id);
	if (data1.hit_obj){
		retcolor = data1.material->get_illumination(light, data1, depth);
	}
	else{
		retcolor = id.world.background;
	}
	return retcolor;
}

/**
 * @details local illumination for an object
 */
RGBColor PhongMaterial::local_illumination(Light &light, const IntersectData &id){
	RGBColor ret;
	Ray shadow = Ray();
	shadow.o = id.hit_pt;
	shadow.d = light.position - id.hit_pt;
	shadow.d.normalize();

	if (light.in_shadow(shadow, id)){
		return RGBColor();
	}

	double t;
	Vector3D L = light.position - id.hit_pt;
	L.normalize();
	Vector3D V = -id.ray.d;
	V.normalize();
	Vector3D R(-L);
	R = R.reflect(id.n);

	RGBColor ambient = get_ambient(id);
	float diffuse_reflection = std::max(L*id.n, 0.0);
	RGBColor diffuse = Dx * Kd * diffuse_reflection;
	RGBColor specular = RGBColor();
	if (diffuse_reflection > 0){
		t = R*V;
		specular = Sx * Ks * std::pow(std::max(t, 0.0), n);
		ret = RGBColor(ambient + light.color * (diffuse + specular));
	}
	else{
		ret = RGBColor(ambient + light.color * diffuse);
	}
	ret.clamp();
	return ret;
}

/**
 * @brief ambient color for an object
 */
RGBColor PhongMaterial::get_ambient(const IntersectData &id){
	return Ka * Ax;
}

/**
 * @brief  face the noraml forward
 */
Normal PhongMaterial::faceForward(Normal A, Vector3D B){
	// For acute angles, dot product is non-negative
	if (A*B >= 0) return A;
	// Obtuse angle, reverse the first vector
	return A * -1;
}
