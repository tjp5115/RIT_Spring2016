#pragma once
#include "Renderer.h"
#include <vector>
#include <stdlib.h>
class DiamondSquare
{
public:
	DiamondSquare(Renderer *r, int levels, float _height, unsigned int _rnd_seed, float graph_seed);
	~DiamondSquare();
private:
	vector<float> height_map;
	Renderer *renderer;
	int size;
	float max_height;
	unsigned int rnd_seed;
	void create_graph(float height);
	void set_element(int r, int c, float val);
	float get_element(int r, int c);
	float get_random(float h);
	void draw_graph();
};

