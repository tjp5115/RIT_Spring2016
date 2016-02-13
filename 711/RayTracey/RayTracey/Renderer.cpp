#include "Renderer.h"
int window;
Renderer::Renderer(int width, int height, int *argcp, char **argv)
{
	w = width;                 
	h = height;
	setInstance(this);

	int v1[] = { 155, 155 };
	int v2[] = { 255, 255 };
	vertex.push_back(v1);
	vertex.push_back(v2);
	float c1[] = { .3f, .5f, .9f};
	float c2[] = { .5f, .2f, .7f};
	color.push_back(c1);
	color.push_back(c2);
	init(argcp,argv);
}


void Renderer::display()
{

	glClear(GL_COLOR_BUFFER_BIT);

	glEnableClientState(GL_VERTEX_ARRAY);
	glEnableClientState(GL_COLOR_ARRAY);

	glVertexPointer(2, GL_INT, 0, &vertex[0]);
	glColorPointer(3, GL_FLOAT, 0, &color[0]);
	
	glDrawArrays(GL_POINTS, 0, 2);

	glDisable(GL_VERTEX_ARRAY);
	glDisable(GL_COLOR_ARRAY);
	glFlush();
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

void Renderer::init(int *argcp, char **argv)
{
	glutInit(argcp, argv);
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGBA);
	glutInitWindowSize(w, h);
	glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
	window = glutCreateWindow("Ray Tracey");

	glutKeyboardFunc(kbd);
	glutDisplayFunc(displayWrapper);

	glClearColor(1.0, 1.0, 1.0, 0.0);
	glLineWidth(3.0);

	/* start the GLUT main loop */
	glutMainLoop();

	exit(0);
}