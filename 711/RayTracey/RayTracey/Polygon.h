#pragma once
#include "Object.h"
class Polygon :
	public Object
{
public:
	Polygon();
	~Polygon();

	virtual bool hit(const Ray &r, double &t, Traced &tr) const;
};

