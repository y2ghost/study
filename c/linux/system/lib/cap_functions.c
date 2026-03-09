#include "cap_functions.h"
#include <sys/prctl.h>
#include <linux/securebits.h>

int modifyCapSetting(cap_flag_t flag, int capability, int setting)
{
    cap_t caps = cap_get_proc();
    if (caps == NULL) {
        return -1;
    }

    cap_value_t capList[1];
    capList[0] = capability;

    if (cap_set_flag(caps, flag, 1, capList, setting) == -1) {
        cap_free(caps);
        return -1;
    }

    if (cap_set_proc(caps) == -1) {
        cap_free(caps);
        return -1;
    }

    if (cap_free(caps) == -1) {
        return -1;
    }

    return 0;
}

void printSecbits(int secbits, bool verbose, FILE *fp)
{
    struct secbitInfo {
        int   flag;
        char *name;
        char  letter;
    };

    struct secbitInfo secbitInfoList[] = {
        {SECBIT_NO_CAP_AMBIENT_RAISE, "NO_CAP_AMBIENT_RAISE", 'a'},
        {SECBIT_NO_CAP_AMBIENT_RAISE_LOCKED, "NO_CAP_AMBIENT_RAISE_LOCKED", 'A'},
        {SECBIT_KEEP_CAPS, "KEEP_CAPS", 'k'},
        {SECBIT_KEEP_CAPS_LOCKED, "KEEP_CAPS_LOCKED", 'K'},
        {SECBIT_NOROOT, "NOROOT", 'r'},
        {SECBIT_NOROOT_LOCKED, "NOROOT_LOCKED", 'R'},
        {SECBIT_NO_SETUID_FIXUP, "NO_SETUID_FIXUP", 's'},
        {SECBIT_NO_SETUID_FIXUP_LOCKED, "NO_SETUID_FIXUP_LOCKED", 'S'},
        {0, NULL, '\0'}
    };

    int printed = 0;
    if (verbose) {
        fprintf(fp, "[");
        for (struct secbitInfo *p = secbitInfoList; p->flag != 0; p++) {
            if (secbits & p->flag) {
                if (printed > 0) {
                    fprintf(fp, ", ");
                }

                fprintf(fp, "%s", p->name);
                printed++;
            }
        }

        fprintf(fp, "]");
    } else {
        for (struct secbitInfo *p = secbitInfoList; p->flag != 0; p++) {
            if (secbits & p->flag) {
                fputc(p->letter, fp);
            } else {
                fputc('-', fp);
            }
        }
    }
}
