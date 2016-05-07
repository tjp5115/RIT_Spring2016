#include "Ward.h"
#include <iostream>
using namespace std;
/**
 * @brief get the Tone Reproduces color using ward.
 */
RGBColor Ward::get_tone(RGBColor color, double L_wa){
	//cout << L_max << endl;
	//cout << "L_dmax : " << L_dmax << endl;
	if (sf == 0.0){
		double numer = 1.219 + std::pow(L_dmax / 2.0, 0.4);
		double denom = 1.219 + std::pow(L_wa, 0.4);
		sf = std::pow(numer / denom, 2.5);
	}
	//cout << sf << endl;
	return RGBColor(color.r*sf/L_dmax, color.g*sf/L_dmax, color.b*sf/L_dmax);
}
