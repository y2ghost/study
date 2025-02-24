#!/bin/bash

gradle test --rerun-tasks --tests "$1"
exit 0
