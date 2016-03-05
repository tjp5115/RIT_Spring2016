#pragma once
#include "World.h"
#include "RGBColor.h"
#include "IntersectData.h"
#include "Light.h"
class Material
{
public:
	Material();
	~Material();

	virtual RGBColor get_illumination(const Light &light, const IntersectData &id) const = 0 ;
	virtual RGBColor get_ambient() const = 0;
};

