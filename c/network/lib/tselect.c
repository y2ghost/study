#include <etcp.h>
#include <sys/time.h>

const int NTIMERS = 25;

typedef struct tevent_t tevent_t;

struct tevent_t {
    tevent_t *next;
    struct timeval tv;
    void (*func)(void *arg);
    void *arg;
    unsigned int id;
};

static tevent_t *active_list = NULL;
static tevent_t *inactive_list = NULL;

static tevent_t *allocate_timer(void)
{
    tevent_t *tp = NULL;

    if (NULL == inactive_list) {
        inactive_list = malloc(NTIMERS * sizeof(*inactive_list));
        if (NULL == inactive_list) {
            error(1, 0, "couldn't allocate timers\n");
        }

        for (tp=inactive_list; tp<inactive_list+NTIMERS-1; tp++) {
            tp->next = tp + 1;
        }

        tp->next = NULL;
    }

    tp = inactive_list;
    inactive_list = tp->next;
    return tp;
}

unsigned int timeout(tofunc_t func, void *arg, int ms)
{
    tevent_t *tp = NULL;
    tevent_t *tcur = NULL;
    tevent_t **tprev = NULL;
    static unsigned int id = 1;

    tp = allocate_timer();
    tp->func = func;
    tp->arg = arg;

    if (gettimeofday(&tp->tv, NULL) < 0) {
        error(1, errno, "timeout: gettimeofday failure");
    }

    tp->tv.tv_usec += ms * 1000;
    if (tp->tv.tv_usec > 1000000) {
        tp->tv.tv_sec += tp->tv.tv_usec / 1000000;
        tp->tv.tv_usec %= 1000000;
    }

    tprev = &active_list;
    tcur = active_list;

    while (NULL != tcur) {
        if (0 != timercmp(&tp->tv, &tcur->tv, <)) {
            break;
        }

        tprev = &tcur->next;
        tcur = tcur->next;
    }

    *tprev = tp;
    tp->next = tcur;
    tp->id = id++;
    return tp->id;
}

void untimeout(unsigned int id)
{
    tevent_t **tprev = NULL;
    tevent_t *tcur = NULL;

    tprev = &active_list;
    tcur = active_list;

    while (NULL != tcur) {
        if (id == tcur->id) {
            break;
        }

        tprev = &tcur->next;
        tcur = tcur->next;
    }

    if (NULL == tcur) {
        error(0, 0,
            "untimeout called for non-existent timer (%d)\n", id);
        return;
    }

    *tprev = tcur->next;
    tcur->next = inactive_list;
    inactive_list = tcur;
}

int tselect(int maxp1, fd_set *re, fd_set *we, fd_set *ee)
{
    fd_set rmask;
    fd_set wmask;
    fd_set emask;
    struct timeval tv;
    struct timeval now;
    struct timeval *tvp = NULL;
    tevent_t *tp = NULL;
    int n = 0;

    FD_ZERO(&rmask);
    FD_ZERO(&wmask);
    FD_ZERO(&emask);

    if (NULL!= re) {
        rmask = *re;
    }

    if (NULL != we) {
        wmask = *we;
    }

    if (NULL != ee) {
        emask = *ee;
    }

    while (1) {
        if (gettimeofday(&now, NULL) < 0) {
            error(1, errno, "tselect: gettimeofday failure");
        }

        while (active_list && 0 == timercmp(&now, &active_list->tv, <)) {
            active_list->func(active_list->arg);
            tp = active_list;
            active_list = active_list->next;
            tp->next = inactive_list;
            inactive_list = tp;
        }

        if (NULL != active_list) {
            tv.tv_sec = active_list->tv.tv_sec - now.tv_sec;;
            tv.tv_usec = active_list->tv.tv_usec - now.tv_usec;

            if (tv.tv_usec < 0) {
                tv.tv_usec += 1000000;
                tv.tv_sec--;
            }

            tvp = &tv;
        } else if (re == NULL && we == NULL && ee == NULL) {
            return 0;
        } else {
            tvp = NULL;
        }

        n = select(maxp1, re, we, ee, tvp);
        if (n < 0) {
            return -1;
        }

        if (n > 0) {
            return n;
        }

        if (NULL != re) {
            *re = rmask;
        }

        if (NULL != we) {
            *we = wmask;
        }

        if (NULL != ee) {
            *ee = emask;
        }
    }
}
