#include "Sphere.h"


Sphere::Sphere(Vector c, double r) :
	center(c),
	radius(r)
{}


Sphere::~Sphere()
{
}


bool Sphere::hit(const Ray &ray, double &w_min, Traced &tr) const{ 
	Vector temp = ray.o - center;
	double b = (2.0 * temp * ray.d).sum();
	double c = (temp * temp - radius * radius).sum();
	double root = b*b - 4 * c;
	
	if (root >= 0.0){
		w_min = (-b - sqrt(root)) / 2;
		tr.hit_pt = ray.o + w_min * ray.d;
		tr.n = (tr.hit_pt - center) / radius;
		return true;
	}
	return false;
}