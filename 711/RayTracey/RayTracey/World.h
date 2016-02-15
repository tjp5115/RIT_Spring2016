#pragma once
#include "ViewPlane.h"
#include "Tracer.h"
#include "Object.h"
#include "Ray.h"
#include "Renderer.h"
#include <vector>
#include "cmatrix"
typedef std::valarray<double> Vector;
using namespace std;
class World
{
public:
	World(Renderer *renderer);
	~World();

	ViewPlane vp;
	Vector background;
	Tracer* tracer_ptr;
	vector<Object*> objects;
	
	void add_object(Object *o);
	void render_scene();
private:
	Renderer *renderer;
};

