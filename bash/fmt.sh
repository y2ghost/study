#!/bin/bash

while getopts "hw:" opt; do
    case $opt in
        h) hyph=1 ;;
        w) width="$OPTARG" ;;
    esac
done

shift $(($OPTIND - 1))
nroff << CMDS
.ll ${width:-72}
.na
.hy ${hyph:-0}
.pl 1
$(cat "$@")
CMDS
    
exit 0
