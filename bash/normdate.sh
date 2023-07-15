#!/bin/bash

monthNumToName()
{
    case $1 in
        1) month="Jan";;    2) month="Feb";;
        3) month="Mar";;    4) month="Apr";;
        5) month="May";;    6) month="Jun";;
        7) month="Jul";;    8) month="Aug";;
        9) month="Sep";;    10) month="Oct";;
        11) month="Nov";;   12) month="Dec";;
        *) echo "$0: unknown month value $1" >&2
           exit 1
    esac
    return 0
}

if [ $# -ne 3 ]; then
    echo "usage: $0 month day year" >&2
    echo "formats are Nov 11 1987 and 11 11 1987" >&2
    exit 1
fi

if [ $3 -le 99 ]; then
    echo "$0:must 4-digit year." >&2
    exit 1
fi

if [ -z $(echo $1 | sed 's/[[:digit:]]//g') ]; then
    monthNumToName $1
else
    month="$(echo $1 | cut -c1 | tr '[:lower:]' '[:upper:]')"
    month="$month$(echo $1 | cut -c2-3 | tr '[:upper:]' '[:lower:]')"
fi

echo $month $2 $3
exit 0
