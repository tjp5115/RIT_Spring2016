#include "DiamondSquare.h"
#include <iostream>
#include <SOIL.h>
using namespace std;
DiamondSquare::DiamondSquare(Renderer *_r, int levels, double height, unsigned int _rnd_seed, double graph_seed)
{
	renderer = _r;
	size = levels + 1;
	height_map = vector<double>(size*size);;
	tex_map = vector<double*>(size*size);;
	// initialze the array;
	set_height_element(0, 0, graph_seed);
	set_height_element(0, size - 1, graph_seed);
	set_height_element(size - 1, 0, graph_seed);
	set_height_element(size - 1, size - 1, graph_seed);
	max_height = graph_seed * 2;
	create_graph(height);
	set_texture();
	draw_graph();
	rnd_seed = _rnd_seed;

}


DiamondSquare::~DiamondSquare()
{
}

void DiamondSquare::create_graph(double height){
	for (int side_length = size - 1; side_length >= 2; side_length /= 2, height /= 2){
		int half = side_length / 2;

		// square stage 
		for (int x = 0; x < size - 1; x += side_length){
			for (int y = 0; y < size - 1; y += side_length){
				//find midpoitns
				double a = get_height_element(x, y);
				double b = get_height_element(x + side_length, y);
				double c = get_height_element(x, y + side_length);
				double d = get_height_element(x + side_length, y + side_length);
				//average the midpoints, and add noise to the new value
				double val = ((a + b + c + d) / 4) + get_random(height);
				set_height_element(x + half, y + half, val);
			}
		}


		//diamond stage 
		for (int x = 0; x < size - 1; x += half){
			for (int y = (x+half)%side_length; y < size - 1; y += side_length){
				// find the midpoints, and get the elements
				double l = get_height_element((x - half + size - 1) % (size - 1), y);
				double h = get_height_element((x + half) % (size - 1), y);
				double i = get_height_element(x, (y + half) % (size - 1));
				double g = get_height_element(x, (y - half + size - 1) % (size - 1));
				//average the 4 midpoints, and add noise to the new value;
				double val = ((l + h + i + g) / 4) + get_random(height);
				set_height_element(x, y, val);

				//weap values located on edge of graph.
				if (x == 0) set_height_element(size - 1, y, val);
				if (y == 0) set_height_element(x, size - 1, val);
			}
		}

	}


}

void DiamondSquare::set_texture(){
	unsigned int fungus = SOIL_load_OGL_texture("fungus.png", SOIL_LOAD_AUTO,SOIL_CREATE_NEW_ID,SOIL_FLAG_INVERT_Y);
	glBindTexture(GL_TEXTURE_2D, fungus);

	unsigned int sandGrass = SOIL_load_OGL_texture("sandGrass.png", SOIL_LOAD_AUTO, SOIL_CREATE_NEW_ID, SOIL_FLAG_INVERT_Y);
	glBindTexture(GL_TEXTURE_2D, sandGrass);

	unsigned int rocks = SOIL_load_OGL_texture("rocks.png", SOIL_LOAD_AUTO, SOIL_CREATE_NEW_ID, SOIL_FLAG_INVERT_Y);
	glBindTexture(GL_TEXTURE_2D, rocks);

	float textureU = float(230)*0.1f;
	float textureV = float(230)*0.1f;

	for(int r = 0; r < size ; ++r){
		for (int c = 0; c < size; ++c){
			float scaleC = float(c) / float(230);
			float scaleR = float(r) / float(230);
			double temp[2] = { textureU*scaleC, textureV*scaleR };
			set_tex_element(r, c, temp);
			//cout << temp[0] << " " << temp[1] << endl; 
		}
	}
}

void DiamondSquare::draw_graph(){
	/*
	for (int i = 0; i < size; ++i){
		for (int j = 0; j < size; ++j)
			cout << get_element(i, j) / max_height << "\t";
		cout << endl;
	}
	*/
	int r;
	double s = size - 2;
	double *texCord;
	for (r = 0; r < size - 1; ++r){
		int c;
		for (c = 0; c < size - 1; ++c){
			// add a square to the renderer
			renderer->add_point(r / s, get_height_element(r, c) / max_height, c / s);
			renderer->add_point(r / s, get_height_element(r, c + 1) / max_height, (c + 1) / s);
			renderer->add_point((r + 1) / s, get_height_element(r + 1, c) / max_height, c / s);

			renderer->add_point((r + 1) / s, get_height_element(r + 1, c) / max_height, c / s);
			renderer->add_point(r / s, get_height_element(r, c + 1) / max_height, (c + 1) / s);
			renderer->add_point((r + 1) / s, get_height_element(r + 1, c + 1) / max_height, (c + 1) / s);
			texCord = get_tex_element(r, c);
			renderer->add_tex(texCord[0], texCord[1]);
		}
	}

	int maxHeight = glGetUniformLocation(renderer->getProgram(), "maxHeight");
	glUniform1f(maxHeight, renderer->getMaxY());

	int minHeight = glGetUniformLocation(renderer->getProgram(), "minHeight");
	glUniform1f(minHeight, renderer->getMinY());

	renderer->draw();
}

void DiamondSquare::set_height_element(int r, int c, double val){
	height_map[size * r + c] = val;
}

double DiamondSquare::get_height_element(int r, int c){
	return height_map[size * r + c];
}

void DiamondSquare::set_tex_element( int r, int c, double val[2]){
	tex_map[size * r + c] = val;
}

double* DiamondSquare::get_tex_element( int r, int c){
	return tex_map[size * r + c];
}

double DiamondSquare::get_random(double h){
	double r = (double)rand() / (double)RAND_MAX;
	//cout << r << endl;
	return r * 2 * h - h;
}