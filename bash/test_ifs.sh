#!/bin/bash

IFS_OLD=$IFS
IFS=$'\n'

for entry in $(cat /etc/passwd)
do
    echo "条目: $entry"
    IFS=:
    for value in $entry
    do
        echo "    $value"
    done
done

