#pragma once
#include "Renderer.h"
#include "World.h"
#include "Sphere.h"
#include "Plane.h"
using namespace std;
int main(int argc, char **argv){


	Renderer *r = new Renderer(244, 244, &argc, argv);
	World *w = new World(r);
	w->background = RGBColor(0.050, 0.206, 0.342);

	//set camera
	double vp_dist = 100;
	//Point3D eye = Point3D(0, 500, 0);
	Point3D eye = Point3D(0, 0, 500);
	Point3D lookat = Point3D(0, 0, 0);
	//Point3D lookat = Point3D(-120, -190, -100);
	Vector3D up = Vector3D(0.0, 1.0, 0.0);
	w->set_camera(eye,lookat,up,vp_dist);

	//set objects

	double radius_1 = 215;
	Sphere * sphere_1 = new Sphere(Point3D(-120, -190, -100), radius_1);
	double c2[] = { 0.001, 0.303, 0.0 };
	sphere_1->set_color(RGBColor(0.001, 0.303, 0.0));
	w->add_object(sphere_1);


	double radius_2 = 220;
	Sphere * sphere_2 = new Sphere(Point3D(100,0,0), radius_2);
	double c1[] = { 0.220, 0.028, 0.240 };
	sphere_2->set_color(RGBColor(0.220, 0.028, 0.240));
	w->add_object(sphere_2);

	Plane *plane = new Plane(Point3D(0, -400, 0), Normal(0, 1, 0), 30000, 30000);
	plane->set_color(RGBColor(1, 0, 0));
	w->add_object(plane);



		

	w->render_scene();

}
/*
#include "Renderer.h"
int main(int argc, char **argv){
	Renderer *r = new Renderer(400.0f, 400.0f, &argc, argv);
}
*/