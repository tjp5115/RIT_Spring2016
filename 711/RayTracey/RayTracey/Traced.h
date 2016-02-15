#pragma once
class World;

#include "cmatrix"
typedef std::valarray<double> Vector;
class Traced
{
public:
	Traced(World &world);
	Traced(const Traced &traced);
	~Traced();

	bool hit_obj;
	Vector hit_pt;
	Vector n;
	World &world;
};

