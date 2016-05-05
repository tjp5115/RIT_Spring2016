#pragma once
#include "RGBColor.h"
#include <cmath>
class ToneReproduction
{
public:
	ToneReproduction();
	ToneReproduction(double _L_max);
	~ToneReproduction();
	virtual RGBColor get_tone(RGBColor color, double L_wa){ return color; };
	double get_L(RGBColor color){ return 0.27 * color.r + 0.67 * color.g + 0.06 * color.b; };
protected:
	double L_max;

};