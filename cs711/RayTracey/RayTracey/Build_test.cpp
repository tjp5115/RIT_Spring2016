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
	RGBColor grey(0.25);											// grey


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

	Light *light = new Light(Point3D(0,300,400), darkYellow);
	w->add_light(light);

	//set objects
	double radius_1 = 215;
	Sphere * sphere_1 = new Sphere(Point3D(-120, -190, -100), radius_1);

	RGBColor Ax(darkGreen);
	float ka = 0.25;
	RGBColor Dx(lightPurple);
	float kd = 0.75;
	RGBColor Sx(darkPurple);
	float n = 4;
	float Ks = 0.3;
	RGBColor ambient(grey);
	PhongMaterial *material_1 = new PhongMaterial(Ax, ka, Dx, kd, Sx, n, Ks, ambient);
	sphere_1->set_material(material_1);
	w->add_object(sphere_1);

	double radius_2 = 220;
	Sphere * sphere_2 = new Sphere(Point3D(100,0,0), radius_2);

	Ax = (darkGreen);
	ka = 0.25;
	Dx = (lightPurple);
	kd = 0.75;
	Sx = (darkPurple);
	n = 4;
	Ks = 0.3;

	ambient = (grey);
	PhongMaterial *material_2 = new PhongMaterial(Ax, ka, Dx, kd, Sx, n, Ks,ambient);
	sphere_2->set_material(material_2);
	w->add_object(sphere_2);


	Plane *plane = new Plane(Point3D(0, -370, 0), Normal(0, 1, 0), 30000, 30000);

	Ax = (darkGreen);
	ka = 0.25;
	Dx = (lightPurple);
	kd = 0.75;
	Sx = (darkPurple);
	n = 4;
	Ks = 0.3;
	ambient = (grey);
	PhongMaterial *material_3 = new PhongMaterial(Ax, ka, Dx, kd, Sx, n, Ks,ambient);
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