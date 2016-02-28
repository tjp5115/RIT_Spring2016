#pragma once
#include "IntersectData.h"
#include "Ray.h"
#include "Material.h";
#include "RGBColor.h"

class Object
{
public:
	Object();
	~Object();
	virtual bool hit(const Ray &r, double &t, IntersectData &tr) const = 0;
	Material *material;
	virtual void set_material(Material *m){ material = m; };
protected:
};

