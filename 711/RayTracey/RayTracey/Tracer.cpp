#pragma once
#include "Tracer.h"
#include "World.h"
Tracer::Tracer(World *w) :
	world_ptr(w)
{}

Tracer::~Tracer()
{
}
Color Tracer::trace_ray(const Ray &ray) const {
	Traced trace(world_ptr->hit_objects(ray));
	if (trace.hit_obj){
		return trace.color;
	}
	return world_ptr->background;
}
