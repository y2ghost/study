#!/bin/bash

freq=60
maxshots=30
animate=0

while getopts "af:m:" opt; do
    case $opt in
        a) animate=1 ;;
        f) freq="$OPTARG" ;;
        m) maxshots="$OPTARG" ;;
        ?) echo "Usage: $0 [-a] [-f frequency] [-m maxcaps]" >&2
           exit 1
    esac
done

counter=0
capture="$(which screencapture) -x -m -C"

while [ $counter -lt $maxshots ]; do
    $capture screen${counter}.png
    counter=$((counter + 1))
    sleep $freq
done

if [ $animate -eq 1 ]; then
    convert -delay 100 -loop 0 -resize "33%" screen*.png animated-screen.gif
fi

exit 0
