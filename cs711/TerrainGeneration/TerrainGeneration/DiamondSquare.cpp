#include "DiamondSquare.h"
#include <iostream>
using namespace std;
DiamondSquare::DiamondSquare(Renderer *_r, int levels, double height, unsigned int rnd_seed, double graph_seed)
{
	renderer = _r;
	size = levels + 1;
	height_map = vector<double>(size*size);
	normals = vector<Normal>(size*size);
	// initialze the array;
	set_height_element(0, 0, graph_seed);
	set_height_element(0, size - 1, graph_seed);
	set_height_element(size - 1, 0, graph_seed);
	set_height_element(size - 1, size - 1, graph_seed);
	max_height = graph_seed * 2;
	srand(rnd_seed);
	create_graph(height);
	set_texture();
	draw_graph();
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

	double s = size;
	double v1, v2;
	for (int r = 0; r < size; ++r){
		for (int c = 0; c < size; ++c){
			v1 = r / s;
			v2 = c / s;
			points.push_back(Point3D(
				v1,												//x
				get_height_element(r, c) / max_height,				//y
				v2));											//t
		}
	}

}

void DiamondSquare::set_texture(){



	unsigned int fungus = SOIL_load_OGL_texture("images/water.jpg", SOIL_LOAD_AUTO,SOIL_CREATE_NEW_ID,SOIL_FLAG_INVERT_Y);
	int fungus_location = glGetUniformLocation(renderer->getProgram(), "fungus");
	glUniform1i(fungus_location, 0);


	unsigned int sandGrass = SOIL_load_OGL_texture("images/grass.jpg", SOIL_LOAD_AUTO, SOIL_CREATE_NEW_ID, SOIL_FLAG_INVERT_Y);
	int sand_location = glGetUniformLocation(renderer->getProgram(), "sand");
	glUniform1i(sand_location, 1);


	unsigned int rocks = SOIL_load_OGL_texture("images/rocks.jpg", SOIL_LOAD_AUTO, SOIL_CREATE_NEW_ID, SOIL_FLAG_INVERT_Y);
	int rocks_location = glGetUniformLocation(renderer->getProgram(), "rocks");
	glUniform1i(rocks_location, 2);

	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, fungus);

	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, sandGrass);

	glActiveTexture(GL_TEXTURE2);
	glBindTexture(GL_TEXTURE_2D, rocks);
	
}

void DiamondSquare::draw_graph(){
	/*
	for (int i = 0; i < size; ++i){
		for (int j = 0; j < size; ++j)
			cout << get_element(i, j) / max_height << "\t";
		cout << endl;
	}
	*/
	double s = size - 1;
	vector<double> texCord;
	Point3D p1, p2, p3;
	int i1, i2, i3;
	Normal N;
	for (int r = 0; r < size - 1; ++r){
		for (int c = 0; c < size - 1; ++c){


			// add a square to the renderer
			i1 = size * r + c;
			i2 = size * r + c + 1;
			i3 = size * (r + 1) + c;
			p1 = points[i1];
			p2 = points[i2];
			p3 = points[i3];


			//cout << p1.x << p1.y << p1.z << p1.s << p1.t << endl;
			//cout << p1.s << " " << p1.t << endl;
			//cout << p2.x << p2.y << p2.z << endl;
			//cout << p3.x << p3.y << p3.z << endl;
			//calculate normal
			N = Normal(p1 ,p2 ,p3);
			N.normalize();
			add_point(i1, p1, N);
			add_point(i2, p2, N);
			add_point(i3, p3, N);

			i1 = size * (r + 1) + c;
			i2 = size * r + (c + 1);
			i3 = size * (r + 1) + (c + 1);
			p1 = points[i1]; 
			p2 = points[i2];
			p3 = points[i3];

			//calculate normal
			N = Normal(p1, p2, p3);
			N.normalize();
			add_point(i1, p1, N);
			add_point(i2, p2, N);
			add_point(i3, p3, N);
		}
	}

	for (int i = 0; i < normals.size(); ++i){
		Normal N = normals[i];
		Point3D p1 = points[i];
		renderer->add_normal(N.x, N.y, N.z);
		renderer->add_point(p1.x, p1.y, p1.z);
		renderer->add_tex(p1.x, p1.z);

	}

	int maxHeight = glGetUniformLocation(renderer->getProgram(), "maxHeight");
	glUniform1f(maxHeight, renderer->getMaxY());

	int minHeight = glGetUniformLocation(renderer->getProgram(), "minHeight");
	glUniform1f(minHeight, renderer->getMinY());
	
	//cout << num_points << endl;
	renderer->draw();
}

void DiamondSquare::set_height_element(int r, int c, double val){
	height_map[size * r + c] = val;
}

double DiamondSquare::get_height_element(int r, int c){
	return height_map[size * r + c];
}

double DiamondSquare::get_random(double h){
	double r = (double)rand() / (double)RAND_MAX;
	//cout << r << endl;
	return r * 2 * h - h;
}

void DiamondSquare::add_point(int index, Point3D p, Normal N){
	renderer->add_element(index);
	normals[index] += N;
}