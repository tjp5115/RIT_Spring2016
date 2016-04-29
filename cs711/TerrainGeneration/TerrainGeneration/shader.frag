#version 120
//Author: Tyler Paulsen
// Flat shading fragment shader
uniform sampler2D texture[3];
uniform float maxHeight;
uniform float minHeight;

//incoming data
varying vec3 v_position;
varying vec2 texCoord;
void main()
{
	float heightScale = (v_position.y - minHeight) / (maxHeight - minHeight);

	const float fRange1 = 0.15f; 
	vec4 color1 = vec4(1.0, 0.0, 1.0, 1.0);
	const float fRange2 = 0.3f; 
	vec4 color2 = vec4(1.0, 0.0, 0.0, 1.0);
	const float fRange3 = 0.65f; 
	vec4 color3 = vec4(1.0, 1.0, 0.0, 1.0);
	const float fRange4 = 0.85f; 
		
	vec4 color = vec4(0.0);
	
	
	if(heightScale >= 0.0 && heightScale <= fRange1){
		//color = texture2D(texture[0], texCoord); 
		color = color1;
	}else if(heightScale <= fRange2) { 
		heightScale -= fRange1; 
		heightScale /= (fRange2-fRange1); 
	   
		float heightScale2 = heightScale; 
		heightScale = 1.0-heightScale;  
		color += color1 * heightScale;
		color += color2 * heightScale2;
		//color += texture2D(texture[0], texCoord)*heightScale; 
		//color += texture2D(texture[1], texCoord)*heightScale2; 
   }else if(heightScale <= fRange3){
		//color = texture2D(texture[1], texCoord); 
		color = color2;
   }else if(heightScale <= fRange4){ 
		heightScale -= fRange3; 
		heightScale /= (fRange4-fRange3); 
	   
		float heightScale2 = heightScale; 
		heightScale = 1.0-heightScale;  
	   
		color += color2 * heightScale;
		color += color3 * heightScale2;
		//color += texture2D(texture[1], texCoord)*heightScale; 
		//color += texture2D(texture[2], texCoord)*heightScale2;       
   }else{
		color = color3;
   }

    gl_FragColor = color;
}
