#include "Traced.h"
Traced::Traced(World &w):
	hit_obj(false),
	world(w),
	hit_pt(3,1),
	n(3,1)
{}


Traced::Traced(const Traced &t): 
	hit_obj(t.hit_obj),
	hit_pt(t.hit_pt),
	world(t.world),
	n(t.n),
	color(t.color)
{}
Traced::~Traced(){}
