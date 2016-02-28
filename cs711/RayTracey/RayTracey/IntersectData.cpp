#include "IntersectData.h"
IntersectData::IntersectData(World &w):
	hit_obj(false),
	world(w),
	hit_pt(),
	n(),
	material(),
	ray()
{}


IntersectData::IntersectData(const IntersectData &t): 
	hit_obj(t.hit_obj),
	hit_pt(t.hit_pt),
	world(t.world),
	color(t.color),
	n(t.n),
	material(t.material),
	ray(t.ray)
{}
IntersectData::~IntersectData(){}
