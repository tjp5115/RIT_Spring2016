#pragma once
#include "ToneReproduction.h"
class Ward :
	public ToneReproduction
{
public:
	Ward() : ToneReproduction(){};
	Ward(double _L_max, double _L_dmax) : ToneReproduction(_L_max, _L_dmax), sf(0.0){};

	virtual RGBColor get_tone(RGBColor color, double L_wa);
	virtual RGBColor adjust_color(RGBColor color){ return color* L_max; }
private:
	double sf;
};

