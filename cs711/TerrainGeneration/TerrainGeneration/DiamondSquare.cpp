#include "DiamondSquare.h"
#include <iostream>
using namespace std;
DiamondSquare::DiamondSquare(Renderer *_r, int levels, float height, unsigned int _rnd_seed, float graph_seed)
{
	renderer = _r;
	size = levels + 1;
	vector<float> temp(size*size);
	height_map = temp;
	// initialze the array;
	set_element(0, 0, graph_seed);
	set_element(0, size - 1, graph_seed);
	set_element(size - 1, 0, graph_seed);
	set_element(size - 1, size - 1, graph_seed);

	create_graph(height);
	draw_graph();
}


DiamondSquare::~DiamondSquare()
{
}

void DiamondSquare::create_graph(float height){
	for (int side_length = size - 1; side_length >= 2; side_length /= 2, height /= 2){
		int half = side_length / 2;

		// square stage 
		for (int x = 0; x < size - 1; x += side_length){
			for (int y = 0; y < size - 1; y += side_length){
				//find midpoitns
				float a = get_element(x						,	y);
				float b = get_element(x + side_length		,	y);
				float c = get_element(x						,	y + side_length);
				float d = get_element(x + side_length		,	y + side_length);
				//average the midpoints, and add noise to the new value
				float val = ((a + b + c + d) / 4) + get_random(height);
				set_element(x + half, y + half, val);
			}
		}


		//diamond stage 
		for (int x = 0; x < size - 1; x += half){
			for (int y = (x+half)%side_length; y < size - 1; y += side_length){
				// find the midpoints, and get the elements
				float l = get_element((x - half + size - 1) % (size-1)	,		y);
				float h = get_element((x + half) % (size-1)				,		y);
				float i = get_element(x									,		(y + half) % (size-1));
				float g = get_element(x									,		(y - half + size - 1) % (size-1));
				//average the 4 midpoints, and add noise to the new value;
				float val = ((l + h + i + g) / 4) + get_random(height);
				set_element(x, y, val);

				//weap values located on edge of graph.
				if (x == 0) set_element(size - 1, y, val);
				if (y == 0) set_element(x, size - 1, val);
			}
		}

	}


}

void DiamondSquare::draw_graph(){
	for (int i = 0; i < size; ++i){
		for (int j = 0; j < size; ++j)
			cout << get_element(i, j) << "\t";
		cout << endl;
	}

	int r;
	for (r = 0; r < size - 1; ++r){
		int c;
		for (c = 0; c < size - 1; ++c){
			// add a square to the renderer
			renderer->add_point(r, get_element(r, c), c, 0, 1, 0);
			renderer->add_point(r, get_element(r, c+1), c+1, 0, 1, 0);
			renderer->add_point(r+1, get_element(r+1, c), c, 0, 1, 0);
		//	cout << "(" << r << " " << c << ")\t" << "(" << r << " " << c+1 << ")\t" << "(" << r+1 << " " << c << ")"<< endl;
			renderer->add_point(r+1, get_element(r+1, c), c, 0, 1, 0);
			renderer->add_point(r,  get_element(r, c+1) , c+1, 0, 1, 0);
			renderer->add_point(r+1, get_element(r+1, c+1), c+1, 0, 1, 0);
		//	cout << "(" << r+1 << " " << c << ")\t" << "(" << r << " " << c+1 << ")\t" << "(" << r+1 << " " << c+1 << ")\n"<< endl;
		}
	}

	renderer->draw();
}

void DiamondSquare::set_element(int r, int c, float val){
	height_map[size * r + c] = val;
}

float DiamondSquare::get_element(int r, int c){
	return height_map[size * r + c];
}


float DiamondSquare::get_random(float h){
	srand(rnd_seed++);
	return rand() % (int)h * 2 - h;
}