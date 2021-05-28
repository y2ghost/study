#include    <setjmp.h>
#include    <signal.h>
#include    <unistd.h>

static jmp_buf env_alrm;

static void sig_alrm(int signo)
{
    longjmp(env_alrm, 1);
}

unsigned int sleep2(unsigned int seconds)
{
    if (SIG_ERR == signal(SIGALRM, sig_alrm)) {
        return seconds;
    }

    if (0 == setjmp(env_alrm)) {
        alarm(seconds);     /* start the timer */
        pause();            /* next caught signal wakes us up */
    }

    return alarm(0);       /* turn off timer, return unslept time */
}
