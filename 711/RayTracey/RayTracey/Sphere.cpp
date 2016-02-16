#include "Sphere.h"
#include <iostream>
using namespace std;

Sphere::Sphere(Vector c, double r) :
	center(c),
	radius(r)
{}


Sphere::~Sphere()
{
}


bool Sphere::hit(const Ray &ray, double &tmin, Traced &sr) const{ 
	/*
	Vector temp = ray.o - center;
	double a = (ray.d * ray.d).sum();
	double b = (2.0 * temp * ray.d).sum();
	double c = (temp * temp - radius * radius).sum();
	double root = b*b - 4 * a * c;
	if (a != 1.0) cout << "a is not normal =(" << endl;
	double kEpsilon = 0.001;
	double w;
	//cout << root << endl;
	if (root >= 0.0){
		w_min = (-b - sqrt(root)) / ( 2 );
		tr.hit_pt = ray.o + w_min * ray.d;
		tr.n = (tr.hit_pt - center) / radius;
		return true;
	}
	*/
	double 		t;
	Vector temp = ray.o - center;
	double 		a = (ray.d * ray.d).sum();
	double 		b = 2.0 * (temp * ray.d).sum();
	double 		c = (temp * temp).sum() - radius * radius;
	double 		disc = b * b - 4.0 * a * c;
	double kEpsilon = 0.001;
	
	cout << a << " "<< b << " " << c << endl;
	if (disc < 0.0)
		return(false);
	else {
		double e = sqrt(disc);
		double denom = 2.0 * a;
		cout << "hit" << endl;
		t = (-b - e) / denom;    // smaller root

		if (t > kEpsilon) {
			tmin = t;
			sr.n = (temp + t * ray.d) / radius;
			sr.hit_pt= ray.o + t * ray.d;
			return (true);
		}

		t = (-b + e) / denom;    // larger root

		if (t > kEpsilon) {
			tmin = t;
			sr.n = (temp + t * ray.d) / radius;
			sr.hit_pt= ray.o + t * ray.d;
			return (true);
		}
	}
	cout << "no hit" << endl;
	return false;
}