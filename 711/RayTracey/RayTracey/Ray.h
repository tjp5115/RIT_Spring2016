#pragma once
#include "Point3D.h"
#include "Vector3D.h"
class Ray
{
public:
	Ray();
	Ray(const Point3D &origin, const Vector3D& direction);
	~Ray();

	Point3D o;
	Vector3D d;

};

