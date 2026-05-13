#!/bin/sh

homedir="/home"         # home directory for users
secs=10                 # seconds before user is logged out

if [ -z $1 ] ; then
    echo "Usage: $0 account" >&2 ; exit 1
elif [ "$(whoami)" != "root" ] ; then
    echo "Error. You must be 'root' to run this command." >&2; exit 1
fi

echo "Please change account $1 password to something new."
passwd $1

if who|grep "$1" > /dev/null ; then
    tty="$(who | grep $1 | tail -1 | awk '{print $2}')"
    cat << "EOF" > /dev/$tty

*************************************************************
URGENT NOTICE FROM THE ADMINISTRATOR:
This account is being suspended at the request of management.
You are going to be logged out in $secs seconds. Please immediately
shut down any processes you have running and log out.

If you have any questions, please contact your supervisor or
John Doe, Director of Information Technology.
*************************************************************

EOF

    echo "(Warned $1, now sleeping $secs seconds)"
    sleep $secs
    jobs=$(ps -u $1 | sed -e '1d' | awk '{print $1}')
    kill -s HUP  $jobs                  # send hangup sig to their processes
    sleep 1                             # give it a second...
    kill -s KILL $jobs > /dev/null 2>&1 # and kill anything left
    echo "$1 was logged in. Just logged them out."
fi

chmod 000 $homedir/$1
echo "Account $1 has been suspended."
exit 0
