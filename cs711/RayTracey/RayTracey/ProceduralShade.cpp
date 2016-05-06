#include "ProceduralShade.h"
#include <stdlib.h>

ProceduralShade::ProceduralShade(RGBColor _same_color, RGBColor _diff_color, float _square_size, RGBColor _Ax, float _Ka, float _Kd, RGBColor _Sx, float _n, float _Ks) :
PhongMaterial(_Ax, _Ka, _same_color, _Kd, _Sx, _n, _Ks),
same_color(_same_color),
diff_color(_diff_color),
square_size(_square_size)
{
}

/**
 * @brief get the illumination of a point.
 */
RGBColor ProceduralShade::get_illumination(Light &light, const IntersectData &id, unsigned int depth){
	set_color(id);
	return PhongMaterial::get_illumination(light, id, depth);
}

/**
 * @brief get the ambient color of a point
 */
RGBColor ProceduralShade::get_ambient(const IntersectData &id){
	set_color(id);
	return PhongMaterial::get_ambient(id);
}

/**
 * @brief finds what color the diffuse and ambient component should be at a point.
 */
void ProceduralShade::set_color(const IntersectData &id){
	int r = (int)(id.texture.x) / square_size;
	int c = (int)(id.texture.z) / square_size;

	if (r % 2 == 0 && c % 2 == 0)
		Dx = Ax = same_color;
	else if (r % 2 == 1 && c % 2 == 0)
		Dx = Ax = diff_color;
	else if (r % 2 == 0 && c % 2 == 1)
		Dx = Ax = diff_color;
	else
		Dx = Ax = same_color;
}

