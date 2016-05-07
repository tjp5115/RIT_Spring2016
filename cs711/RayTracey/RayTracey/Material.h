#pragma once
#include "World.h"
#include "RGBColor.h"
#include "IntersectData.h"
#include "Light.h"
class Material
{
public:
	Material(){};

	virtual RGBColor get_illumination(Light &light, const IntersectData &id, unsigned int depth){ return 0; };
	virtual RGBColor local_illumination(Light &light, const IntersectData &id){ return 0; };
	virtual RGBColor get_ambient(const IntersectData &id){return 0;};
};

