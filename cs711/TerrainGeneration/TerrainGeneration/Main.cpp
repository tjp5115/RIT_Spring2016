#include "Renderer.h"
#include "DiamondSquare.h"
#include "ViewParams.h"
int main(int argc, char **argv){
	ViewParams *vp = new ViewParams();
	Renderer *render = new Renderer(500,500,&argc,argv,vp);
	DiamondSquare *ds = new DiamondSquare(render, 64, 150.0, 41412341, 300.0);
}