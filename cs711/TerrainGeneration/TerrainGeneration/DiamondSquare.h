#pragma once
#include "Renderer.h"
#include <vector>
#include <stdlib.h>
class DiamondSquare
{
public:
	DiamondSquare(Renderer *r, int levels, double height, unsigned int _rnd_seed, double graph_seed);
	~DiamondSquare();
private:
	vector<double> height_map;
	Renderer *renderer;
	int size;
	double max_height;
	unsigned int rnd_seed;
	void create_graph(double height);
	void set_element(int r, int c, double val);
	double get_element(int r, int c);
	double get_random(double h);
	void draw_graph();
};

