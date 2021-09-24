#include "rect.hpp"

Rect::Rect(int x, int y, int a, int b) : Shape(x,y), aa(a), bb(b) {}

void Rect::draw(Image &img)
{
  cout << "Rect::draw() called" << endl;
  PR(xx); PR(yy); PR(aa); PR(bb);

  img.draw_rectangle(xx-aa/2, yy-bb/2, xx+aa/2, yy+bb/2, red, opacity);  

  // bord is drawn if the shape is selected or moving
  if (selected != NO_SELECT) {
      for (int i=1; i<=4; i++) {
          img.draw_line(xx-aa/2+i, yy-bb/2+i, xx+aa/2-i, yy-bb/2+i, black,1,0xFFFF0000);
          img.draw_line(xx+aa/2-i, yy-bb/2+i, xx+aa/2-i, yy+bb/2-i, black,1,0xFFFF0000);
          img.draw_line(xx+aa/2-i, yy+bb/2-i, xx-aa/2+i, yy+bb/2-i, black,1,0xFFFF0000);
          img.draw_line(xx-aa/2+i, yy+bb/2-i, xx-aa/2+i, yy-bb/2+i, black,1,0xFFFF0000);
      }
  }
}

bool Rect::is_inside(int x, int y) const
{
    if (x >= xx-aa/2 && y >= yy-aa/2 && y <= yy+bb/2 && x <= xx+aa/2)
        return true;
    else return false;
}
