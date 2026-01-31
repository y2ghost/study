#!/usr/bin/env sh

var=test_for_char
while [ -n "$var" ]; do
    temp=${var#?}
    ch=${var%"$temp"}
    printf "%c\n" $ch
    var=$temp
done

exit 0
