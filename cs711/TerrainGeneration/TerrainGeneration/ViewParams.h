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
	float left = -1.0f;
	float right = 1.0f;
	float top = 1.0f;
	float bottom = -1.0f;
	float near_c = 3.0f;
	float far_c = 100.5f; 
	void camera(int program);
	void transform(int program);
	void frustum(int program);
};

