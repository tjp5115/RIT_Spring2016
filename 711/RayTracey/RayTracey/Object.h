#pragma once
#include "Traced.h"
#include "Ray.h"
#include "cmatrix"
typedef std::valarray<double> Vector;

class Object
{
public:
	Object();
	~Object();
	virtual bool hit(const Ray &r, double &t, Traced &tr) const = 0;
	void set_color(const Vector c){ color = c; }
	Vector color;
protected:
};

