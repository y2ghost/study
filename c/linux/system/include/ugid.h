#ifndef _UGID_H_
#define _UGID_H_

#include <sys/types.h>

char *get_name_byuid(uid_t uid);
uid_t get_uid_byname(const char *name);

char *get_gname_bygid(gid_t gid);
gid_t get_gid_bygname(const char *gname);

#endif /* _UGID_H_ */
