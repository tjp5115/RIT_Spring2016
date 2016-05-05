#pragma once
#include "ToneReproduction.h"
class Ward :
	public ToneReproduction
{
public:
	Ward() : ToneReproduction(){};
	Ward(double _L_max) : ToneReproduction(_L_max){};
	~Ward();

	virtual RGBColor get_tone(RGBColor color, double L_wa);
private:
};

