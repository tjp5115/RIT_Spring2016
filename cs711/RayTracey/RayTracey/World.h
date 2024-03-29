#pragma once
#include <vector>
#include <iostream>

#include "RGBColor.h"
#include "Object.h"
#include "Ray.h"

#include "Light.h"
#include "ToneReproduction.h"

#include "Renderer.h"

using namespace std;

struct Camera 
{
	Point3D lookat, eye;
	Vector3D up, n, u, v;
	double vp_distance; //view plane distance
};

class World
{
public:
	World(Renderer *renderer);

	RGBColor background;
	vector<Object*> objects;
	
	void add_object(Object *o);
	void render_scene();
	IntersectData hit_objects(const Ray &ray,int obj_id);
	IntersectData hit_objects(const Ray &ray);
	void set_camera(Point3D l, Point3D e, Vector3D up, double vp_dist);
	void set_tone_reproduction(ToneReproduction *_tone){ tone = _tone; };
	void add_light(Light *l){ lights.push_back(l); };
	unsigned int DEPTH;
private:
	ToneReproduction *tone;
	vector<Light*> lights;
	Renderer *renderer;
	const double BIG_NUMBER = 1.0E10;
	void compute_uvw(void);
	Camera camera;
	Vector3D get_camera_direction(double pixel[]);

	RGBColor trace_ray(const Ray &ray) ;
	RGBColor black;
};

