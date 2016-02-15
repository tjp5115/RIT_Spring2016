#pragma once
class World;
#include "Ray.h"
#include "cmatrix"
typedef std::valarray<double> Vector;
class Tracer
{
public:
	Tracer(World* world);
	~Tracer();

	Vector trace_ray(const Ray &ray);
private:
	World *world_ptr;

};

