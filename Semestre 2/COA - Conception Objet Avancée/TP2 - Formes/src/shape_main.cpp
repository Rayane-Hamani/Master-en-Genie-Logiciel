#include <iostream>
#include <sstream>
#include <iomanip>
#include <vector>
#include <cassert>
#include <CImg.h>
#include <shape.hpp>
#include <circle.hpp>
#include <rect.hpp>
#include <triangle.hpp>
#include <ShapeGroup.hpp>

using namespace cimg_library;
using namespace std;

/**
   Returns a pointer to the last shape in vector shapes that contains
   point (x,y)
 */
Shape *get_selected(const vector<Shape *> &shapes, int x, int y)
{
    Shape *tmp = nullptr;
    for (auto s : shapes) 
        if (s->is_inside(x, y)) tmp = s;
    return tmp;
}

/**
   If pointer s is inside the vector, it erases it from the vector,
   and returns it. Otherwise, it returns nullptr.
 */
Shape * vector_erase(vector<Shape *> &shapes, Shape *s)
{
    // look for the pointer to be removed
    auto it = find(shapes.begin(), shapes.end(), s);

    if (it != shapes.end()) { // if found
        shapes.erase(it);     // remove it
        return s;             // and return the pointer
    } else                    // else nothing to erase
        return nullptr;       // return nullptr
}

int main()
{
    // The main window
    CImgDisplay main_disp(900, 500, "Main Display", 0);

    // Mouse position
    int x = 0, y = 0;
    // Mouse button state
    bool button_status = false;

    // the image to be drawn on screen
    Image img(main_disp.width(),main_disp.height(), 1, 3, 0);

    vector<Shape *> all_shapes;     // the vector containing all shapes
    vector<Shape *> sel_shapes;     // the vector containing only selected shapes
    Shape *moving = nullptr;        // the shape to be moved
    
    while (!main_disp.is_closed() && !main_disp.is_keyESC() && !main_disp.is_keyQ()) {
        main_disp.wait();           // wait for a mouse or keyboard event
        
        x = main_disp.mouse_x();    // get the x position of the mouse, relative to the window
        y = main_disp.mouse_y();    // get the x position of the mouse, relative to the window
        
        bool click = main_disp.button(); // true if the mouse has been clicked
        button_status = (button_status != click);

        // keyboard events
        if (main_disp.is_key(cimg::keyC)) {
            // create a new circle
            Circle *c = new Circle(x, y, 50);
            all_shapes.push_back(c);
        }
        if (main_disp.is_key(cimg::keyR)) {
            // create a new rectangle
            Rect *c = new Rect(x, y, 120, 80);
            all_shapes.push_back(c);
        }
        if (main_disp.is_key(cimg::keyT)) {
            Triangle *c = new Triangle(x, y, 120, 80);
            all_shapes.push_back(c);
        }
        if (main_disp.is_key(cimg::keyG)) {
            ShapeGroup * new_group = new ShapeGroup(x,y,sel_shapes);
            for (Shape * shape : sel_shapes){
                vector_erase(all_shapes,shape);
                vector_erase(sel_shapes,shape);}
            all_shapes.push_back(new_group);
            sel_shapes.push_back(new_group);
        }
        if (main_disp.is_key(cimg::keyU)) {
            for (Shape * shape : sel_shapes){
                ShapeGroup *tmp = dynamic_cast<ShapeGroup *>(shape);
                if(tmp != nullptr){
                    vector_erase(all_shapes,shape);                                             
                    vector_erase(sel_shapes,shape);                                             
                    vector<Shape *> to_push = tmp->ungroup();                                   
                    for (Shape * shape2 : to_push){                                             
                        all_shapes.push_back(shape2);                                           
                    }
                }                                                                        
            }
        }
        if (main_disp.is_key(cimg::keyD)) {
            for (Shape *shape : sel_shapes){
                vector_erase(all_shapes,shape);
                vector_erase(sel_shapes,shape);
            }
        }
        
        // Select shapes
        if (click) {
            Shape *selected = get_selected(all_shapes, x, y);     // select the last shape under the mouse pointer, or nullptr
            if (selected != nullptr) {
                Shape::select_state f = selected->select();       // call select()
                if (f == Shape::SELECT)                           // if it is in selected state
                    sel_shapes.push_back(selected);               //   add it to the sel_shape vector
                else vector_erase(sel_shapes, selected);          // else remove it from sel_shapes
            
                if (f == Shape::MOVING) moving = selected;        // if it is in the moving state   
                else moving = nullptr;                            // assign it to moving
            }
            else if (moving) {
                moving->select();                                 // if there is something moving, 
                moving = nullptr;                                 // and we click outside, no moving anymore
            }
        }

        // if there is something to move ... 
        if (moving)                             
            moving->move(x,y);

        // Start drawing
        img.fill(255); // fill the background with white

        // draw the text
        stringstream ss;        
        ss.str() = "";
        ss << "X = " << setw(3) << x << "  Y = " << setw(3) << y
           << "   click = " << click << "  b status = " << button_status;
        cout << ss.str() << endl;
        img.draw_text(10,                // x position of the text
                      5,                 // y position of the text
                      ss.str().c_str(),  // the text as a c string
                      black,0,1,16);     // pen color, background color, etc.

        ss.str("");
        ss << "Press: c (new circle), r (new rectangle), t (new triangle), d (delete), g (group), u (ungroup)";
        img.draw_text(10,                // x position of the text
                      25,                // y position of the text
                      ss.str().c_str(),  // the text as a c string
                      black,0,1,16);     // pen color, background color, etc.

        // draw all shapes
        for (auto s : all_shapes)
            s->draw(img);
        
        img.display(main_disp);
        main_disp.wait(50);              // wait for 50 milliseconds
    }
}
