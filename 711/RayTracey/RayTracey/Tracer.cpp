#pragma once
#include "Tracer.h"

Tracer::Tracer(World *w) :
	world_ptr(w)
{}


Tracer::~Tracer()
{
}
Vector Tracer::trace_ray(const Ray &ray){
	return Vector();
}
