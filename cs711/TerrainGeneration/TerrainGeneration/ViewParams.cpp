#include "ViewParams.h"


ViewParams::ViewParams()
{
	rotateDefault.push_back(0.0f);
	rotateDefault.push_back(0.0f);
	rotateDefault.push_back(0.0f);

	translateDefault.push_back(-.4f);
	translateDefault.push_back(1.0f);
	translateDefault.push_back(0.0f);

	scaleDefault.push_back(1.5f);
	scaleDefault.push_back(1.5f);
	scaleDefault.push_back(1.5f);

	eyeDefault.push_back(1.5f);
	eyeDefault.push_back(3.0f);
	eyeDefault.push_back(5.0f);

	lookDefault.push_back(0.0f);
	lookDefault.push_back(1.0f);
	lookDefault.push_back(0.0f);

	upDefault.push_back(0.0f);
	upDefault.push_back(1.0f);
	upDefault.push_back(0.2f);
}


ViewParams::~ViewParams()
{
}


void ViewParams::transform(int program)
{
	int thetaLoc = glGetUniformLocation(program, "theta");
	int transLoc = glGetUniformLocation(program, "trans");
	int scaleLoc = glGetUniformLocation(program, "scale");

	glUniform3fv(thetaLoc, 1, &rotateDefault[0]);
	glUniform3fv(transLoc, 1, &translateDefault[0]);
	glUniform3fv(scaleLoc, 1, &scaleDefault[0]);
}

void ViewParams::camera(int program)
{
	int posLoc = glGetUniformLocation(program, "cPosition");
	int lookLoc = glGetUniformLocation(program, "cLookAt");
	int upVecLoc = glGetUniformLocation(program, "cUp");

	glUniform3fv(posLoc, 1, &eyeDefault[0]);
	glUniform3fv(lookLoc, 1, &lookDefault[0]);
	glUniform3fv(upVecLoc, 1, &upDefault[0]);
}

void ViewParams::frustum(int program)
{
	int leftLoc = glGetUniformLocation(program, "left");
	int rightLoc =glGetUniformLocation(program, "right");
	int topLoc = glGetUniformLocation(program, "top");
	int bottomLoc = glGetUniformLocation(program, "bottom");
	int nearLoc = glGetUniformLocation(program, "near");
	int farLoc = glGetUniformLocation(program, "far");

	glUniform1f(leftLoc, left);
	glUniform1f(rightLoc, right);
	glUniform1f(topLoc, top);
	glUniform1f(bottomLoc, bottom);
	glUniform1f(nearLoc, near_c);
	glUniform1f(farLoc, far_c);
}