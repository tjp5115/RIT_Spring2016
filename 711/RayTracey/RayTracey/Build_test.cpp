#include "Renderer.h"
#include "World.h"
#include "Sphere.h"
#include "Plane.h"
#include "cmatrix"
using namespace std;
typedef std::valarray<double> Vector;
int main(int argc, char **argv){
	Renderer *r = new Renderer(400, 400, &argc, argv);
	World *w = new World(r);
	
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
	*/
	double s2[] = { 5, -52, -100 };
	double radius_2 = 15.0;
	double dark_green[] = {0.0, 0.41, 0.41};
	Sphere * sphere_2 = new Sphere(Vector(s2, 3), radius_2);
	sphere_2->set_color(Vector(dark_green,3));
	w->add_object(sphere_2);


	double s1[] = { -20, -57, -120 };
	double radius_1 = 15.0;
	double yellow[] = { 0.61, 0.61, 0 };
	Sphere * sphere_1 = new Sphere(Vector(s1, 3), radius_1);
	sphere_1->set_color(Vector(yellow, 3));
	w->add_object(sphere_1);
	

	w->render_scene();

}
/*
#include "Renderer.h"
int main(int argc, char **argv){
	Renderer *r = new Renderer(400.0f, 400.0f, &argc, argv);
}
*/