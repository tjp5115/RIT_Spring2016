#include "ViewParams.h"


ViewParams::ViewParams()
{
	//rotateDefault.push_back(0.0f);
	rotateDefault.push_back(0.0f);
	rotateDefault.push_back(0.0f);
	rotateDefault.push_back(0.0f);

	//translateDefault.push_back(-0.5f);
	translateDefault.push_back(-1.0f);
	//translateDefault.push_back(0.5f);
	translateDefault.push_back(0.0f);
	translateDefault.push_back(0);

	scaleDefault.push_back(1.0f);
	scaleDefault.push_back(1.0f);
	scaleDefault.push_back(1.0f);

	//eyeDefault.push_back(1.5f);
	//eyeDefault.push_back(3.0f);
	//eyeDefault.push_back(5.0f);

	eyeDefault.push_back(1.5f);
	eyeDefault.push_back(6.0f);
	eyeDefault.push_back(5.0f);

	lookDefault.push_back(0.0f);
	//lookDefault.push_back(1.0f);
	lookDefault.push_back(0.0f);
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

void ViewParams::increase_scale(int program, float s){
	for (int i = 0; i < scaleDefault.size(); ++i)
		scaleDefault[i] += s;

	int scaleLoc = glGetUniformLocation(program, "scale");
	glUniform3fv(scaleLoc, 1, &scaleDefault[0]);
}

void ViewParams::move_forward(int program, float s){
	eyeDefault[2] += s;
	lookDefault[2] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	int scaleLoc = glGetUniformLocation(program, "cPosition");
	glUniform3fv(scaleLoc, 1, &eyeDefault[0]);
	glUniform3fv(lookLoc, 1, &lookDefault[0]);
}
void ViewParams::move_up(int program, float s){
	eyeDefault[1] += s;
	lookDefault[1] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	int scaleLoc = glGetUniformLocation(program, "cPosition");
	glUniform3fv(scaleLoc, 1, &eyeDefault[0]);
	glUniform3fv(lookLoc, 1, &lookDefault[0]);
}
void ViewParams::move_side(int program, float s){
	eyeDefault[0] += s;
	lookDefault[0] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	int scaleLoc = glGetUniformLocation(program, "cPosition");
	glUniform3fv(scaleLoc, 1, &eyeDefault[0]);
	glUniform3fv(lookLoc, 1, &lookDefault[0]);
}

void ViewParams::look_up(int program, float s){
	lookDefault[1] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	glUniform3fv(lookLoc, 1, &lookDefault[0]);
}
void ViewParams::look_side(int program, float s){
	lookDefault[0] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	glUniform3fv(lookLoc, 1, &lookDefault[0]);
}
void ViewParams::look_far(int program, float s){
	lookDefault[2] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	glUniform3fv(lookLoc, 1, &lookDefault[0]);
}