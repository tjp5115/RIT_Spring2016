#pragma once
#include "Renderer.h"
#include "World.h"
#include "Sphere.h"
#include "Plane.h"
#include "Light.h"
#include "PhongMaterial.h"
using namespace std;
int main(int argc, char **argv){

	RGBColor yellow(1, 1, 0);										// yellow
	RGBColor brown(0.71, 0.40, 0.16);								// brown
	RGBColor darkGreen(0.0, 0.41, 0.41);							// darkGreen
	RGBColor orange(1, 0.75, 0);									// orange
	RGBColor green(0, 0.6, 0.3);									// green
	RGBColor lightGreen(0.65, 1, 0.30);								// light green
	RGBColor darkYellow(0.61, 0.61, 0);								// dark yellow
	RGBColor lightPurple(0.65, 0.3, 1);								// light purple
	RGBColor darkPurple(0.5, 0, 1);									// dark purple
	RGBColor dark_grey(0.09);											// grey
	RGBColor light_gray(0.28);											// grey
	RGBColor red(1,.05,.05);											// red
	RGBColor white(1,.8,.9);											// red


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

	Light *light = new Light(Point3D(0,5000,4000), RGBColor(1.0));
	w->add_light(light);

	//set objects
	double radius_1 = 215;
	Sphere * sphere_1 = new Sphere(Point3D(-200, -190, -200), radius_1);

	RGBColor Ax(light_gray);
	float ka = 0.25f;
	RGBColor Dx(light_gray);
	float kd = 0.55f;
	RGBColor Sx(white);
	float n = 6;
	float Ks = 0.7;
	PhongMaterial *material_1 = new PhongMaterial(Ax, ka, Dx, kd, Sx, n, Ks);
	sphere_1->set_material(material_1);
	w->add_object(sphere_1);

	double radius_2 = 220;
	Sphere * sphere_2 = new Sphere(Point3D(100,0,0), radius_2);

	Ax = (dark_grey);
	ka = 0.25;
	Dx = (dark_grey);
	kd = 0.75;
	Sx = (white);
	n = 4;
	Ks = 0.3;

	PhongMaterial *material_2 = new PhongMaterial(Ax, ka, Dx, kd, Sx, n, Ks);
	sphere_2->set_material(material_2);
	w->add_object(sphere_2);


	Plane *plane = new Plane(Point3D(-300, -900, 0), Normal(0, 1, 0), 40000, 40000);

	Ax = (red);
	ka = 0.25;
	Dx = (red);
	kd = 0.85;
	Sx = (white);
	n = 2;
	Ks = 0.3;
	PhongMaterial *material_3 = new PhongMaterial(Ax, ka, Dx, kd, Sx, n, Ks);
	plane->set_material(material_3);

	w->add_object(plane);



		

	w->render_scene();

}
/*
#include "Renderer.h"
int main(int argc, char **argv){
	Renderer *r = new Renderer(400.0f, 400.0f, &argc, argv);
}
*/