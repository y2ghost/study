#!/bin/sh

validint()
{
    number="$1"
    min="$2"
    max="$3"

    if [ -z $number ]; then
        echo "You didn't enter anything." >&2; return 1
    fi

    if [ "${number:0:1}" = "-" ]; then
        testvalue="${number#?}"
    else
        testvalue="$number"
    fi

    nodigits="$(echo $testvalue | sed 's/[[:digit:]]//g')"
    if [ ! -z $nodigits ]; then
        echo "Invalid number format! Only digits, no commas, spaces, etc."
        return 1
    fi

    if [ ! -z $min ]; then
        if [ "$number" -lt "$min" ]; then
            echo "Your value is too small: smallest acceptable is $min" >&2
            return 1
        fi
    fi

    if [ ! -z $max ]; then
        if [ "$number" -gt "$max" ]; then
            echo "Your value is too big: largest acceptable is $max" >&2
            return 1
        fi
    fi

    return 0
}

if validint "$1" "$2" "$3"; then
    echo "That input is a valid integer value within your constaints"
fi

exit 0
