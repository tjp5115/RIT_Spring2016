#include "Sphere.h"
#include <iostream>
using namespace std;
const double Sphere::kEpsilon = 0.001;
Sphere::Sphere(Point3D c, double r) :
	center(c),
	radius(r)
{}


Sphere::~Sphere()
{
}


/**
 * @brief determines if a ray has hit the sphere
 */
bool Sphere::hit(const Ray &ray, double &tmin, IntersectData &sr) const{ 
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
		if (w > kEpsilon){
			tmin = w;
			sr.n = (temp + w * ray.d) / radius;
			sr.n.normalize();
			sr.hit_pt = ray.o + w * ray.d;
			return (true);
		}

		w = (-b + e) / 2.0;

		if (w > kEpsilon){
			tmin = w;
			sr.n = (temp + w * ray.d) / radius;
			sr.n.normalize();
			sr.hit_pt = ray.o + w * ray.d;
			return (true);
		}
	}
	return false;
}


/**
 * @brief determines if a shadow is ont he sphere
 */
bool Sphere::shadow_hit(const Ray &ray, double &tmin) const{
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
		if (w > kEpsilon){
			tmin = w;
			return (true);
		}

		w = (-b + e) / 2.0;

		if (w > kEpsilon){
			tmin = w;
			return (true);
		}
	}
	return false;
}