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
	/*
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, w, 0, h, -1, 1);
	*/
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);


	gluLookAt(0, 100, 100,
		0, 0, 0,
		0, 1, 0);

	glScaled(100, 1, 100);

	glEnableClientState(GL_VERTEX_ARRAY);
	glEnableClientState(GL_COLOR_ARRAY);
	glVertexPointer(3, GL_FLOAT, sizeof(Vertex), &vertices[0].x);
	glColorPointer(3, GL_FLOAT, sizeof(Vertex), &vertices[0].red);
	
	glDrawArrays(GL_TRIANGLES, 0, vertices.size());
	glDisableClientState(GL_VERTEX_ARRAY);
	glDisableClientState(GL_COLOR_ARRAY);
	glFlush();
//	glutSwapBuffers();
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
void Renderer::draw()
{
	glutInit(argcp, argv);
	//glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowSize(w, h);
	window = glutCreateWindow("Ray Tracey");
	glutKeyboardFunc(kbd);
	glutDisplayFunc(displayWrapper);
	glClearColor(0,0,0,0.0);
	glutMainLoop();
	exit(0);
}

void Renderer::add_point(float x, float y, float z, float r, float g, float b){
		Vertex v = { x, y, z, r, g, b };
		vertices.push_back(v);
}