#pragma once
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#if defined(__APPLE__)
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
#include "RGBColor.h"
using namespace std;

struct Vertex
{
	GLfloat x, y, red, green, blue;
};


class Renderer 
{
protected:
	static Renderer * instance;
public:
	Renderer(int width, int height, int *argcp, char **argv);
	~Renderer();
	void add_pixel(int x, int y, double r, double g, double b);
	void init(RGBColor background);
	int w;
	int h;
private:
	static void displayWrapper();
	void setInstance(Renderer *r);
	virtual void display();
	vector<Vertex> vertices;
	int *argcp;
	char **argv;
};

