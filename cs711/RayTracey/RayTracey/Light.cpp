#include "Light.h"
#include "World.h"
Light::Light() :
position(0, 0, 0),
color(0)
{
}
Light::Light(Point3D _position, RGBColor _color) :
position(_position),
color(_color)
{}


/**
 * @details Checks to see if a ray is in the shadow.
 * 
 * @param r shadow ray
 * @param id date for the ray
 * 
 * @return if in a shadow or not.
 */
bool Light::in_shadow(const Ray &r, const IntersectData &id){
	double t;
	float d = position.distance(r.o);
	for (int i = 0; i < id.world.objects.size(); ++i){
		if (id.world.objects[i]->shadow_hit(r, t) && t < d){
			return true;
		}
	}
	return false;
}