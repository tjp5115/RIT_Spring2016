#pragma once
class World;
class Material;
#include "Point3D.h"
#include "Normal.h"
#include "RGBColor.h"
class IntersectData
{
public:
	IntersectData(World &world);
	IntersectData(const IntersectData &IntersectData);
	~IntersectData();

	Normal n;
	bool hit_obj;
	Point3D hit_pt;
	World &world;
	double w;
	RGBColor color;
	Material *material;
};

