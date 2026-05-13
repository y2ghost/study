#include "common.h"
#include <sys/capability.h>

int main(int argc, char *argv[])
{
    if (argc != 3) {
        fprintf(stderr, "%s <textual-cap-set> <pathname>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    cap_t capSets = cap_from_text(argv[1]);
    if (capSets == NULL) {
        err_sys("cap_from_text");
    }

    char *textCaps = cap_to_text(capSets, NULL);
    if (textCaps == NULL) {
        err_sys("cap_to_text");
    }

    printf("caps_to_text() returned \"%s\"\n\n", textCaps);
    if (cap_set_file(argv[2], capSets) == -1) {
        err_sys("cap_set_file");
    }

    if (cap_free(textCaps) != 0 || cap_free(capSets) != 0) {
        err_sys("cap_free");
    }

    exit(EXIT_SUCCESS);
}

