#include "IntersectData.h"
IntersectData::IntersectData(World &w):
	hit_obj(false),
	world(w),
	hit_pt(),
	n(),
	material(),
	ray(),
	obj_id(-1),
	texture()
{}

IntersectData::IntersectData(const IntersectData &t): 
	hit_obj(t.hit_obj),
	hit_pt(t.hit_pt),
	world(t.world),
	n(t.n),
	material(t.material),
	ray(t.ray),
	obj_id(t.obj_id),
	texture(t.texture)
{}
IntersectData::~IntersectData(){}
