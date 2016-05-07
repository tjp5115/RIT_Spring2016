#
# Created by gmakemake (Ubuntu Jul 25 2014) on Sat May  7 11:25:53 2016
#

#
# Definitions
#

.SUFFIXES:
.SUFFIXES:	.a .o .c .C .cpp .s .S
.c.o:
		$(COMPILE.c) $<
.C.o:
		$(COMPILE.cc) $<
.cpp.o:
		$(COMPILE.cc) $<
.S.s:
		$(CPP) -o $*.s $<
.s.o:
		$(COMPILE.cc) $<
.c.a:
		$(COMPILE.c) -o $% $<
		$(AR) $(ARFLAGS) $@ $%
		$(RM) $%
.C.a:
		$(COMPILE.cc) -o $% $<
		$(AR) $(ARFLAGS) $@ $%
		$(RM) $%
.cpp.a:
		$(COMPILE.cc) -o $% $<
		$(AR) $(ARFLAGS) $@ $%
		$(RM) $%

CC =		gcc
CXX =		g++

RM = rm -f
AR = ar
LINK.c = $(CC) $(CFLAGS) $(CPPFLAGS) $(LDFLAGS)
LINK.cc = $(CXX) $(CXXFLAGS) $(CPPFLAGS) $(LDFLAGS)
COMPILE.c = $(CC) $(CFLAGS) $(CPPFLAGS) -c
COMPILE.cc = $(CXX) $(CXXFLAGS) $(CPPFLAGS) -c
CPP = $(CPP) $(CPPFLAGS)
########## Flags from header.mak

#
# This header.mak file will set up all necessary options for compiling
# and linking C and C++ programs which use OpenGL, GLUT, and/or GLEW
# on the Ubuntu systems.
#
# To use it, place it in the same directory as your source code, and
# run the command
#
#	gmakemake > Makefile
#
# To compile and link your program, just run "make".
#
# If you want to take advantage of GDB's extra debugging features,
# change "-g" in the CFLAGS and LIBFLAGS macro definitions to "-ggdb".
#
INCLUDE =
LIBDIRS =

LDLIBS = -lglut -lGL -lm -lGLEW

CFLAGS = -g $(INCLUDE) -DGL_GLEXT_PROTOTYPES
CCFLAGS =  $(CFLAGS)
CXXFLAGS = $(CFLAGS)

LIBFLAGS = -g $(LIBDIRS) $(LDLIBS)
CLIBFLAGS = $(LIBFLAGS)
CCLIBFLAGS = $(LIBFLAGS)

########## End of flags from header.mak


CPP_FILES =	Build_test.cpp Light.cpp Matrix.cpp Normal.cpp PhongMaterial.cpp Plane.cpp Point3D.cpp ProceduralShade.cpp RGBColor.cpp Ray.cpp Reinhard.cpp Renderer.cpp Sphere.cpp Vector3D.cpp Ward.cpp World.cpp
C_FILES =	
PS_FILES =	
S_FILES =	
H_FILES =	IntersectData.h Light.h Material.h Matrix.h Normal.h Object.h PhongMaterial.h Plane.h Point3D.h ProceduralShade.h RGBColor.h Ray.h Reinhard.h Renderer.h Sphere.h ToneReproduction.h Vector3D.h ViewPlane.h Ward.h World.h resource.h
SOURCEFILES =	$(H_FILES) $(CPP_FILES) $(C_FILES) $(S_FILES)
.PRECIOUS:	$(SOURCEFILES)
OBJFILES =	Light.o Matrix.o Normal.o PhongMaterial.o Plane.o Point3D.o ProceduralShade.o RGBColor.o Ray.o Reinhard.o Renderer.o Sphere.o Vector3D.o Ward.o World.o 

#
# Main targets
#

all:	Build_test 

Build_test:	Build_test.o $(OBJFILES)
	$(CXX) $(CXXFLAGS) -o Build_test Build_test.o $(OBJFILES) $(CCLIBFLAGS)

#
# Dependencies
#

Build_test.o:	IntersectData.h Light.h Material.h Matrix.h Normal.h Object.h PhongMaterial.h Plane.h Point3D.h ProceduralShade.h RGBColor.h Ray.h Reinhard.h Renderer.h Sphere.h ToneReproduction.h Vector3D.h Ward.h World.h
Light.o:	IntersectData.h Light.h Matrix.h Normal.h Object.h Point3D.h RGBColor.h Ray.h Renderer.h ToneReproduction.h Vector3D.h World.h
Matrix.o:	Matrix.h
Normal.o:	Matrix.h Normal.h Point3D.h Vector3D.h
PhongMaterial.o:	IntersectData.h Light.h Material.h Matrix.h Normal.h Object.h PhongMaterial.h Point3D.h RGBColor.h Ray.h Renderer.h ToneReproduction.h Vector3D.h World.h
Plane.o:	IntersectData.h Matrix.h Normal.h Object.h Plane.h Point3D.h RGBColor.h Ray.h Vector3D.h
Point3D.o:	Matrix.h Point3D.h Vector3D.h
ProceduralShade.o:	IntersectData.h Light.h Material.h Matrix.h Normal.h Object.h PhongMaterial.h Point3D.h ProceduralShade.h RGBColor.h Ray.h Renderer.h ToneReproduction.h Vector3D.h World.h
RGBColor.o:	RGBColor.h
Ray.o:	Matrix.h Point3D.h Ray.h Vector3D.h
Reinhard.o:	RGBColor.h Reinhard.h ToneReproduction.h
Renderer.o:	RGBColor.h Renderer.h
Sphere.o:	IntersectData.h Matrix.h Normal.h Object.h Point3D.h RGBColor.h Ray.h Sphere.h Vector3D.h
Vector3D.o:	Matrix.h Normal.h Point3D.h Vector3D.h
Ward.o:	RGBColor.h ToneReproduction.h Ward.h
World.o:	IntersectData.h Light.h Material.h Matrix.h Normal.h Object.h Point3D.h RGBColor.h Ray.h Renderer.h ToneReproduction.h Vector3D.h World.h

#
# Housekeeping
#

Archive:	archive.tgz

archive.tgz:	$(SOURCEFILES) Makefile
	tar cf - $(SOURCEFILES) Makefile | gzip > archive.tgz

clean:
	-/bin/rm -f $(OBJFILES) Build_test.o core

realclean:        clean
	-/bin/rm -f Build_test 
