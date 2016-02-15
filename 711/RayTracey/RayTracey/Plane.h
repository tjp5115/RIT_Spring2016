#pragma once
#include "Object.h"
#include "cmatrix"
typedef std::valarray<double> Vector;
class Plane :
	public Object
{
public:
	Plane();
	Plane(const Vector &point, const Vector &normal);
	~Plane();

	virtual bool hit(const Ray &r, double &w_min, Traced &tr) const;
private:
	Vector p;
	Vector n;

};

