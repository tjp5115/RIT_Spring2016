#pragma once
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#if defined(__APPLE__)
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
#include "cmatrix"
typedef std::valarray<double> Vector;
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
	void init(Vector background);
	int w;
	int h;
private:
	static void displayWrapper();
	void setInstance(Renderer *r);
	virtual void display();
	vector<Vertex> vertices;
	//vector<int> vertex1;
	//vector<float> color1;


	int *argcp;
	char **argv;
};

