#include "PhongMaterial.h"

#include <algorithm>
PhongMaterial::PhongMaterial() :
Ax(),
Ka(0),
Dx(),
Kd(0),
Sx(),
n(0),
Ks(0),
Kr(0),
Kt(0)
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

PhongMaterial::~PhongMaterial()
{
}


RGBColor PhongMaterial::get_illumination(Light &light, const IntersectData &id, unsigned int depth){
	/*
	find closest intersection
	if ( !intersection )
		retcolor = background color
	else
		spawn shadow ray
	retcolor = local illumination
	if ( kr > 0 )
		spawn reflection ray
		retcolor += kr * illuminate ( reflect ray )
	if ( kt > 0 )
		spawn transmission ray
		retcolor += kt * illuminate ( transmission ray )
	 return retcolor
 */
	if (depth == 0 )
		return RGBColor();
	
	RGBColor retcolor = id.material->local_illumination(light, id);
	if (Kr > 0.0){
		Ray reflect = Ray();
		reflect.o = id.hit_pt;
		reflect.d = id.ray.d - 2 * id.n * (id.ray.d * id.n);
		IntersectData data = id.world.hit_objects(reflect);
		if (data.hit_obj){
			retcolor += Kr * data.material->get_illumination(light, data, --depth);
		}
		else{
			retcolor += id.world.background;
		}
	}

	if (Kt > 0.0){

	}

	return retcolor;
}

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

RGBColor PhongMaterial::get_ambient(const IntersectData &id){
	return Ka * Ax;
}