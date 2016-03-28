#pragma once
#include "PhongMaterial.h"
class ProceduralShade :
	public PhongMaterial
{
public:
	ProceduralShade(RGBColor _same_color, RGBColor _diff_color, float _square_size, RGBColor _Ax, float _Ka, float _Kd, RGBColor _Sx, float _n, float _Ks);
	~ProceduralShade();
	virtual RGBColor get_illumination(const Light &light, const IntersectData &id);
	virtual RGBColor get_ambient(const IntersectData &id);
private:
	RGBColor same_color;
	RGBColor diff_color;
	float square_size;

	void set_color(const IntersectData &id) ;
};

