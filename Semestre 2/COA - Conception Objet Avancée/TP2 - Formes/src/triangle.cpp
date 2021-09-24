#include "triangle.hpp"

Triangle::Triangle(int x, int y, int a, int b) : Shape(x,y), aa(a), bb(b) {}

void Triangle::draw(Image &img)
{
  cout << "Triangle::draw() called" << endl;
  PR(xx); PR(yy); PR(aa); PR(bb);

  img.draw_triangle(xx-aa/2, yy-bb/2, xx+aa/2, yy-bb/2, xx, yy+bb/2, yellow, opacity);  

  if (selected != NO_SELECT) {
      for (int i=1; i<=4; i++) {
          img.draw_line(xx-aa/2+i, yy-bb/2+i, xx+aa/2-i, yy-bb/2+i, black, 1, 0xFFFF0000);
          img.draw_line(xx-aa/2+i, yy-bb/2+i, xx-i,      yy+bb/2-i, black, 1, 0xFFFF0000);
          img.draw_line(xx+aa/2-i, yy-bb/2+i, xx-i,      yy+bb/2+i, black, 1, 0xFFFF0000);
      }
  }
}

int cross_product_z(int x1, int y1, int x2, int y2)
{
    return x1 * y2 - x2 * y1;
}

bool Triangle::is_inside(int x, int y) const
{
    // Algorithm from here : https://blackpawn.com/texts/pointinpoly/default.html
    
    // is it above the base (A->B) ?
    int z1 = cross_product_z(aa, 0, x-(xx-aa/2), y-(yy-bb/2));
    // is it right of (A->C) ?
    int z2 = cross_product_z(x-(xx-aa/2), y-(yy-bb/2), aa/2, bb);
    // is it left of (B->C)
    int z3 = cross_product_z(-aa/2, bb, x-(xx+aa/2), y-(yy-bb/2));

    if (z1 >= 0 && z2 >= 0 && z3 >= 0)
        return true;
    else return false;
}
