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
using namespace std;

#include "ViewParams.h"

struct Vertex
{
	GLfloat x, y, z, a;
};


class Renderer 
{
protected:
	static Renderer * instance;
public:
	Renderer(int width, int height, int *argcp, char **argv, ViewParams *vp);
	~Renderer();
	void add_point(float x, float y, float z);
	void draw();
	int w;
	int h;
private:
	static void displayWrapper();
	void setInstance(Renderer *r);
	virtual void display();
	vector<float> vertices;
	int *argcp;
	char **argv;
	GLuint program, vbuffer, ebuffer;
	ViewParams *vp;
};

