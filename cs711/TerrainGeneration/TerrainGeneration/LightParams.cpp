#include "LightParams.h"
#include <iostream>
using namespace std;
LightParams::LightParams(RGBColor _Lx, RGBColor _ambient, Point3D _pos) :
	Lx(_Lx),
	ambient(_ambient),
	pos(_pos)
{
}


LightParams::~LightParams()
{
}

void LightParams::addPhong(RGBColor _Ax, float _Ka, RGBColor _Dx, float _Kd, RGBColor _Sx, float _n, float _Ks){
	Ax = RGBColor(_Ax);
	Dx = RGBColor(_Dx);
	Sx = RGBColor(_Sx);

	Ka = _Ka;
	Kd = _Kd;
	Ks = _Ks;

	n = _n;
}

void LightParams::setUpPhong(int program){
	int location = glGetUniformLocation(program, "a_color");
	glUniform4f(location, ambient.r, ambient.g, ambient.b, 1.0);
	//cout << location << endl;

	location = glGetUniformLocation(program, "Ax");
	glUniform4f(location, Ax.r, Ax.g, Ax.b, 1.0);
	//cout << location << endl;

	location = glGetUniformLocation(program, "Ka");
	glUniform1f(location, Ka);
	//cout << Ka << endl;
	//cout << location << endl;

	location = glGetUniformLocation(program, "Dx");
	glUniform4f(location, Dx.r, Dx.g, Dx.b, 1.0);
	//cout << location << endl;

	location = glGetUniformLocation(program, "Kd");
	glUniform1f(location, Kd);
	//cout << Kd << endl;
	//cout << location << endl;

	location = glGetUniformLocation(program, "Sx");
	glUniform4f(location, Sx.r, Sx.g, Sx.b, 1.0);
	//cout << location << endl;

	location = glGetUniformLocation(program, "n");
	glUniform1f(location, n);
	//cout << n << endl;
	//cout << location << endl;

	location = glGetUniformLocation(program, "Ks");
	glUniform1f(location, Ks);
	//cout << Ks << endl;
	//cout << location << endl;

	location = glGetUniformLocation(program, "Lx");
	glUniform4f(location, Lx.r, Lx.g, Lx.b, 1.0);
	//cout << location << endl;

	location = glGetUniformLocation(program, "ls_position");
	glUniform4f(location, pos.x, pos.y, pos.z, 1.0);
	//cout << location << endl;
}