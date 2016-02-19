#include "Traced.h"
Traced::Traced(World &w):
	hit_obj(false),
	world(w),
	hit_pt(),
	n()
{}


Traced::Traced(const Traced &t): 
	hit_obj(t.hit_obj),
	hit_pt(t.hit_pt),
	world(t.world),
	color(t.color),
	n(t.n)
{}
Traced::~Traced(){}
