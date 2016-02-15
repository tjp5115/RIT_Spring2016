#pragma once
#include "cmatrix"
typedef std::valarray<double> Vector;
class Ray
{
public:
	Ray();
	Ray(const Vector &origin, const Vector &direction);
	~Ray();

	Vector o;
	Vector d;

};

