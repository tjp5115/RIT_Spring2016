#include "World.h"
#include <iostream>
using namespace std;

World::World(Renderer *renderer_p){

	renderer = renderer_p;
	background = Vector(1,3);
	tracer_ptr = new Tracer(this);

	//default camera
	camera.lookat = Vector(3);
	double u[] = { 0, 1, 0 };
	camera.up = Vector(u,3);
	double e[] = { 0, 0, 500 };
	camera.eye = Vector(e,3);
	camera.d = 200;
	compute_uvw();
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
	ray.d = Vector(3);
	Vector pixel = Vector(2);
	int h = renderer->h;
	int w = renderer->w;
	float s = 1.0f;

	ray.o = camera.eye[0];

	for (int r = 0; r < h; r++)				
		for (int c = 0; c < w; c++) {
			pixel[0] = s * (c - 0.5 * w);
			pixel[1] = s * (r - 0.5 * h);
			ray.d = get_camera_direction(pixel);
			color = tracer_ptr->trace_ray(ray);
			renderer->add_pixel(c, r, color[0], color[1], color[2]);
			//cout << color[0] << " " << color[1] << " " << color[2] << endl;
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

void World::compute_uvw(){
	camera.n = normalize(camera.eye - camera.lookat);
	camera.u = normalize(cross_product_3x1(camera.up,camera.n));
	camera.v = cross_product_3x1(camera.n,camera.u);
}

Vector World::cross_product_3x1(const Vector a, const Vector b){
	Vector cp(3);

	cp[0] = a[1] * b[2] - a[2] * b[1];
	cp[1] = a[2] * b[0] - a[0] * b[2];
	cp[2] = a[0] * b[1] - a[1] * b[0];
	return cp;
}

Vector World::get_camera_direction(Vector pixel){
	Vector v = pixel[0] * camera.u + pixel[1] * camera.v - camera.d * camera.n;
	return normalize(v);
}
void World::set_camera(Vector eye, Vector lookat, Vector up){
	camera.eye = eye;
	camera.lookat = lookat;
	camera.up= up;
}
Vector World::normalize(const Vector v){
	double length = sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
	Vector n(3);
	n[0] = v[0] / length;
	n[1] = v[1] / length;
	n[2] = v[2] / length;
	return n;
}