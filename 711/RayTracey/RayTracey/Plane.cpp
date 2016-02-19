#include "Plane.h"
#include <iostream>
using namespace std;

Plane::Plane(const Point3D &point, const Normal &normal, double _w, double _h)
{
	p = point;
	n = normal;
	w = _w;
	h = _h;
	p1 = Point3D(p.x - w, p.y, p.z + h);
	p2 = Point3D(p.x + w, p.y, p.z + h);
	p3 = Point3D(p.x + w, p.y, p.z - h);
	p4 = Point3D(p.x - w, p.y, p.z - h);

}


Plane::~Plane()
{
}

bool Plane::hit(const Ray &ray, double &w_min, Traced &tr) const{
	/*
	double w = (p - ray.o) * n / (ray.d * n);
	//cout << ray.o.x << ray.o.y << ray.o.z << endl;
	//cout << w << endl;
	if (w > 0.0){
		w_min = w;
		tr.n = n;
		tr.hit_pt = ray.o + w * ray.d;
		return true;
	}
	*/

	double w = (p - ray.o) * n / (ray.d * n);
	//cout << ray.o.x << ray.o.y << ray.o.z << endl;
	//cout << w << endl;
	if (w <= 0.0){
		return false;
	}
	w_min = w;
	tr.n = n;
	tr.hit_pt = ray.o + w * ray.d;

	//cout << "hit plane: " << tr.hit_pt.x << tr.hit_pt.y << tr.hit_pt.z << endl;
	double angle = 0;
	Vector3D v1, v2;


	v1 = tr.hit_pt - p1;
	v2 = tr.hit_pt - p2;
	v1.normalize();
	v2.normalize();
	angle += acos(abs(v1*v2));

	//cout << acos(abs(v1*v2)) << endl;
	v1 = tr.hit_pt - p2;
	v2 = tr.hit_pt - p3;
	v1.normalize();
	v2.normalize();
	angle += acos(abs(v1*v2));

	//cout << acos(abs(v1*v2)) << endl;
	v1 = tr.hit_pt - p3;
	v2 = tr.hit_pt - p4;
	v1.normalize();
	v2.normalize();
	angle += acos(abs(v1*v2));

	//cout << acos(abs(v1*v2)) << endl;
	v1 = tr.hit_pt - p4;
	v2 = tr.hit_pt - p1;
	v1.normalize();
	v2.normalize();
	//cout << acos(abs(v1*v2)) << endl;

	angle += acos(abs(v1*v2));
	//cout << acos(abs(v1*v2)) << endl;
	//cout << angle << endl;
	
	if (angle >= 6.2){
		//cout << "hit" << endl;
		return true;
	}

	return false;
}