#include "Reinhard.h"
#include <iostream>
using namespace std;

/**
 * @brief get the tone of the color.
 */
RGBColor Reinhard::get_tone(RGBColor color, double L_wa){
	//cout << L_max << endl;
	RGBColor scaled(
		0.18*color.r / L_wa,
		0.18*color.g / L_wa, 
		0.18*color.b / L_wa);
	return RGBColor(
		scaled.r / (scaled.r + 1),
		scaled.g / (scaled.g + 1), 
		scaled.b / (scaled.b + 1));
}