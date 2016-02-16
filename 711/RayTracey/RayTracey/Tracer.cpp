#pragma once
#include "Tracer.h"
#include "World.h"
#include <iostream>
using namespace std;
Tracer::Tracer(World *w) :
	world_ptr(w)
{}

Tracer::~Tracer()
{
}
Vector Tracer::trace_ray(const Ray &ray) const {
	Traced trace(world_ptr->hit_objects(ray));
	if (trace.hit_obj){
		//cout << " hit obj" << endl;
		return trace.color;
	}
	//cout << trace.color[0] << " " << trace.color[1] << " " << trace.color[2] << endl;
	//cout << "miss" << endl;
	return world_ptr->background;
}
