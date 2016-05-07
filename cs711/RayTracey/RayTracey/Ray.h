#pragma once
#include "Point3D.h"
#include "Vector3D.h"
class Ray
{
public:
	Ray(): 
	o(3),
	d(3)
{};
	Ray(const Point3D &origin, const Vector3D& direction) : 
	o(origin),
	d(direction)
{};

	Point3D o;
	Vector3D d;

};

