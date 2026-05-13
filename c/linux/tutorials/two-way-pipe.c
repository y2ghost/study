#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <ctype.h>

void user_handler(int input_pipe[], int output_pipe[])
{
    int exit_code = 0;
    int read_char = '\0';
    int read_bytes = 0;
    char write_char = '\0';

    close(input_pipe[1]);
    close(output_pipe[0]);

    while ((read_char = getchar()) > 0) {
        write_char = read_char;
        read_bytes = write(output_pipe[1], &write_char, 1);
        
        if (-1 == read_bytes) {
            perror("user_handler: write");
            exit_code = 1;
            break;
        }
        
        read_bytes = read(input_pipe[0], &write_char, 1);
        read_char  = write_char;
        
        if (read_bytes < 0) {
            perror("user_handler: read");
            exit_code = 2;
            break;
        }

        putchar(read_char);
    }

    close(input_pipe[0]);
    close(output_pipe[1]);
    exit(exit_code);
}

/* now comes the function executed by the translator process. */
void translator(int input_pipe[], int output_pipe[])
{
    int c = 0;      /* user input - must be 'int', to recognize EOF (= -1). */
    int rc = 0;     /* return values of functions. */
    char ch = '\0'; /* the same - as a char. */

    /* first, close unnecessary file descriptors */
    close(input_pipe[1]); /* we don't need to write to this pipe.  */
    close(output_pipe[0]); /* we don't need to read from this pipe. */

    /* enter a loop of reading from the user_handler's pipe, translating */
    /* the character, and writing back to the user handler.              */
    while (1) {
        rc = read(input_pipe[0], &ch, 1);
        if (rc <= 0) {
            break;
        }
        
        c = ch;
        /* translate any upper-case letter to lowr-case. */
        if (isascii(c) && isupper(c)) {
            c = tolower(c);
        }

        ch = c;
        /* write translated character back to user_handler. */
        rc = write(output_pipe[1], &ch, 1);

        if (-1 == rc) {
            perror("translator: write");
            close(input_pipe[0]);
            close(output_pipe[1]);
            exit(1);
        }
    }

    /* close pipes and exit. */
    close(input_pipe[0]);
    close(output_pipe[1]);
    exit(0);
}

/* and finally, the main function: spawn off two processes, */
/* and let each of them execute its function.               */
int main(int argc, char* argv[])
{
    /* 2 arrays to contain file descriptors, for two pipes. */
    int user_to_translator[2] = {0, 0};
    int translator_to_user[2] = {0, 0};
    int pid = 0;       /* pid of child process, or 0, as returned via fork.    */
    int rc = 0;        /* stores return values of various routines.            */

    /* first, create one pipe. */
    rc = pipe(user_to_translator);
    if (-1 == rc) {
        perror("main: pipe user_to_translator");
        exit(1);
    }

    /* then, create another pipe. */
    rc = pipe(translator_to_user);
    if (-1 == rc) {
        perror("main: pipe translator_to_user");
        exit(1);
    }

    /* now fork off a child process, and set their handling routines. */
    pid = fork();
    switch (pid) {
    case -1:    /* fork failed. */
        perror("main: fork");
        exit(1);
    case 0:     /* inside child process.  */
        translator(user_to_translator, translator_to_user); /* line 'A' */
        /* NOT REACHED */
    default:    /* inside parent process. */
        user_handler(translator_to_user, user_to_translator); /* line 'B' */
        /* NOT REACHED */
    }

    return 0;   /* NOT REACHED */
}
