#include "World.h"
#include <iostream>
using namespace std;

World::World(Renderer *renderer_p){

	renderer = renderer_p;
	background = Vector(1,3);
	tracer_ptr = new Tracer(this);
}


World::~World()
{
}

void World::add_object(Object *o){
	objects.push_back(o);
}

void World::render_scene(){

	Vector color;
	Ray ray;
	double d[] = { 0.0, 0.0, -1.0 };
	ray.d = Vector(d,3);
	int h = renderer->h;
	int w = renderer->w;
	float s = 1.0f;
	for (int r = 0; r < h; r++)				
		for (int c = 0; c <= w; c++) {
			double o[3];
			//o[0] = s * (c - w / 2.0 + 0.5);
			//o[1] = s * (r - h / 2.0 + 0.5);
			o[0] = s * (c - 0.5 * (w  - 1.0));
			o[1] = s * (r - 0.5 * (h  - 1.0));
			o[2] = 100.0;
			ray.o = Vector(o, 3);
			color = tracer_ptr->trace_ray(ray);
			renderer->add_pixel(r, c, color[0], color[1], color[2]);
		//	cout << color[0] << " " << color[1] << " " << color[2] << endl;
			//cout << ray.o[0] << ray.o[1] << ray.o[2] << endl;
		}
	renderer->init(background);
}
Traced World::hit_objects(const Ray &ray){
	Traced trace(*this);
	double w;
	double w_min = BIG_NUMBER;


	for (int i = 0; i < objects.size(); ++i){
		if (objects[i]->hit(ray, w, trace) && (w < w_min)){
			w_min = w;
			trace.color = objects[i]->color;
			trace.hit_obj = true;

			//cout << "hit obj"<< endl;
		//	cout << trace.color[0] << " " << trace.color[1] << " " << trace.color[2] << endl;
		}
		else{
			//cout << w << " " << w_min << " " << trace.hit_obj << endl;
		}
	}

	return trace;
}

