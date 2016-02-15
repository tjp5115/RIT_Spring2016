#include "Plane.h"


Plane::Plane(const Vector &point, const Vector &normal)
{
	p = point;
	n = normal;
}


Plane::~Plane()
{
}

bool Plane::hit(const Ray &ray, double &w_min, Traced &tr) const{
	double w = ((p - ray.o) * n).sum() / (ray.d * n).sum();

	if (w > 0.0){
		w_min = w;
		tr.n = n;
		tr.hit_pt = ray.o + w * ray.d;
		return true;
	}

	return false;
}