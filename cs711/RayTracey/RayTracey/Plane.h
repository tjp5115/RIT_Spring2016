#pragma once
#include "Object.h"
#include "Point3D.h"
#include "Normal.h"

/**
 * @brief object shape that represents a fixed size plane
 */
class Plane :
	public Object
{
public:
	Plane();
	Plane(const Point3D &point, const Normal &normal,double _w, double _h);

	virtual bool hit(const Ray &r, double &w_min, IntersectData &tr) const;
	virtual bool shadow_hit(const Ray &r, double &t) const;
private:
	Point3D p;
	Normal n;
	double w;
	double h;
	float Xmin;
	float Xmax;
	float Zmax;
	float Zmin;
	static const double kEpsilon;
};

