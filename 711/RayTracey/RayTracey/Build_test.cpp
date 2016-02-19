#pragma once
#include "Renderer.h"
#include "World.h"
#include "Sphere.h"
#include "Plane.h"
#include "cmatrix"
using namespace std;
typedef std::valarray<double> Vector;
int main(int argc, char **argv){

	/*
	double s1[] = { 0.0, 0.0, 0.0 };
	double radius_1 = 3.0;
	Sphere * sphere_1 = new Sphere(Vector(s1,3), radius_1);
	w->add_object(sphere_1);

	double s2[] = { 0.0, 0.0, 0.0 };
	double radius_2 = 3.0;
	Sphere * sphere_2 = new Sphere(Vector(s2,3), radius_2);
	w->add_object(sphere_2);

	double p[] = { 0.0, 0.0, 0.0 };
	double n[] = { 0.0, 0.0, 0.0 };
	Plane *plane = new Plane(Vector(p, 3), Vector(n,3));
	w->add_object(plane);
	double s2[] = { 5, -52, -100 };
	double radius_2 = 15.0;
	double dark_green[] = {0.0, 0.41, 0.41};
	Sphere * sphere_2 = new Sphere(Vector(s2, 3), radius_2);
	sphere_2->set_color(Vector(dark_green,3));
	w->add_object(sphere_2);


	double s1[] = { 0, 0, -20 };
	double radius_1 = 10.0;
	double yellow[] = { 0.61, 0.61, 0 };
	Sphere * sphere_1 = new Sphere(Vector(s1, 3), radius_1);
	sphere_1->set_color(Vector(yellow, 3));
	w->add_object(sphere_1);
	*/

	Renderer *r = new Renderer(244, 244, &argc, argv);
	World *w = new World(r);
	
	double background[] = { 0.050, 0.206, 0.342 };
	w->background = Vector(background, 3);

	//set camera
	double vp_dist = 100;
	Point3D eye = Point3D(0, 0, 500);
	Point3D lookat = Point3D(0.0,0.0,0.0);
	Vector3D up = Vector3D(0.0, 1.0, 0.0);
	w->set_camera(eye,lookat,up,vp_dist);

	//set objects

	double radius_1 = 215;
	Sphere * sphere_1 = new Sphere(Point3D(-120, -190, -100), radius_1);
	double c2[] = { 0.001, 0.303, 0.0 };
	sphere_1->set_color(Vector(c2, 3));
	w->add_object(sphere_1);


	double radius_2 = 220;
	Sphere * sphere_2 = new Sphere(Point3D(100,0,0), radius_2);
	double c1[] = { 0.220, 0.028, 0.240 };
	sphere_2->set_color(Vector(c1, 3));
	w->add_object(sphere_2);

	Plane *plane = new Plane(Point3D(0, -400, 0), Normal(0, 1, 0), 30000, 30000);
	double c3[] = { 1, 0, 0 };
	plane->set_color(Vector(c3, 3));
	w->add_object(plane);



		

	w->render_scene();

}
/*
#include "Renderer.h"
int main(int argc, char **argv){
	Renderer *r = new Renderer(400.0f, 400.0f, &argc, argv);
}
*/