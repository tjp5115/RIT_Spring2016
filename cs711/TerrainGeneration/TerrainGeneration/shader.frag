#version 150
//Author: Tyler Paulsen
// Flat shading fragment shader
uniform sampler2D fungus;
uniform sampler2D sand;
uniform sampler2D rocks;
uniform float maxHeight;
uniform float minHeight;

// Lighting
uniform vec4 Ax;
uniform float Ka;

uniform vec4 Dx;
uniform float Kd;

uniform vec4 Sx;
uniform float n;
uniform float Ks;

// Light source properties
uniform vec4 Lx;
uniform vec4 ls_position;

uniform vec3 cPosition;

// Ambient light properties
uniform vec4 a_color;


//incoming data
in vec4 position;
in float height;
in vec2 texCoord;
in vec4 normal;
void main(){
	float heightScale = (height - minHeight) / (maxHeight - minHeight);

	const float fRange1 = 0.3f; 
	vec4 color1 = vec4(1.0, 0.0, 1.0, 1.0);
	const float fRange2 = 0.4f; 
	vec4 color2 = vec4(1.0, 0.0, 0.0, 1.0);
	const float fRange3 = 0.5f; 
	vec4 color3 = vec4(1.0, 1.0, 0.0, 1.0);
	const float fRange4 = 0.65f; 
		
	vec4 color = vec4(0.0);
	vec4 colour = vec4(0.0);
	
	float n_ = n;
	if( heightScale >= 0 && heightScale <= fRange1){
		color = texture2D(fungus, texCoord); 
		n_ = 10;
		//color = color1;
	}else if(heightScale <= fRange2) { 
		heightScale -= fRange1; 
		heightScale /= (fRange2-fRange1); 
	   
		float heightScale2 = heightScale; 
		heightScale = 1.0-heightScale;  
		color += texture2D(fungus, texCoord)*heightScale; 
		color += texture2D(sand, texCoord)*heightScale2; 

		colour = color1 * heightScale;
		colour += color2 * heightScale2;
		n_ = 12;
   }else if(heightScale <= fRange3){
		n_ = 50;
		color = texture2D(sand, texCoord); 
		colour = color2 ;
   }else if(heightScale <= fRange4){ 
		heightScale -= fRange3; 
		heightScale /= (fRange4-fRange3); 
	   
		float heightScale2 = heightScale; 
		heightScale = 1.0-heightScale;  
	   
		color += texture2D(sand, texCoord)*heightScale; 
		color += texture2D(rocks, texCoord)*heightScale2;       
		
		colour = color2 * heightScale;
		colour += color3 * heightScale2;
		n_ = 25;
   }else{
		color += texture2D(rocks, texCoord);       
		colour = color3;
		n_ = 12;
   }

    // Compute the diffuse term.
    vec4 N = normalize(normal);
    vec4 L = normalize( ls_position - position);
    float NdotL = max( dot( N, L ), 0 );
    vec4 Diffuse =  NdotL * Lx * Dx;
     
    // Compute the specular term.
    vec4 V = normalize( vec4(cPosition,1.0) - position);
    vec4 H = normalize( L + V );
    vec4 R = reflect( -L, N );
    float RdotV = max( dot( R, V ), 0 );
    float NdotH = max( dot( N, H ), 0 );
    vec4 Specular = pow(RdotV, n_) * Lx * Sx;
     
    color *= a_color  + Diffuse  + Specular ;

    gl_FragColor = color;

    //gl_FragColor = colour;
}
