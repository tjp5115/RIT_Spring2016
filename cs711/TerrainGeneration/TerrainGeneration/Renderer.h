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
#include "LightParams.h"

struct Vertex
{
	GLfloat x, y, z, a;
};


class Renderer 
{
protected:
	static Renderer * instance;
public:
	Renderer(int width, int height, int *argcp, char **argv, ViewParams *vp, LightParams *lp);
	~Renderer();
	void add_point(float x, float y, float z);
	void add_normal(float x, float y, float z);
	void add_tex(float u, float v);
	void draw();
	GLuint getProgram(){ return program; };
	double getMinY(){ return minY; };
	double getMaxY(){ return maxY; };
	int w;
	int h;
private:
	void init();
	double minY, maxY;
	static void displayWrapper();
	void setInstance(Renderer *r);
	virtual void display();
	vector<float> vertices;
	vector<float> normals;
	vector<float> tex;
	int *argcp;
	char **argv;
	GLuint program, vbuffer, ebuffer;
	ViewParams *vp;
	LightParams *lp;
};

