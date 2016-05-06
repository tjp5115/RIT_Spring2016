#pragma once
#include "IntersectData.h"
#include "Ray.h"
#include "RGBColor.h"
class Material;
class Object
{
public:
	Object() {};
	virtual bool hit(const Ray &r, double &t, IntersectData &tr) const = 0;
	virtual bool shadow_hit(const Ray &r, double &t) const = 0;
	Material *material;
	virtual void set_material(Material *m){ material = m; };
protected:
};

