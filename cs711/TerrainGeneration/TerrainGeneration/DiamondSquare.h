#pragma once
#include "Renderer.h"
#include <vector>
#include <stdlib.h>
#include "Point3D.h"
#include "Normal.h"
class DiamondSquare
{
public:
	DiamondSquare(Renderer *r, int levels, double height, unsigned int _rnd_seed, double graph_seed);
	~DiamondSquare();
private:
	vector<double> height_map;
	vector<vector<double>> tex_map;
	Renderer *renderer;
	int size;
	double max_height;
	unsigned int rnd_seed;
	void create_graph(double height);
	void set_height_element(int r, int c, double val);
	double get_height_element( int r, int c);
	void set_tex_element(int r, int c, vector<double> val);
	vector<double> get_tex_element(int r, int c);
	double get_random(double h);
	void draw_graph();
	void set_texture();
};

