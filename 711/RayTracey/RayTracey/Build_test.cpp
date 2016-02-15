#include "Renderer.h"
#include "World.h"
#include "Sphere.h"
#include "Plane.h"

#include "cmatrix"
typedef std::valarray<double> Vector;
int main(int argc, char **argv){
	Renderer *r = new Renderer(430.0f, 450.0f, &argc, argv);
	World *w = new World(r);

	double s1[] = { 0.0, 0.0, 0.0 };
	double radius_1 = 3.0;
	Sphere * sphere_1 = new Sphere(Vector(s1,3), radius_1);
	w->add_object(sphere_1);
	
	double s2[] = { 0.0, 0.0, 0.0 };
	double radius_2 = 3.0;
	Sphere * sphere_2 = new Sphere(Vector(s1,3), radius_2);
	w->add_object(sphere_2);
	
	double p[] = { 0.0, 0.0, 0.0 };
	double n[] = { 0.0, 0.0, 0.0 };
	Plane *plane = new Plane(Vector(p, 3), Vector(n,3));
	w->add_object(plane);

	w->render_scene();
}