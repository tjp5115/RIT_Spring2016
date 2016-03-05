#pragma once
#include "IntersectData.h"
#include "Point3D.h"
#include "RGBColor.h"
class Light
{
public:
	Light();
	Light(Point3D _position, RGBColor _color);
	~Light();
	
	Point3D position;
	RGBColor color;
	bool in_shadow(const Ray &shadow,const IntersectData &id);
};

