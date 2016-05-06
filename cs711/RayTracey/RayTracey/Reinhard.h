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
	Reinhard(double _L_max) : ToneReproduction(_L_max){};
	~Reinhard() {};

	virtual RGBColor get_tone(RGBColor color, double L_wa);
private:
};
