#include <iostream>
#include <sstream>
#include <iomanip>
#include <CImg.h>

using namespace cimg_library;
using namespace std;

int main()
{
    const unsigned char
        white[] = {255, 255, 255},
        red[]   = { 255,0,0 },
        green[] = { 0,255,0 },
        blue [] = { 0,0,255 },
        yellow[] = {255, 255, 0},
        black[] = { 0,0,0 };

    // The main window
    CImgDisplay main_disp(700, 400, "Main Display", 0);

    // Mouse position
    int x = 0, y = 0;
    // Mouse button state
    bool button_status = false;

    CImg<unsigned char> img(main_disp.width(),main_disp.height(), 1, 3, 0);
    
    while (!main_disp.is_closed() && !main_disp.is_keyESC() && !main_disp.is_keyQ()) {
        main_disp.wait();           // wait for a mouse or keyboard event
        
        x = main_disp.mouse_x();    // get the x position of the mouse, relative to the window
        y = main_disp.mouse_y();    // get the x position of the mouse, relative to the window
        
        bool click = main_disp.button(); // true if the mouse has been clicked
        
        button_status = (button_status != click);

        bool keyA = main_disp.is_key(cimg::keyA);
        
        stringstream ss;

        ss << "X = " << setw(3) << x << "  Y = " << setw(3) << y << "   click = " << click
           << "   b status = " << button_status <<  "   keyA = " << keyA ;
        
        cout << ss.str() << endl;

        img.fill(255); // fill the background with white
        
        // Functions to draw: 
        
        // CImg<T>& CImg<T>::draw_line(const int x0, const int y0,
        //       const int x1, const int y1, const tc ∗const color,
        //       const float opacity=1, const unsigned int
        //       pattern=∼0U, const bool init_hatch=true)

        img.draw_line(10, 20, main_disp.width() - 10, 20, red);
        
        // CImg<T>& CImg<T>::draw_triangle (const int x0, const int
        //       y0, const int x1, const int y1, const int x2, const
        //       int y2, const tc ∗const color, const float opacity =
        //       1 )

        img.draw_triangle(10, 80, 80, 80, 45, 30, blue);

        // CImg<T>& CImg<T>::draw_circle (const int x0, const int y0,
        //       int radius, const tc ∗const color, const float
        //       opacity = 1 )

        img.draw_circle(200, 50, 20, green);

        // CImg<T>& CImg<T>::draw_rectangle (const int x0, const int
        //       y0, const int z0, const int x1, const int y1, const
        //       int z1, const tc ∗const color, const float opacity =
        //       1 )

        img.draw_rectangle(300, 30, 0, 380, 80, 0, yellow);
        
        // Example:

        img.draw_text(main_disp.width()/2 - 200, // x position of the text
                                5,                         // y position of the text
                                ss.str().c_str(),          // the text as a c string
                                black,0,1,16);             // pen color, background color, etc.

        if (button_status)
            img.draw_rectangle(x-50, y-25, x+50, y+25, green);

        img.display(main_disp);
        main_disp.wait(10);           // wait for 10 milliseconds
    }
    cout << "End!!" << endl;
}
