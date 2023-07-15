#!/bin/sh

validAlphaNum()
{
    validchars="$(echo $1 | sed -e 's/[^[:alnum:]]//g')"

    if [ "$validchars" != "$input" ]; then
        return 1
    else
        return 0
    fi
}

/bin/echo -n "Enter input: "
read input

if ! validAlphaNum "$input"; then
    echo "Your input must consist of only letters and nubmers." >&2
    exit 1
else
    echo "Input is valid."
fi

exit 0
