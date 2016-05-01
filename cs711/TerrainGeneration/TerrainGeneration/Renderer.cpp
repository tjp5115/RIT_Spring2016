#include "Renderer.h"
int window;
#include <iostream>
#include "shaderSetup.h"
using namespace std;
#define BUFFER_OFFSET(i) ((char *)NULL + (i))

Renderer::Renderer(int width, int height, int *_argcp, char **_argv, ViewParams *_vp, LightParams *_lp)
{
	w = width;                 
	h = height;
	setInstance(this);
	argcp = _argcp;
	argv = _argv;
	vp = _vp;
	lp = _lp;
	minY = 10000.0;
	maxY = -100000.0;
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
	init();
	// set up our attribute variables
	vp->camera(program);
	vp->transform(program);
	vp->frustum(program);
	lp->setUpPhong(program);
}

void Renderer::init(){
	glutInit(argcp, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
	glutInitWindowSize(w, h);
	window = glutCreateWindow("Terrain Generator");
	glewInit();
	// Load shaders and use the resulting shader program
	program = shaderSetup("shader.vert", "shader.frag");
	if (!program) {
		cerr << "Error setting up shaders - " << errorString(shaderErrorCode) << endl;
	}
	// clear the frame buffer
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// bind our vertex buffer
	//glBindBuffer(GL_ARRAY_BUFFER, vbuffer);

	// bind our element array buffer
	//glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebuffer);

	glUseProgram(program);



}

void Renderer::display()
{
	//cout << minY << " " << maxY << endl;
	glClear(GL_COLOR_BUFFER_BIT);
	int dataSize = vertices.size() * sizeof (float);

	GLuint vPosition = glGetAttribLocation(program, "vPosition");
	glEnableVertexAttribArray(vPosition);
	glVertexAttribPointer(vPosition, 3, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(0));

	GLuint vTex = glGetAttribLocation(program, "vTexCoord");
	glEnableVertexAttribArray(vTex);
	glVertexAttribPointer(vTex, 2, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(dataSize));

	dataSize += tex.size() * sizeof(float);
	// set up the normal attribute variables.
	int vNormal = glGetAttribLocation(program, "vNormal");
	glEnableVertexAttribArray(vNormal);
	glVertexAttribPointer(vNormal, 3, GL_FLOAT, false, 0, BUFFER_OFFSET(dataSize));

	// draw our shape
	glDrawElements(GL_TRIANGLES, elements.size(), GL_UNSIGNED_INT, (void *)0);

	// swap the framebuffers
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
void Renderer::draw()
{
	glClearColor(0,.7,1,1.0);

	int pDataSize = vertices.size() * sizeof (float);
	int tDataSize = tex.size() * sizeof(float);
	int nDataSize = normals.size() * sizeof(float);
	int eDataSize = elements.size() * sizeof (int);;
	//cout << tDataSize << endl;
	//cout << pDataSize << endl;
	//cout << nDataSize << endl;
	cout << eDataSize << endl;
	// create and fill a new point array

	// get and load the element data
	glGenBuffers(1, &vbuffer);
	glBindBuffer(GL_ARRAY_BUFFER, vbuffer);
	glBufferData(GL_ARRAY_BUFFER, pDataSize + tDataSize + nDataSize, NULL, GL_STATIC_DRAW);
	glBufferSubData(GL_ARRAY_BUFFER, 0, pDataSize, &vertices[0]);
	glBufferSubData(GL_ARRAY_BUFFER, pDataSize, tDataSize, &tex[0]);
	glBufferSubData(GL_ARRAY_BUFFER, tDataSize + pDataSize, nDataSize, &normals[0]);

	glGenBuffers(1, &ebuffer);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebuffer);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, eDataSize, &elements[0], GL_STATIC_DRAW);



	glutKeyboardFunc(kbd);
	glutDisplayFunc(displayWrapper);
	glutMainLoop();
	exit(0);
}

void Renderer::add_point(float x, float y, float z){
		vertices.push_back(x);
		vertices.push_back(y);
		vertices.push_back(z);
		if (minY > y){
			minY = y;
		}else if (maxY < y){
			maxY = y;
		}
}

void Renderer::add_normal(float x, float y, float z){
	normals.push_back(x);
	normals.push_back(y);
	normals.push_back(z);
	//cout << x << " " << y <<  " " << z << endl;
}

void Renderer::add_tex(float u, float v){
	tex.push_back(u);
	tex.push_back(v);
	if (u > .99)
		cout << u << " " << v << endl;

}

void Renderer::add_element(int i){
	elements.push_back(i);
}

