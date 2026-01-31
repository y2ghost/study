#!/bin/sh

echon1()
{
    echo "$*" | awk '{printf "%s", $0}'
}

echon2()
{
    printf "%s" "$*"
}

echon3()
{
    echo "$*" | tr -d '\n'
}

echon1 "$*"
echon2 "$*"
echon3 "$*"
