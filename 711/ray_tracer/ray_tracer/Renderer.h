#ifndef RENDERER_H 
#define RENDERER_H 
#include <SDL.h>
#include <iostream>
class Renderer 
{
public:
	Renderer(int screenWidth, int screenHeight);
	~Renderer();


	void begin();
	void end();
	void set_pixel(SDL_Surface *surface, int x, int y, Uint32 color);
private:
	int screen_width;
	int screen_height;

	SDL_Renderer *renderer;
	SDL_Event *event;

	SDL_Window *window;
};
#endif