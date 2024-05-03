#ifndef BECOME_DAEMON_H
#define BECOME_DAEMON_H

// 不需要执行chdir("/")
#define BD_NO_CHDIR             01

// 不需要关闭所有打开的文件
#define BD_NO_CLOSE_FILES       02

// 不需要重新打开stdin, stdoout, stderr 为/dev/null
#define BD_NO_REOPEN_STD_FDS    04  

// 不需要执行umask(0)
#define BD_NO_UMASK0            010

#define BD_MAX_CLOSE            8192

int becomeDaemon(int flags);

#endif
