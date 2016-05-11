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
	vector<float> rotate;
	vector<float> translate;
	vector<float> scale;

	// current view values
	vector<float> eye;
	vector<float> look;
	vector<float> up;

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
	void link_translate(int program);
	void frustum(int program);
	void increase_scale(int program, float s);
	void move_forward(int program, float s);
	void move_side(int program, float s);
	void move_up(int program, float s);
	void look_up(int program, float s);
	void look_side(int program, float s);
	void look_far(int program, float s);
	void add_site(int program, float s);
};

