#include <etcp.h>
#include <sys/shm.h>
#include <sys/sem.h>

#define SM_KEY      0x534d424d  /* SMBM */
#define MUTEX_KEY   0x534d4253  /* SMBS */
#define FREE_LIST   smbarray[NSMB].nexti

typedef union {
    int nexti;
    char buf[SMBUFSZ];
} smb_t;

int mutex = 0;
smb_t *smbarray = NULL;
struct sembuf lkbuf;
struct sembuf unlkbuf;

inline void lock_buf()
{
    if (semop(mutex, &lkbuf, 1) < 0) {
        error(1, errno, "semop failed");
    }
}

inline void unlock_buf()
{
    if (semop(mutex, &unlkbuf, 1) < 0) {
        error(1, errno, "semop failed");
    }
}

void init_smb(int init_freelist)
{
    union semun arg;
    int smid = 0;
    int i = 0;
    int rc = 0;

    lkbuf.sem_op = -1;
    lkbuf.sem_flg = SEM_UNDO;
    unlkbuf.sem_op = 1;
    unlkbuf.sem_flg = SEM_UNDO;
    mutex = semget(MUTEX_KEY, 1, IPC_EXCL|IPC_CREAT);

    if (mutex >= 0) {
        arg.val = 1;
        rc = semctl(mutex, 0, SETVAL, arg);

        if (rc < 0) {
            error(1, errno, "semctl failed");
        }
    } else if (errno == EEXIST) {
        mutex = semget(MUTEX_KEY, 1, IPC_EXCL|IPC_CREAT);
        if (mutex < 0) {
            error(1, errno, "semctl failed");
        }
    } else {
        error(1, errno, "semctl failed");
    }

    smid = shmget(SM_KEY, NSMB *sizeof(smb_t)+sizeof(int), SHM_R|SHM_W|IPC_CREAT);    
    if (smid < 0) {
        error(1, errno, "shmget failed");
    }

    smbarray = (smb_t*)shmat(smid, NULL, 0);
    if (smbarray == (void *)-1) {
        error(1, errno, "shmat failed");
    }

    if (init_freelist) {
        for (i=0; i<NSMB-1; i++) {
            smbarray[i].nexti = i + 1;
        }

        smbarray[NSMB-1].nexti = -1;
        FREE_LIST = 0;
    }
}

void *smballoc(void)
{
    smb_t *bp = NULL;

    lock_buf();
    if (FREE_LIST < 0) {
        error(1, 0, "out of shared memory buffers\n");
    }

    bp = smbarray + FREE_LIST;
    FREE_LIST = bp->nexti;
    unlock_buf();
    return bp;
}

void smbfree(void *smb)
{
    smb_t *bp = NULL;

    bp = smb;
    lock_buf();
    bp->nexti = FREE_LIST;
    FREE_LIST = bp - smbarray;
    unlock_buf();
}

void *smbrecv(int sock)
{
    int index = 0;
    int rc = 0;

    rc = readn(sock, (char*)&index, sizeof(index));
    if (rc == 0) {
        error(1, 0, "smbrecv: peer disconnected\n");
    } else if (rc != sizeof(index)) {
        error(1, errno, "smbrecv: readn failure");
    }

    return smbarray + index;
}

void smbsend(int sock, void *smb)
{
    int index = 0;

    index = (smb_t*)smb - smbarray;
    if (send(sock, (char*)&index, sizeof(index), 0) < 0) {
        error(1, errno, "smbsend: send failure");
    }
}
