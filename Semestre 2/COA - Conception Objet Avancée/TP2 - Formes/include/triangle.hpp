#ifndef __TRIANGLE_H__
#define __TRIANGLE_H__

#include "shape.hpp"

class Triangle : public Shape {
    int aa, bb;
public:
    Triangle(int x, int y, int a, int b);
    
    // void move(int xx, int yy) {};    
    void draw(Image &img);
    bool is_inside(int x, int y) const;
};

#endif
