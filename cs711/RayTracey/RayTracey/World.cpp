#include "World.h"
#include "Material.h"
World::World(Renderer *renderer_p){

	renderer = renderer_p;
	background = RGBColor();

	//default camera
	camera.lookat = Point3D(0,0,0);
	camera.up = Vector3D(0,1,0);
	camera.eye = Point3D(0,0,500);
	camera.vp_distance = 200;
	compute_uvw();

	black = RGBColor(0);
}


World::~World()
{
}

void World::add_object(Object *o){
	objects.push_back(o);
}

void World::render_scene(){

	RGBColor color;
	Ray ray;
	double pixel[2];
	int h = renderer->h;
	int w = renderer->w;

	ray.o = camera.eye;
	
	for (int r = 0; r < h; r++)				
		for (int c = 0; c < w; c++) {
			pixel[0] = (c - 0.5 * w + 0.5);
			pixel[1] = (r - 0.5 * h + 0.5);
			ray.d = get_camera_direction(pixel);
			color = trace_ray(ray);
			if (!(color == background) )
				renderer->add_pixel(c, r, color.r, color.g, color.b);
		}
	renderer->init(background);
}


IntersectData World::hit_objects(const Ray &ray){
	return hit_objects(ray, -1);
}
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

void World::compute_uvw(){
	camera.n = camera.eye - camera.lookat;
	camera.n.normalize();
	camera.u = camera.up ^ camera.n;
	camera.u.normalize();
	camera.v = camera.n ^ camera.u;
}


Vector3D World::get_camera_direction(double pixel[]){
	Vector3D v = pixel[0] * camera.u + pixel[1] * camera.v -camera.vp_distance * camera.n;
	v.normalize();
	return v;
}

void World::set_camera(Point3D e, Point3D l, Vector3D up, double vp_dist){
	camera.eye = e;
	camera.lookat = l;
	camera.up = up;
	camera.vp_distance = vp_dist;
	compute_uvw();
}


RGBColor World::trace_ray(const Ray &ray){
	IntersectData id(hit_objects(ray));
	if (id.hit_obj){
			Ray shadow = Ray();
			RGBColor total_color = id.material->get_ambient(id) * RGBColor (1);
			shadow.o = id.hit_pt;
		for (int i = 0; i < lights.size(); ++i){
			shadow.d = lights[i]->position - id.hit_pt;
			shadow.d.normalize();
			if (!lights[i]->in_shadow(shadow,id)){
				total_color += id.material->get_illumination(*lights[i],id);
			}
//				total_color += id.material->get_illumination(*lights[i], id, shadow); 
		}
		total_color.clamp();
		return total_color;
	}
	return background;
}