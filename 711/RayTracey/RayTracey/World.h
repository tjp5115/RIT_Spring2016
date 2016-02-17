#pragma once
#include "ViewPlane.h"
#include "Tracer.h"
#include "Traced.h"
#include "Object.h"
#include "Ray.h"
#include "Renderer.h"
#include <vector>
#include "cmatrix"

typedef std::valarray<double> Vector; 
using namespace std;

struct Camera 
{
	Vector n, u, v, lookat, eye, up;
	double d;
};

class World
{
public:
	World(Renderer *renderer);
	~World();

	Vector background;
	Tracer* tracer_ptr;
	vector<Object*> objects;
	
	void add_object(Object *o);
	void render_scene();
	Traced hit_objects(const Ray &ray);

	void set_camera(Vector eye, Vector lookat, Vector up);
private:
	Renderer *renderer;
	const double BIG_NUMBER = 1.0E10;
	void compute_uvw(void);
	Camera camera;
	Vector cross_product_3x1(const Vector a, const Vector b);
	Vector normalize(const Vector v);
	Vector get_camera_direction(Vector pixel);
};

