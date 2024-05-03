#!/bin/bash

in_path()
{
    cmd=$1
    ourpath=$2
    result=1
    oldIFS=$IFS
    IFS=":"

    for d in $ourpath; do
        if [ -x $d/$cmd ]; then
            result=0
        fi
    done

    IFS=$oldIFS
    return $result
}

check_cmd()
{
    var=$1
    if [ "$var" != "" ]; then
        if [ "${var:0:1}" = "/" ]; then
            if [ ! -x $var ]; then
                return 1
            fi
        elif ! in_path $var "$PATH"; then
            return 2
        fi
    fi
}

if [ $# -ne 1 ]; then
    echo "usage: $0 cmmand" >&2
    exit 1
fi

check_cmd "$1"
case $? in
0 ) echo "$1 found in PATH"                   ;;
1 ) echo "$1 not found or not executable"     ;;
2 ) echo "$1 not found in PATH"               ;;
esac

exit 0
