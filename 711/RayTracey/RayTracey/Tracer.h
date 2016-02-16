#pragma once
#include "Ray.h"
class World;
#include "cmatrix"
typedef std::valarray<double> Vector;
class Tracer
{
public:
	Tracer(World* world);
	~Tracer();

	Vector trace_ray(const Ray &ray) const;
private:
	World *world_ptr;

};

