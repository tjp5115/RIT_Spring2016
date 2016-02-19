#include "Sphere.h"
#include <iostream>
using namespace std;

Sphere::Sphere(Point3D c, double r) :
	center(c),
	radius(r)
{}


Sphere::~Sphere()
{
}


bool Sphere::hit(const Ray &ray, double &tmin, Traced &sr) const{ 
	double w;
	Vector3D temp = ray.o - center;
	double b = 2.0 * temp * ray.d;
	double c = temp * temp - radius * radius;
	double disc = b * b - 4.0 * c;
	if (disc < 0.0)
		return(false);
	else {
		double e = sqrt(disc);
		w = (-b - e) / 2.0;
		tmin = w;
		sr.n = (temp + w * ray.d) / radius;
		sr.hit_pt= ray.o + w * ray.d;
		return (true);
	}
}