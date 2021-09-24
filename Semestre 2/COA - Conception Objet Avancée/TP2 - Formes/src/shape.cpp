#include "shape.hpp"
#include <iostream>

Shape::Shape(int x,int y) : xx(x), yy(y) {}

void Shape::move(int x, int y)
{
  xx = x; yy = y;
  cout << "Shape::move called:\n";
  PR(xx); PR(yy);
}

Shape::select_state Shape::select()
{
    selected = static_cast<select_state>(int(selected + 1) % int(SELECT_STATE_END));
    if (selected == MOVING) opacity = 0.5;
    else opacity = 1.0;
    return selected;
}
