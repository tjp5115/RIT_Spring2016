#include "Renderer.h"
#include "DiamondSquare.h"
#include "ViewParams.h"
#include "LightParams.h"
#include "RGBColor.h";
#include "Point3D.h";
int main(int argc, char **argv){
	ViewParams *vp = new ViewParams();

	LightParams *lp = new LightParams(RGBColor(1.0f, 1.0f, 1.0f), RGBColor(0.5, 0.5, 0.5), Point3D(0.0f, 8.0f, -5.0f));
	RGBColor Ax(0.95f, 0.85f, .75f);
	float   Ka = 0.8f;

	RGBColor Dx(0.6f, 0.5f, 0.4f);
	float   Kd = 0.7f;

	RGBColor Sx(0.3f, 0.3f, 0.3f);
	float   n = 15.0f;
	float   Ks = 0.2f;
	lp->addPhong(Ax, Ka, Dx, Kd, Sx, n, Ks);
	
	Renderer *render = new Renderer(800, 800, &argc, argv, vp, lp);

	// changing Height
	//DiamondSquare *ds = new DiamondSquare(render, 32, 100.0, 41412341, 150.0);
	//DiamondSquare *ds = new DiamondSquare(render, 32, 50.0, 41412341, 150.0);
	//sees changing
	DiamondSquare *ds = new DiamondSquare(render, 2048, 100.0, time(NULL), 250.0);
	
	//DiamondSquare *ds = new DiamondSquare(render, 64, 126.0, 123098, 225.0);
	//DiamondSquare *ds = new DiamondSquare(render, 32, 50.0, 41412341, 250.0);
	//looks bad.
	//DiamondSquare *ds = new DiamondSquare(render, 256, 400.0, 41412341, 300.0); // need to fix the right edge. getting bad triangles.
}