#!/bin/bash

IFS=":"
count=0
nonex=0

for d in $PATH; do
    if [ -d "$d" ]; then
        for cmd in "$d"/*; do
            if [ -x "$cmd" ]; then
                count=$(($count + 1))
            else
                nonex=$(($nonex + 1))
            fi
        done
    fi
done

echo "$count commands, and $nonex entries that weren't executable"
exit 0
