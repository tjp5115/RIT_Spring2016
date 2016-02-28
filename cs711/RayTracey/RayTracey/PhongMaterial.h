#pragma once
#include "Material.h"

class PhongMaterial :  public Material
{
public:
	PhongMaterial();
	PhongMaterial(RGBColor _Ax, float _Ka, RGBColor _Dx, float _Kd, RGBColor _Sx, float _n, float _Ks, RGBColor _ambient);
	~PhongMaterial();
	
	virtual RGBColor get_illumination(const Light &light, const IntersectData &id, const Ray &shadow) const;
	virtual RGBColor get_ambient() const;
private:

	RGBColor Ax;
	float Ka;

	RGBColor Dx;
	float Kd;

	RGBColor Sx;

	float n;
	float Ks;

	RGBColor ambient;


	
};

