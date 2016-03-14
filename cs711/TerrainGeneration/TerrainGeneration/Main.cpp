#include "Renderer.h"
#include "DiamondSquare.h"
int main(int argc, char **argv){
	Renderer *render = new Renderer(500,500,&argc,argv);
	DiamondSquare *ds = new DiamondSquare(render, 4, 50.0, 41412341, 100.0);
}