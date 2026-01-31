#!/usr/bin/env sh

reverse_str()
{
    var=$1
    REVSTR=

    while [ -n "$var" ]; do
        temp=${var%?}
        REVSTR+=${var#"$temp"}
        var=$temp
    done
}

reverse_str $1
echo $REVSTR
