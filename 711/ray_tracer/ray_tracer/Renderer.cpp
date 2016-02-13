#include "Renderer.h"
Renderer::Renderer(int p_width, int p_height){
	SDL_Init(SDL_INIT_VIDEO);

	event = NULL;
	event = new SDL_Event();


	screen_width = p_width;
	screen_height = p_height;

	surface = SDL_CreateRGBSurface(SDL_SWSURFACE,width,height,32;

	window = NULL;
	window = SDL_CreateWindow("Tyler's SS", 100, 100, screen_width, screen_height, SDL_WINDOW_RESIZABLE | SDL_WINDOW_SHOWN);
	renderer = NULL;
	renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);

	if (window == NULL){
		std::cout << "failed to create window" << std::endl;
	}

}


Renderer::~Renderer()
{
	SDL_DestroyWindow(window);
}

void Renderer::begin(){
	//#SDL_PollEvent(gs->getMainEvent());
	SDL_RenderClear(renderer);
}
void Renderer::set_pixel( int x, int y, Uint32 color){
	SDL_LockSurface(surface);
	Uint32 *pixels = (Uint32 *)surface->pixels;
	pixels[ ( y * surface->w ) + x] = color;
	SDL_UnlockSurface(surface);
}
void Renderer::end(){
	SDL_RenderPresent(renderer);
}