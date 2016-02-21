#pragma once
#include "Object.h"
#include "Point3D.h"
class Sphere :
	public Object
{
public:
	Sphere(Point3D c, double r);
	~Sphere();

	virtual bool hit(const Ray &r, double &w_min, Traced &tr) const;

private:
	Point3D center;
	double radius;
};
