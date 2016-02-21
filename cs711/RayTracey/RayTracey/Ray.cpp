#include "Ray.h"


Ray::Ray() : 
	o(3),
	d(3)
{}
Ray::Ray(const Point3D &origin, const Vector3D &direction) : 
	o(origin),
	d(direction)
{}


Ray::~Ray()
{
}
