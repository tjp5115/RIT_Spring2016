#include "PhongMaterial.h"

#include <algorithm>
PhongMaterial::PhongMaterial():
Ax(),
Ka(0),
Dx(),
Kd(0),
Sx(),
n(0),
Ks(0)
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


RGBColor PhongMaterial::get_illumination(const Light &light, const IntersectData &id) const{
	RGBColor ret;
	double t;
	Vector3D L = light.position - id.hit_pt;
	L.normalize();
	Vector3D V = -id.ray.d;
	V.normalize();
	Vector3D R(-L);
	R = R.reflect(id.n);

	RGBColor ambient = get_ambient();
	float diffuse_reflection = std::max(L*id.n,0.0);
	RGBColor diffuse = Dx * Kd * diffuse_reflection;
	RGBColor specular = RGBColor();


	if (diffuse_reflection > 0){
		t = R*V;
		specular = Sx * Ks * std::pow(std::max(t, 0.0), n);
		ret = RGBColor(ambient + light.color * (diffuse + specular));
	}else{
		ret = RGBColor(ambient + light.color * diffuse);
	}
	ret.clamp(); 
	return ret;
}

RGBColor PhongMaterial::get_ambient() const{
	return Ka * Ax;
}