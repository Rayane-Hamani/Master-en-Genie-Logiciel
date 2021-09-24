#ifndef __RECT_H__
#define __RECT_H__

#include "shape.hpp"

class Rect : public Shape {
    int aa, bb;
public:
    Rect(int x, int y, int a, int b);
    void draw(Image &img);
    bool is_inside(int x, int y) const;
};

#endif
