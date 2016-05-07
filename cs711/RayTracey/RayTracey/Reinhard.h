#pragma once
#include "ToneReproduction.h"
/**
 * @brief Reinhard implementation of Tone Reproduction
 */
class Reinhard:
	public ToneReproduction
{
public:
	Reinhard() : ToneReproduction(){};
	Reinhard(double _L_max, double _L_dmax) : ToneReproduction(_L_max, _L_dmax){};

	virtual RGBColor get_tone(RGBColor color, double L_wa);
	virtual RGBColor adjust_color(RGBColor color){ return color * L_max; }
private:
};
