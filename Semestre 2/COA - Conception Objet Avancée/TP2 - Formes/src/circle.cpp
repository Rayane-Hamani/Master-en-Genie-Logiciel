#include <cmath>
#include "circle.hpp"


Circle::Circle(int x, int y, int r) : Shape(x,y), rr(r) {}

void Circle::draw(Image &img)
{
  cout << "Circle::draw() called" << endl;
  PR(xx); PR(yy); PR(rr);

  img.draw_circle(xx, yy, rr, blue, opacity);
  if (selected != NO_SELECT) {
      for (int i=1; i<5; i++) 
          img.draw_circle(xx, yy, rr-i, black, 1, 0xFFFF0000);
  }
}

bool Circle::is_inside(int x, int y) const
{
    double myr = (xx-x)*(xx-x) + (yy-y)*(yy-y);
    myr = sqrt(myr);
    if (myr <= rr) return true;
    else return false;
}
