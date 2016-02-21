#pragma once
class World;
#include "Ray.h"
#include "Traced.h"
#include "cmatrix"
typedef std::valarray<double> Color;
class Tracer
{
public:
	Tracer(World* world);
	~Tracer();

	Color trace_ray(const Ray &ray) const;
private:
	World *world_ptr;

};

