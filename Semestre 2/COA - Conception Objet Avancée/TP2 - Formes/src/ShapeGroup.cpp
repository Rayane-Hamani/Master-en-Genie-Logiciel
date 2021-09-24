#include "ShapeGroup.hpp"

ShapeGroup::ShapeGroup(int x, int y, vector<Shape *> group_shape) : Shape(x,y){
    this -> group_shape=group_shape;
}

void ShapeGroup::move(int x, int y){
    int offset_x = x-xx;
    int offset_y = y-yy;
    xx = x;
    yy = y;
    for (Shape * shape : group_shape){
        shape->move(shape->getX()+offset_x,shape->getY()+offset_y);
    }
}

void ShapeGroup::draw(Image &image){
    for(Shape * shape : group_shape) shape->draw(image);
}

bool ShapeGroup::is_inside(int x, int y) const{
    for(Shape * shape : group_shape){
        if(shape->is_inside(x,y)){
             return True;}
    }
    return False;
}

Shape::select_state ShapeGroup::select(){
    select_state state;
    for (Shape * shape : group_shape){state = shape->select();}
    return(state);
}

vector<Shape *> ShapeGroup::ungroup(){
    vector<Shape *> res;
    res.reserve(group_shape.size());
    for (Shape * shape : group_shape) res.push_back(shape);
    group_shape.clear();
    return res;
}