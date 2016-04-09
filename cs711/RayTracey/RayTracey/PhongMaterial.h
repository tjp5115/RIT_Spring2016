#pragma once
#include "Material.h"

class PhongMaterial :  public Material
{
public:
	PhongMaterial();
	PhongMaterial(RGBColor _Ax, float _Ka, RGBColor _Dx, float _Kd, RGBColor _Sx, float _n, float _Ks);
	~PhongMaterial();
	
	virtual RGBColor get_illumination(Light &light, const IntersectData &id, unsigned int depth);
	virtual RGBColor get_ambient(const IntersectData &id);
	virtual RGBColor local_illumination(Light &light, const IntersectData &id);
	float Kr;
	float Kt;
protected:


	RGBColor Ax;
	float Ka;

	RGBColor Dx;
	float Kd;

	RGBColor Sx;

	float n;
	float Ks;

};

