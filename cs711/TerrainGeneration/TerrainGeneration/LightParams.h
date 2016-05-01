#pragma once
#include "Point3D.h"
#include "RGBColor.h"

#if defined(__APPLE__)
#include <GLUT/glut.h>
#else
#include <GL/glew.h>
#include <GL/glut.h>
#include <GL/gl.h>
#endif
class LightParams
{
public:
	LightParams(RGBColor _Lx, RGBColor _ambient, Point3D _pos);
	~LightParams();
	void addPhong(RGBColor Ax, float Ka, RGBColor Dx, float Kd, RGBColor Sx, float n, float Ks);
	void setUpPhong(int program);
private:
	// Material properties
	RGBColor Ax, Dx, Sx;
	float Ka, Kd, n, Ks;

	// Light source properties
	RGBColor Lx, ambient;
	Point3D pos;

};

