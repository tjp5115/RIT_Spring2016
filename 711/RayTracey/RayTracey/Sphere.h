#pragma once
#include "Object.h"
#include "cmatrix"
typedef std::valarray<double> Vector;
class Sphere :
	public Object
{
public:
	Sphere(Vector c, double r);
	~Sphere();

	virtual bool hit(const Ray &r, double &w_min, Traced &tr) const;

private:
	Vector center;
	double radius;
};

