#include "ViewParams.h"
#include <string> 
#include <iostream>
#include <sstream> 
using namespace std;
ViewParams::ViewParams()
{
	//rotate.push_back(0.0f);
	rotate.push_back(0.0f);
	rotate.push_back(0.0f);
	rotate.push_back(0.0f);

	translate.push_back(0.0f);
	translate.push_back(0.0f);
	translate.push_back(0.0f);

	translate.push_back(1.0f);
	translate.push_back(0.0f);
	translate.push_back(0.0f);

	translate.push_back(1.0f);
	translate.push_back(0.0f);
	translate.push_back(1.f);

	translate.push_back(0.0f);
	translate.push_back(0.0f);
	translate.push_back(1.0f);

	scale.push_back(1.0f);
	scale.push_back(1.0f);
	scale.push_back(1.0f);

	//eye.push_back(1.5f);
	//eye.push_back(3.0f);
	//eye.push_back(5.0f);

	eye.push_back(1.5f);
	eye.push_back(6.0f);
	eye.push_back(5.0f);

	look.push_back(0.0f);
	//look.push_back(1.0f);
	look.push_back(0.0f);
	look.push_back(0.0f);

	up.push_back(0.0f);
	up.push_back(1.0f);
	up.push_back(0.2f);
}


ViewParams::~ViewParams()
{
}

void ViewParams::link_translate(int program)
{
	int transLoc = glGetUniformLocation(program, "trans");

	glUniform3fv(transLoc, 1, &translate[0]);
}

void ViewParams::transform(int program)
{
	int thetaLoc = glGetUniformLocation(program, "theta");
	int scaleLoc = glGetUniformLocation(program, "scale");

	glUniform3fv(thetaLoc, 1, &rotate[0]);
	glUniform3fv(scaleLoc, 1, &scale[0]);

	for (GLuint i = 0; i < 4; ++i){
		stringstream ss;
		string index;
		ss << i;
		index = ss.str();
		int transLoc = glGetUniformLocation(program, ("translates["+index+"]").c_str());
		cout << transLoc << endl;
		glUniform3fv(transLoc, 1, &translate[i * 3]);
	}
}


void ViewParams::camera(int program)
{
	int posLoc = glGetUniformLocation(program, "cPosition");
	int lookLoc = glGetUniformLocation(program, "cLookAt");
	int upVecLoc = glGetUniformLocation(program, "cUp");

	glUniform3fv(posLoc, 1, &eye[0]);
	glUniform3fv(lookLoc, 1, &look[0]);
	glUniform3fv(upVecLoc, 1, &up[0]);
}

void ViewParams::frustum(int program)
{
	int leftLoc = glGetUniformLocation(program, "left");
	int rightLoc = glGetUniformLocation(program, "right");
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
	for (int i = 0; i < scale.size(); ++i)
		scale[i] += s;

	int scaleLoc = glGetUniformLocation(program, "scale");
	glUniform3fv(scaleLoc, 1, &scale[0]);
	translate[3] += s;
	translate[6] += s;
	translate[8] += s;
	translate[11] += s;
	for (GLuint i = 0; i < 4; ++i){
		stringstream ss;
		string index;
		ss << i;
		index = ss.str();
		int transLoc = glGetUniformLocation(program, ("translates[" + index + "]").c_str());
		cout << transLoc << endl;
		glUniform3fv(transLoc, 1, &translate[i * 3]);
	}
}

void ViewParams::move_forward(int program, float s){
	eye[2] += s;
	look[2] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	int scaleLoc = glGetUniformLocation(program, "cPosition");
	glUniform3fv(scaleLoc, 1, &eye[0]);
	glUniform3fv(lookLoc, 1, &look[0]);
}
void ViewParams::move_up(int program, float s){
	eye[1] += s;
	look[1] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	int scaleLoc = glGetUniformLocation(program, "cPosition");
	glUniform3fv(scaleLoc, 1, &eye[0]);
	glUniform3fv(lookLoc, 1, &look[0]);
}
void ViewParams::move_side(int program, float s){
	eye[0] += s;
	look[0] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	int scaleLoc = glGetUniformLocation(program, "cPosition");
	glUniform3fv(scaleLoc, 1, &eye[0]);
	glUniform3fv(lookLoc, 1, &look[0]);
}

void ViewParams::look_up(int program, float s){
	look[1] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	glUniform3fv(lookLoc, 1, &look[0]);
}
void ViewParams::look_side(int program, float s){
	look[0] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	glUniform3fv(lookLoc, 1, &look[0]);
}
void ViewParams::look_far(int program, float s){
	look[2] += s;

	int lookLoc = glGetUniformLocation(program, "cLookAt");
	glUniform3fv(lookLoc, 1, &look[0]);
}
void ViewParams::add_site(int program, float s){
	int farLoc = glGetUniformLocation(program, "far");
	far_c += s;
	glUniform1f(farLoc, far_c);
}