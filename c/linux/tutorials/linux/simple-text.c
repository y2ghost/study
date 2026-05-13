#include <X11/Xlib.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

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
    XGCValues values;
    int line_style = LineSolid;
    int cap_style = CapButt;
    int join_style = JoinBevel;
    int screen_num = DefaultScreen(display);
    unsigned line_width = 2;
    unsigned long valuemask = 0;

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
    XFontStruct* font_info = NULL;
    int screen_num = 0;
    unsigned display_width = 0;
    unsigned display_height = 0;
    unsigned win_width = 0;
    unsigned win_height = 0;
    char *display_name = getenv("DISPLAY");
    char* font_name = "*-helvetica-*-12-*";

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
    win_width = (display_width / 3);
    win_height = (display_height / 3);
    printf("window width - '%d'; height - '%d'\n", win_width, win_height);
    /* create a simple window, as a direct child of the screen's   */
    /* root window. Use the screen's white color as the background */
    /* color of the window. Place the new window's top-left corner */
    /* at the given 'x,y' coordinates.                             */
    win = create_simple_window(display, win_width, win_height, 0, 0);
    /* allocate a new GC (graphics context) for drawing in the window. */
    gc = create_gc(display, win, 0);
    XSync(display, False);
    /* try to load the given font. */
    font_info = XLoadQueryFont(display, font_name);

    if (NULL != font_info) {
        fprintf(stderr, "XLoadQueryFont: failed loading font '%s'\n", font_name);
        exit(-1);
    }

    /* assign the given font to our GC. */
    XSetFont(display, gc, font_info->fid);

    {
        /* variables used for drawing the text strings. */
        int x = 0;
        int y = 0;
        char* text_string = NULL;
        int string_width = 0;
        int font_height = 0;

        /* find the height of the characters drawn using this font.        */
        font_height = font_info->ascent + font_info->descent;
        /* draw a "hello world" string on the top-left side of our window. */
        text_string = "hello world";
        x = 0;
        y = font_height;
        XDrawString(display, win, gc, x, y, text_string, strlen(text_string));
        /* draw a "middle of the road" string in the middle of our window. */
        text_string = "middle of the road";
        /* find the width, in pixels, of the text that will be drawn using */
        /* the given font.                                                 */
        string_width = XTextWidth(font_info, text_string, strlen(text_string));
        x = (win_width - string_width) / 2;
        y = (win_height + font_height) / 2;
        XDrawString(display, win, gc, x, y, text_string, strlen(text_string));
    }

    /* flush all pending requests to the X server. */
    XFlush(display);
    /* make a delay for a short period. */
    sleep(4);
    /* close the connection to the X server. */
    XCloseDisplay(display);
    return 0;
}
