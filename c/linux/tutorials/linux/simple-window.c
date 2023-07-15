#include <X11/Xlib.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char* argv[])
{
    Display* display = NULL;
    int screen_num = 0;
    Window win;
    unsigned display_width = 0;
    unsigned display_height = 0;
    unsigned width = 0;
    unsigned height = 0;
    unsigned win_x = 0;
    unsigned win_y = 0;
    unsigned win_border_width = 0;
    char *display_name = getenv("DISPLAY");
  
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
    /* the window should be placed at the top-left corner of the screen. */
    win_x = 0;
    win_y = 0;
    /* the window's border shall be 2 pixels wide. */
    win_border_width = 2;
    /* create a simple window, as a direct child of the screen's   */
    /* root window. Use the screen's white color as the background */
    /* color of the window. Place the new window's top-left corner */
    /* at the given 'x,y' coordinates.                             */
    win = XCreateSimpleWindow(display, RootWindow(display, screen_num),
        win_x, win_y, width, height, win_border_width,
        BlackPixel(display, screen_num),
        WhitePixel(display, screen_num));
    /* make the window actually appear on the screen. */
    XMapWindow(display, win);
    /* flush all pending requests to the X server, and wait until */
    /* they are processed by the X server.                        */
    XSync(display, False);
    /* make a delay for a short period. */
    sleep(4);
    /* close the connection to the X server. */
    XCloseDisplay(display);
    return 0;
}
