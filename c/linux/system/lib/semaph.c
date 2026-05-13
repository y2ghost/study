#include "common.h"
#include <sys/ipc.h>
#include <sys/sem.h>
#include <errno.h>

#define BIGCOUNT    10000

void sem_op(int, int);
int sem_create(key_t, int);
int sem_open(key_t);
void sem_rm(int);
void sem_close(int);
void sem_wait(int);
void sem_signal(int);

static struct sembuf op_lock[2] = {
    {2, 0, 0},
    {2, 1, SEM_UNDO},
};

static struct sembuf op_endcreate[2] = {
    {1, -1, SEM_UNDO},
    {2, -1, SEM_UNDO},
};

static struct sembuf op_open[1] = {
    {1, -1, SEM_UNDO},
};

static struct sembuf op_close[3] = {
    {2, 0, 0},
    {2, 1, SEM_UNDO},
    {1, 1, SEM_UNDO},
};

static struct sembuf op_unlock[1] = {
    {2, -1, SEM_UNDO},
};

static struct sembuf op_op[1] = {
    {0, 99, SEM_UNDO},
};

int sem_create(key_t key, int initval)
{
    register int id = 0;
    register int semval = 0;
    union semun {
        int val;
        struct semid_ds *buf;
        unsigned short *array;
    } semctl_arg;

    if (IPC_PRIVATE == key) {
        return -1;
    } else if (-1 == key) {
        return -1;
    }

    while (1) {
        id = semget(key, 3, 0666 | IPC_CREAT);
        if (id < 0) {
            return -1;
        }

        if (semop(id, &op_lock[0], 2) < 0) {
            if (EINVAL == errno) {
                continue;
            }
            
            err_sys("can't lock");
        }

        break;
    }

    semval = semctl(id, 1, GETVAL, 0);
    if (semval < 0) {
        err_sys("can't GETVAL");
    }

    if (0 == semval) {
        semctl_arg.val = initval;
        if (semctl(id, 0, SETVAL, semctl_arg) < 0) {
            err_sys("can SETVAL[0]");
        }

        semctl_arg.val = BIGCOUNT;
        if (semctl(id, 1, SETVAL, semctl_arg) < 0) {
            err_sys("can SETVAL[1]");
        }
    }

    if (semop(id, &op_endcreate[0], 2) < 0) {
        err_sys("can't end create");
    }

    return id;
}

int sem_open(key_t key)
{
    register int id = 0;

    if (IPC_PRIVATE == key) {
        return -1;
    } else if (-1 == key) {
        return -1;
    }

    id = semget(key, 3, 0);
    if (id < 0) {
        return -1;
    }

    if (semop(id, &op_open[0], 1) < 0) {
        err_sys("can't open");
    }

    return id;
}

void sem_rm(int id)
{
    if (semctl(id, 0, IPC_RMID, 0) < 0) {
        err_sys("can't IPC_RMID");
    }
}

void sem_close(int id)
{
    register int semval = 0;

    if (semop(id, &op_close[0], 3) < 0) {
        err_sys("can't semop");
    }

    semval = semctl(id, 1, GETVAL, 0);
    if (semval < 0) {
        err_sys("can't GETVAL");
    }

    if (semval > BIGCOUNT) {
        err_dump("sem[1] > BIGCOUNT");
    } else if (BIGCOUNT == semval) {
        sem_rm(id);
    } else {
        if (semop(id, &op_unlock[0], 1) < 0) {
            err_sys("can't unlock");
        }
    }
}

void sem_wait(int id)
{
    sem_op(id, -1);
}

void sem_signal(int id)
{
    sem_op(id, 1);
}

void sem_op(int id, int value)
{
    if (0 == (op_op[0].sem_op = value)) {
        err_sys("can't have value == 0");
    }

    if (semop(id, &op_op[0], 1) < 0) {
        err_sys("sem_op error");
    }
}

