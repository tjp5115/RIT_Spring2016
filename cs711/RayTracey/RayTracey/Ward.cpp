#include "Ward.h"
#include <iostream>
using namespace std;

RGBColor Ward::get_tone(RGBColor color, double L_wa){
	//cout << L_max << endl;
	double numer = 1.219 + std::pow(L_max / 2.0, 0.4);
	double denom = 1.219 + std::pow(L_wa, 0.4);
	double sf = std::pow(numer / denom, 2.5);
	return RGBColor(color.r*sf/L_max, color.g*sf/L_max, color.b*sf/L_max);
}