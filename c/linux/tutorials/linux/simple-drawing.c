#include <X11/Xlib.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

Window create_simple_window(Display* display, int width, int height, int x, int y)
{
    int screen_num = DefaultScreen(display);
    int win_border_width = 2;
    Window win;

    win = XCreateSimpleWindow(display, RootWindow(display, screen_num),
        x, y, width, height, win_border_width,
        BlackPixel(display, screen_num),
        WhitePixel(display, screen_num));
    /* make the window actually appear on the screen. */
    XMapWindow(display, win);
    /* flush all pending requests to the X server. */
    XFlush(display);
    return win;
}

GC create_gc(Display* display, Window win, int reverse_video)
{
    GC gc;
    unsigned long valuemask = 0;
    XGCValues values;
    unsigned int line_width = 2;
    int line_style = LineSolid;
    int cap_style = CapButt;
    int join_style = JoinBevel;
    int screen_num = DefaultScreen(display);

    gc = XCreateGC(display, win, valuemask, &values);
    if (gc < 0) {
        fprintf(stderr, "XCreateGC: \n");
    }

    /* allocate foreground and background colors for this GC. */
    if (reverse_video) {
        XSetForeground(display, gc, WhitePixel(display, screen_num));
        XSetBackground(display, gc, BlackPixel(display, screen_num));
    } else {
        XSetForeground(display, gc, BlackPixel(display, screen_num));
        XSetBackground(display, gc, WhitePixel(display, screen_num));
    }

    /* define the style of lines that will be drawn using this GC. */
    XSetLineAttributes(display, gc,
        line_width, line_style, cap_style, join_style);
    /* define the fill style for the GC. to be 'solid filling'. */
    XSetFillStyle(display, gc, FillSolid);
    return gc;
}

int main(int argc, char* argv[])
{
    GC gc;
    Window win;
    Display* display = NULL;
    int screen_num = 0;
    unsigned display_width = 0;
    unsigned display_height = 0;
    unsigned width = 0;
    unsigned height = 0;
    char *display_name = getenv("DISPLAY");

    /* open connection with the X server. */
    display = XOpenDisplay(display_name);
    if (NULL == display) {
        fprintf(stderr, "%s: cannot connect to X server '%s'\n",
            argv[0], display_name);
        exit(1);
    }

    /* get the geometry of the default screen for our display. */
    screen_num = DefaultScreen(display);
    display_width = DisplayWidth(display, screen_num);
    display_height = DisplayHeight(display, screen_num);
    /* make the new window occupy 1/9 of the screen's size. */
    width = (display_width / 3);
    height = (display_height / 3);
    printf("window width - '%d'; height - '%d'\n", width, height);
    /* create a simple window, as a direct child of the screen's   */
    /* root window. Use the screen's white color as the background */
    /* color of the window. Place the new window's top-left corner */
    /* at the given 'x,y' coordinates.                             */
    win = create_simple_window(display, width, height, 0, 0);
    /* allocate a new GC (graphics context) for drawing in the window. */
    gc = create_gc(display, win, 0);
    XSync(display, False);
    /* draw one pixel near each corner of the window */
    XDrawPoint(display, win, gc, 5, 5);
    XDrawPoint(display, win, gc, 5, height-5);
    XDrawPoint(display, win, gc, width-5, 5);
    XDrawPoint(display, win, gc, width-5, height-5);
    /* draw two intersecting lines, one horizontal and one vertical, */
    /* which intersect at point "50,100".                            */
    XDrawLine(display, win, gc, 50, 0, 50, 200);
    XDrawLine(display, win, gc, 0, 100, 200, 100);
    /* now use the XDrawArc() function to draw a circle whose diameter */
    /* is 30 pixels, and whose center is at location '50,100'.         */
    XDrawArc(display, win, gc, 50-(30/2), 100-(30/2), 30, 30, 0, 360*64);

    {
        XPoint points[] = {
            {0, 0},
            {15, 15},
            {0, 15},
            {0, 0}
        };
        int npoints = sizeof(points)/sizeof(XPoint);

        XDrawLines(display, win, gc, points, npoints, CoordModeOrigin);
    }

    /* draw a rectangle whose top-left corner is at '120,150', its width is */
    /* 50 pixels, and height is 60 pixels.                                  */
    XDrawRectangle(display, win, gc, 120, 150, 50, 60);
    /* draw a filled rectangle of the same size as above, to the left of the */
    /* previous rectangle.                                                   */
    XFillRectangle(display, win, gc, 60, 150, 50, 60);
    /* flush all pending requests to the X server. */
    XFlush(display);
    XSync(display, False);
    /* make a delay for a short period. */
    sleep(4);
    /* close the connection to the X server. */
    XCloseDisplay(display);
    return 0;
}
