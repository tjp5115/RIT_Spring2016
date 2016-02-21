#include "World.h"
#include <iostream>
using namespace std;

World::World(Renderer *renderer_p){

	renderer = renderer_p;
	background = Vector(1,3);
	tracer_ptr = new Tracer(this);

	//default camera
	camera.lookat = Point3D(0,0,0);
	camera.up = Vector3D(0,1,0);
	camera.eye = Point3D(0,0,500);
	camera.vp_distance = 200;
	compute_uvw();
}


World::~World()
{
}

void World::add_object(Object *o){
	objects.push_back(o);
}

void World::render_scene(){

	Color color;
	Ray ray;
	double pixel[2];
	int h = renderer->h;
	int w = renderer->w;

	ray.o = camera.eye;
	
	for (int r = 0; r < h; r++)				
		for (int c = 0; c < w; c++) {
			pixel[0] = (c - 0.5 * w + 0.5);
			pixel[1] = (r - 0.5 * h + 0.5);
			ray.d = get_camera_direction(pixel);
			color = tracer_ptr->trace_ray(ray);
			renderer->add_pixel(c, r, color[0], color[1], color[2]);
		}
	renderer->init(background);
}
Traced World::hit_objects(const Ray &ray){
	Traced trace(*this);
	double w;
	Normal normal;
	double w_min = BIG_NUMBER;
	Point3D hit;

	for (int i = 0; i < objects.size(); ++i){
		if (objects[i]->hit(ray, w, trace) && (w < w_min)){
			w_min = w;
			trace.color = objects[i]->color;
			trace.hit_obj = true;
			hit = trace.hit_pt;
			//cout << "hit" << endl; 
		}
	}
	if (trace.hit_obj){
		trace.hit_pt = hit;
		trace.n = normal;
	}
	return trace;
}

void World::compute_uvw(){
	camera.n = camera.eye - camera.lookat;
	camera.n.normalize();
	camera.u = camera.up ^ camera.n;
	camera.u.normalize();
	camera.v = camera.n ^ camera.u;
}


Vector3D World::get_camera_direction(double pixel[]){
	Vector3D v = pixel[0] * camera.u + pixel[1] * camera.v -camera.vp_distance * camera.n;
	v.normalize();
	return v;
}

void World::set_camera(Point3D e, Point3D l, Vector3D up, double vp_dist){
	camera.eye = e;
	camera.lookat = l;
	camera.up = up;
	camera.vp_distance = vp_dist;
	compute_uvw();
}