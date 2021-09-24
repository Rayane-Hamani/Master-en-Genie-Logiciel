#ifndef __SHAPE_H__
#define __SHAPE_H__

#include <CImg.h>
#include <iostream>

const unsigned char white[] = {255, 255, 255},
    red[]   = { 255,0,0 },
    green[] = { 0,255,0 },
    blue [] = { 128,128,255 },
    yellow[] = {255, 255, 0},
    black[] = { 0,0,0 };

using namespace std;

typedef cimg_library::CImg<unsigned char> Image;

// Macros used for debugging
#define TRACE(x) {cout << "executing " #x << endl; x;}
#define PR(x) {cout << "    " #x << " = " << x << endl;}

class Shape {
public:
    enum select_state { NO_SELECT, SELECT, MOVING, SELECT_STATE_END, } ;

    Shape(int x, int y);
    
    virtual void move(int x, int y);
    virtual void draw(Image &img) = 0;
    virtual bool is_inside(int x, int y) const = 0;
    virtual select_state select();

    inline int getX() const { return xx; }
    inline int getY() const { return yy; }
    select_state get_state() const { return selected; }

    virtual ~Shape() {}
    
protected:
    int xx,yy;
    select_state selected = NO_SELECT;
    float opacity = 1.0;
};

#endif
