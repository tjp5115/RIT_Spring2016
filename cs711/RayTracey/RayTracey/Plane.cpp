#include "Plane.h"
#include <iostream>
using namespace std;
const double Plane::kEpsilon = 0.00001;
Plane::Plane(const Point3D &point, const Normal &normal, double _w, double _h)
{
	p = point;
	n = normal;
	w = _w;
	h = _h;
	Xmin = p.x - w;
	Xmax = p.x + w;
	Zmax = p.z + h;
	Zmin = p.z - h;
}

/**
 * @brief determines if the ray hits the plane
 */
bool Plane::hit(const Ray &ray, double &w_min, IntersectData &tr) const{

	double w_ = (p - ray.o) * n / (ray.d * n);
	//cout << ray.o.x << ray.o.y << ray.o.z << endl;
	//cout << w << endl;
	if (w_ <= kEpsilon){
		return false;
	}
	w_min = w_;
	tr.n = n;
	tr.hit_pt = ray.o + w_ * ray.d;
	tr.texture.z = (tr.hit_pt.z + h - p.z);
	tr.texture.x = (tr.hit_pt.x + w - p.x);

	if (tr.hit_pt.x > Xmin && tr.hit_pt.x < Xmax && tr.hit_pt.z > Zmin && tr.hit_pt.z < Zmax)
		return true;
	return false;
}


/**
 * @brief determines if a shadow is casted on the object.
 */
bool Plane::shadow_hit(const Ray &ray, double &w_min) const{
	double w_ = (p - ray.o) * n / (ray.d * n);
	if (w_ <= kEpsilon){
		return false;
	}
	w_min = w_;
	Point3D hit_pt = ray.o + w_ * ray.d;
	if (hit_pt.x > Xmin && hit_pt.x < Xmax && hit_pt.z > Zmin && hit_pt.z < Zmax)
		return true;
	return false;
}