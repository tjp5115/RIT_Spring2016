#include "World.h"
#include "Material.h"
World::World(Renderer *renderer_p){
	renderer = renderer_p;
	background = RGBColor();
	DEPTH = 1;
	//default camera
	camera.lookat = Point3D(0,0,0);
	camera.up = Vector3D(0,1,0);
	camera.eye = Point3D(0,0,500);
	camera.vp_distance = 200;
	compute_uvw();

	black = RGBColor(0);
	tone = new ToneReproduction();
}

/**
 * @brief add an object to the scene
 */
void World::add_object(Object *o){
	objects.push_back(o);
}

/**
 * @brief begin the raytrace
 */
void World::render_scene(){

	Ray ray;
	double pixel[2];
	int h = renderer->h;
	int w = renderer->w;
	int size = (h) * (w);
	ray.o = camera.eye;
	double L_wa = 0.0;
	vector<RGBColor> color_array = vector<RGBColor>(size);
	for (int r = 0; r < h; r++){
		for (int c = 0; c < w; c++) {
			pixel[0] = (c - 0.5 * w + 0.5);
			pixel[1] = (r - 0.5 * h + 0.5);
			ray.d = get_camera_direction(pixel);
			color_array[h * r + c] = trace_ray(ray);
			L_wa += std::log(0.000001 + tone->get_L(color_array[h * r + c]));
		}
	}

	double e = 2.71828;
	L_wa /= size;
	cout << L_wa << endl;
	L_wa = std::pow(e, L_wa);
	cout << L_wa << endl;
	RGBColor color;
	for (int r = 0; r < h; r++){
		for (int c = 0; c < w; c++) {
			color = tone->get_tone(color_array[h * r + c], L_wa);
			//cout << color.r << endl;
			renderer->add_pixel(c, r, color.r, color.g, color.b);
		}
	}

	renderer->init(background);
}

/**
 * @brief report the information of a traced ray.
 */
IntersectData World::hit_objects(const Ray &ray){
	return hit_objects(ray, -1);
}
/**
 * @brief checks all objects for an intersection, records that data, returns the data for the nearest hit.
 */
IntersectData World::hit_objects(const Ray &ray, int obj_id){
	IntersectData id(*this);
	double w;
	Normal normal;
	double w_min = BIG_NUMBER;
	Point3D hit;
	for (int i = 0; i < objects.size(); ++i){
		if (i == obj_id){
			continue;
		}
		if (objects[i]->hit(ray, w, id) && (w < w_min) ){
			w_min = w;
			id.hit_obj = true;
			hit = id.hit_pt;
			id.material = objects[i]->material;
			id.ray = ray;
			normal = id.n;
			id.obj_id = i;
			//cout << id.obj_id << " " << obj_id << endl;
		}
	}
	if (id.hit_obj){
		id.hit_pt = hit;
		id.n = normal;
	}
	return id;

}

/**
 * @brief compute camera vectors
 */
void World::compute_uvw(){
	camera.n = camera.eye - camera.lookat;
	camera.n.normalize();
	camera.u = camera.up ^ camera.n;
	camera.u.normalize();
	camera.v = camera.n ^ camera.u;
}


/**
 * @brief get the camera direction
 */
Vector3D World::get_camera_direction(double pixel[]){
	Vector3D v = pixel[0] * camera.u + pixel[1] * camera.v -camera.vp_distance * camera.n;
	v.normalize();
	return v;
}

/**
 * @brief set the camera parameters
 */
void World::set_camera(Point3D e, Point3D l, Vector3D up, double vp_dist){
	camera.eye = e;
	camera.lookat = l;
	camera.up = up;
	camera.vp_distance = vp_dist;
	compute_uvw();
}


/**
 * @brief start the ray trace for a given ray
 */
RGBColor World::trace_ray(const Ray &ray){
	IntersectData id(hit_objects(ray));
	if (id.hit_obj){
		RGBColor total_color = id.material->get_ambient(id) * RGBColor (1);
		for (int i = 0; i < lights.size(); ++i){
			total_color += id.material->get_illumination(*lights[i],id,DEPTH);
		}
		total_color.clamp();
		return total_color;
	}
	return background;
}