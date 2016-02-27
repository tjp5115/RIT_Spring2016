#include "Light.h"


Light::Light() :
position(0, 0, 0),
color(3)
{
}
Light::Light(Point3D _position, RGBColor _color) :
position(_position),
color(_color)
{}

Light::~Light()
{
}
