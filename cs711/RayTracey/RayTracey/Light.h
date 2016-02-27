#pragma once
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
};

