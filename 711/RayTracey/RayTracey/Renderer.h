#include <stdio.h>
#include <stdlib.h>
#include <vector>
#if defined(__APPLE__)
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
using namespace std;
class Renderer 
{
protected:
	static Renderer * instance;
public:
	Renderer(int width, int height, int *argcp, char **argv);
	~Renderer();

private:
	static void displayWrapper();
	void setInstance(Renderer *r);
	virtual void display();
	void init(int *argcp, char **argv);
	int w;
	int h;
	vector<int*> vertex;
	vector<float*> color;
};

