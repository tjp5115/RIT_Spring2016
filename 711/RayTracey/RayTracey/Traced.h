#pragma once
class World;
#include "Point3D.h"
#include "Normal.h"
#include "cmatrix"
typedef std::valarray<double> Color;
class Traced
{
public:
	Traced(World &world);
	Traced(const Traced &traced);
	~Traced();

	Normal n;
	bool hit_obj;
	Point3D hit_pt;
	World &world;
	double w;
	Color color;
};

