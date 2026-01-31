#include <unistd.h>
#include <signal.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <errno.h>

int system(const char *command)
{
    if (command == NULL) {
        return system(":") == 0;
    }

    sigset_t blockMask;
    sigset_t origMask;
    sigemptyset(&blockMask);
    sigaddset(&blockMask, SIGCHLD);
    sigprocmask(SIG_BLOCK, &blockMask, &origMask);

    struct sigaction saIgnore;
    struct sigaction saOrigQuit;
    struct sigaction saOrigInt;
    struct sigaction saDefault;
    saIgnore.sa_handler = SIG_IGN;
    saIgnore.sa_flags = 0;
    sigemptyset(&saIgnore.sa_mask);
    sigaction(SIGINT, &saIgnore, &saOrigInt);
    sigaction(SIGQUIT, &saIgnore, &saOrigQuit);

    pid_t childPid = fork();
    int status = 0;

    switch (childPid) {
    case -1:
        status = -1;
        break;
    case 0:
        saDefault.sa_handler = SIG_DFL;
        saDefault.sa_flags = 0;
        sigemptyset(&saDefault.sa_mask);

        if (saOrigInt.sa_handler != SIG_IGN) {
            sigaction(SIGINT, &saDefault, NULL);
        }

        if (saOrigQuit.sa_handler != SIG_IGN) {
            sigaction(SIGQUIT, &saDefault, NULL);
        }

        sigprocmask(SIG_SETMASK, &origMask, NULL);
        execl("/bin/sh", "sh", "-c", command, (char *) NULL);
        _exit(127);
    default:
        while (waitpid(childPid, &status, 0) == -1) {
            if (errno != EINTR) {
                status = -1;
                break;
            }
        }
        break;
    }

    int savedErrno = errno;
    sigprocmask(SIG_SETMASK, &origMask, NULL);
    sigaction(SIGINT, &saOrigInt, NULL);
    sigaction(SIGQUIT, &saOrigQuit, NULL);
    errno = savedErrno;
    return status;
}

// 编写system函数示例
int main(int argc, char *argv[])
{
    system("ls");
    return 0;
}
