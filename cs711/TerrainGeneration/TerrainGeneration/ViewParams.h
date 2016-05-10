#pragma once

#include <stdio.h>
#include <stdlib.h>
#include <vector>
#if defined(__APPLE__)
#include <GLUT/glut.h>
#else
#include <GL/glew.h>
#include <GL/glut.h>
#include <GL/gl.h>
#endif

#include <vector>
using namespace std;
class ViewParams
{
public:
	ViewParams();
	~ViewParams();
	// current values for transformations
	vector<float> rotateDefault;
	vector<float> translateDefault;
	vector<float> scaleDefault;

	// current view values
	vector<float> eyeDefault;
	vector<float> lookDefault;
	vector<float> upDefault;

	// clipping window boundaries
	float left = -2.0f;
	//float left = -1.0f;
	float right = 2.0f;
	//float right = 1.0f;
	float top = 2.0f;
	//float top = 1.0f;
	float bottom = -2.0f;
	//float bottom = -1.0f;
	float near_c = 3.0f;
	float far_c = 70.0f;
	void camera(int program);
	void transform(int program);
	void frustum(int program);
	void increase_scale(int program, float s);
	void move_forward(int program, float s);
	void move_side(int program, float s);
	void move_up(int program, float s);
	void look_up(int program, float s);
	void look_side(int program, float s);
	void look_far(int program, float s);
};

