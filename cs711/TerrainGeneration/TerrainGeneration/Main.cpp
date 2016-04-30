#include "Renderer.h"
#include "DiamondSquare.h"
#include "ViewParams.h"
int main(int argc, char **argv){
	ViewParams *vp = new ViewParams();
	Renderer *render = new Renderer(500,500,&argc,argv,vp);
	// changing Height
	//DiamondSquare *ds = new DiamondSquare(render, 32, 100.0, 41412341, 150.0);
	//DiamondSquare *ds = new DiamondSquare(render, 32, 50.0, 41412341, 150.0);
	//sees changing
	DiamondSquare *ds = new DiamondSquare(render, 32, 150.0, 41412341, 250.0);
	//DiamondSquare *ds = new DiamondSquare(render, 64, 126.0, 123098, 225.0);
	//DiamondSquare *ds = new DiamondSquare(render, 32, 50.0, 41412341, 250.0);
	//looks bad.
	//DiamondSquare *ds = new DiamondSquare(render, 256, 400.0, 41412341, 300.0); // need to fix the right edge. getting bad triangles.
}