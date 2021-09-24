#ifndef __SHAPE_GROUP_H__
#define __SHAPE_GROUP_H__

#include <vector>
#include "shape.hpp"


class ShapeGroup : public Shape {
    vector<Shape *> group_shape;
    public:
    ShapeGroup(int x, int y, vector<Shape *> group_shape);
    void move(int x, int y);
    void draw(Image &image);
    bool is_inside(int x, int y) const;
    select_state select();
    vector<Shape *> ungroup();
    };

#endif