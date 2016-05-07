Author: Tyler Paulsen
cs-711 Global Illumination

Building Code:
 - Libraries used: glut and glew

How to build:
	I build my code with Visual Studio 2015. The steps to build are:
	1) Project -> Properties -> Configuration Properties -> C/C++ -> General
			add <path to lib>\freeglut\include and <path to lib>\glew-1.13.0\include   to the "Additional Include Directories" property
	2) Project -> Properties -> Configuration Properties -> Linker -> General
			add <path to lib>\freeglut\lib and <path to lib>\glew-1.13.0\lib   to the "Additional Library Directories" property
	3) Project -> Properties -> Configuration Properties -> Linker -> Input
			add glew32.lib and freeglut.lib to the "Additional Dependencies" property


Notes:
If you are starting a clean project, you need to make sure the freeglut.dll library is in the same directory as the RayTracey.exe

If you open the project using the RayTracey.sln file, you will still need to change the properties listed above. They were set to my computer.

*********************************************************************************************************
************************************** IMPORTANT NOTE ***************************************************
As stated before, the only libararies used are glut and glew, so i took and old Headerfile,	and did a gmakemake > Makefile command. I did test to see if it would build with just a make command, and it there were no compilation errors, I did not run the project, only built it.

