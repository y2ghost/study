#!/bin/bash

TITLE="cron backup report for $(hostname -s) from $(date +%Y%m%d:%T)"
FROM=root@test.local
EMAIL=test@t.cn
SOURCE=~/source
DEST=~/dest
LFPATH=/tmp
LOG=/tmp/$(date +%Y%m%d_%T)_backup.log

rsync --delete --log-file=$LOG -avzq $SOURCE $DEST
(echo "$TITLE"; echo; cat $LOG) | sendmail -f$FROM -t $EMAIL
exit 0
