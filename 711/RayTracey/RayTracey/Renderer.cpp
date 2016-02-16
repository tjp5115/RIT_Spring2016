#include "Renderer.h"
int window;
#include <iostream>
using namespace std;
Renderer::Renderer(int width, int height, int *_argcp, char **_argv)
{
	w = width;                 
	h = height;
	setInstance(this);
	argcp = _argcp;
	argv = _argv;

	/*
	for (int i = 0; i < h; ++i){
		vertex1.push_back(i);
		vertex1.push_back(i);
		color1.push_back(0.0);
		color1.push_back(0.41);
		color1.push_back(0.41);
		color1.push_back(1.0);
	}
	*/
}


void Renderer::display()
{

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glViewport(0, 0, w, h);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, w, 0, h, -1, 1);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	glEnableClientState(GL_VERTEX_ARRAY);
	glEnableClientState(GL_COLOR_ARRAY);

	glVertexPointer(2, GL_INT, 0, &vertex[0]);
	glColorPointer(3, GL_FLOAT, 0, &color[0]);
	//glVertexPointer(2, GL_INT, 0, &vertex1[0]);
	//glColorPointer(4, GL_FLOAT, 0, &color1[0]);
	//glPointSize(3.0);
	glDrawArrays(GL_POINTS, 0, vertex.size()/2);
	glDisableClientState(GL_VERTEX_ARRAY);
	glDisableClientState(GL_COLOR_ARRAY);
	glFlush();
	glutSwapBuffers();
}

void kbd(unsigned char key, int x, int y)
{
	switch ((char)key) {
	case 'q':
	case 27:   //esc
		glutDestroyWindow(window);
		exit(0);
	default:
		break;
	}

	return;
}

Renderer* Renderer::instance = 0;
void Renderer::setInstance(Renderer *r){
	instance = r;
}
void Renderer::displayWrapper(){
	instance->display();
}
void Renderer::init(Vector background)
{
	glutInit(argcp, argv);
	//glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowSize(w, h);
	window = glutCreateWindow("Ray Tracey");

	glutKeyboardFunc(kbd);
	glutDisplayFunc(displayWrapper);
	glClearColor(background[0],background[1],background[2], 0.0);
	glutMainLoop();
	exit(0);
}

void Renderer::add_pixel(int x, int y, double r, double g, double b){
	vertex.push_back(x);
	vertex.push_back(y);
	//cout << r << g << b << endl;
	color.push_back(r);
	color.push_back(g);
	color.push_back(b);
}