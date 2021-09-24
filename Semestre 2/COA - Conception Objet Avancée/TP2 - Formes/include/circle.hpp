#ifndef __CIRCLE_H__
#define __CIRCLE_H__

#include "shape.hpp"

class Circle : public Shape {
    int rr;
public:
    Circle(int x, int y, int r);
    void draw(Image &img);
    bool is_inside(int x, int y) const;
};

#endif
