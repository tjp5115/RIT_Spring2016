#pragma once
#include "Traced.h"
#include "Ray.h"
#include "cmatrix"
typedef std::valarray<double> Color;

class Object
{
public:
	Object();
	~Object();
	virtual bool hit(const Ray &r, double &t, Traced &tr) const = 0;
	void set_color(const Color c){ color = c; }
	Color color;
protected:
};

