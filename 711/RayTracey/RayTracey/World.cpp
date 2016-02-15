#include "World.h"


World::World(Renderer *renderer_p) :
	renderer(renderer_p)
{}


World::~World()
{
}

void World::add_object(Object *o){
	objects.push_back(o);
}

void World::render_scene(){
	Vector color;
	Ray ray;
	int h = vp.h;
	int w = vp.w;
	float s = vp.pixel_size;
	for (int x = 0; x < w; x++)			
		for (int y = 0; y <= h; y++) {	
			double r[3];
			r[0] = s * (y - h / 2.0 + 0.5);
			r[1] = s * (x - w / 2.0 + 0.5);
			r[2] = 100.0;
			ray.o = Vector(r, 3);
			color = tracer_ptr->trace_ray(ray);
			renderer->add_pixel(x, y, color);
		}

}

