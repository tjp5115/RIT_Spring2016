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
	void set_color(const RGBColor c){ color = c; }
	RGBColor color;
	Material *material;
protected:
};

