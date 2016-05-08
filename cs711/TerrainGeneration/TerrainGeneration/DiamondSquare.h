#pragma once
#include "Renderer.h"
#include <vector>
#include <stdlib.h>
#include <unordered_map>
#include <string>
#include "Point3D.h"
#include "Normal.h"
class DiamondSquare
{
public:
	DiamondSquare(Renderer *r, int levels, double height, unsigned int _rnd_seed, double graph_seed);
	~DiamondSquare();
private:
	vector<double> height_map;
	vector<Normal> normals;
	int num_points;
	vector<int> elements;
	Renderer *renderer;
	unordered_map<string, int> map;
	int size;
	double max_height;
	unsigned int rnd_seed;
	void create_graph(double height);
	void set_height_element(int r, int c, double val);
	double get_height_element( int r, int c);
	double get_random(double h);
	void draw_graph();
	void set_texture();
	void add_point(double x, double y, double z, double u, double v, Normal N);

};

