#pragma once
class World;
class Material;
#include "Point3D.h"
#include "Normal.h"
#include "RGBColor.h"
#include "Ray.h"

/**
 * @brief Class to hold data throughout the process of a ray being traced.
 */
class IntersectData
{
public:
	IntersectData(World &world):
	hit_obj(false),
	world(w),
	hit_pt(),
	n(),
	material(),
	ray(),
	obj_id(-1),
	texture()
{};
	IntersectData();
	IntersectData(const IntersectData &IntersectData): 
	hit_obj(t.hit_obj),
	hit_pt(t.hit_pt),
	world(t.world),
	n(t.n),
	material(t.material),
	ray(t.ray),
	obj_id(t.obj_id),
	texture(t.texture)
{};
	~IntersectData();
	Normal n;
	bool hit_obj;
	Point3D hit_pt;
	World &world;
	double w;
	Ray ray;
	Material *material;
	int obj_id;
	Point3D  texture;
};

