#include "Ray.h"


Ray::Ray() : 
	o(3),
	d(3)
{}
Ray::Ray(const Vector &origin, const Vector &direction) : 
	o(origin),
	d(direction)
{}


Ray::~Ray()
{
}
