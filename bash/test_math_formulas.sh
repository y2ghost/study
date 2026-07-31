#!/bin/bash

x=10

if (( $x ** 2 > 90 ))
then
    (( q = $x ** 2 ))
    echo "$x^2 = $q"
fi

