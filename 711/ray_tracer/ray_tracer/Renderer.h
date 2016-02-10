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

private:
	int screen_width;
	int screen_height;

	int mouseX;
	int mouseY;


	bool quit;

	SDL_Renderer *renderer;
	SDL_Event *event;

	SDL_Window *window;
};
#endif