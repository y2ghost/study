#include <signal.h>
#include <sys/time.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>

static volatile sig_atomic_t got_alarm = 0;

static void _display_times(const char *msg, int include_timer)
{
    struct itimerval itv;
    struct timeval curr;
    static struct timeval start;
    static int call_num = 0;

    if (0 == call_num) {
        gettimeofday(&start, NULL);
    }

    if (0 == call_num % 20) {
        printf(" Elapsed Value Interval\n");
    }

    gettimeofday(&curr, NULL);
    printf("%-7s Elapsed(%6.2f)", msg, curr.tv_sec - start.tv_sec +
        (curr.tv_usec - start.tv_usec) / 1000000.0);

    if (0 != include_timer) {
        getitimer(ITIMER_REAL, &itv);
        printf(" Value(%6.2f) Interlval(%6.2f)",
            itv.it_value.tv_sec + itv.it_value.tv_usec / 1000000.0,
            itv.it_interval.tv_sec + itv.it_interval.tv_usec / 1000000.0);
    }

    printf("\n");
}

static void _sigalrm_handler(int sig)
{
    got_alarm = 1;
}

int main(int ac, char *av[])
{
    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = _sigalrm_handler;
    sigaction(SIGALRM, &sa, NULL);

    struct itimerval itv;
    itv.it_value.tv_sec = 2;
    itv.it_value.tv_usec = 0;
    itv.it_interval.tv_sec = 3;
    itv.it_interval.tv_usec = 0;
    _display_times("START:", 0);
    setitimer(ITIMER_REAL, &itv, 0);

    int sig_cnt = 0;
    int max_sigs = 3;
    clock_t prev_clock = clock();

    while (1) {
        while (((clock()-prev_clock) * 10 / CLOCKS_PER_SEC) < 5) {
            if (0 != got_alarm) {
                got_alarm = 0;
                _display_times("ALARM:", 1);
                sig_cnt++;

                if (sig_cnt >= max_sigs) {
                    printf("That's all folks\n");
                    exit(0);
                }
            }
        }

        prev_clock = clock();
        _display_times("Main ", 1);
    }

    return 0;
}
