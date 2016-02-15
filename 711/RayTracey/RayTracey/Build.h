#pragma once
#include "Object.h"
#include <vector>
using namespace std;
class Build
{
public:
	Build();
	~Build();
	vector<Object*> objects;
	vector<Object*> get_objects(){return objects;};
	void add_object(Object *o){ objects.push_back(o); }
};

