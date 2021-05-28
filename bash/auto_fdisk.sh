#!/bin/bash
# author: yyghost
# date: 2017-09-08

export PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin
export LANG=en_US.UTF-8

# check disk partition
check_disk() 
{
    # clear lockfile, it temporay store uninit disks
    > $lockfile
    for i in $(fdisk -l 2> /dev/null |
        grep -oE 'Disk /dev/x?[hsv]d[a-z]' | awk '{print $2}')
    do
        # first exclude dos partitions
        # the check if the device mounted, and have first partition
        # in the /proc/partitions
        if [ -z "$(blkid | grep -v 'TYPE="dos"' | grep -w "$i")" ]; then 
            if [ -z "$(mount | grep "$i")" -a \
                -z "$(cat /proc/partitions | awk '{print $4}' | \
                    grep -w "${i}1")" ]; then
                echo $i >> $lockfile
            fi
        fi
    done

    DISK_LIST=$(cat $lockfile)
    if [ "X$DISK_LIST" == "X" ]; then
        echo
        echo "no disk need to be fdisk!"
        echo
        rm -rf $lockfile
        exit 0
    fi
}

# use fdisk to create and format just one primary partition
# do not to delete the empty lines, they are important input
auto_fdisk() {
fdisk "$1" << EOF
n
p
1


wq

EOF

# wait sync partitions table
sleep 5
mkfs.ext4 ${1}1
}

main() {
    # check if user is super
    [ $(id -u) != "0" ] && {
        echo "You must be root to run this script!"; exit 1;
    }

    # check lock file
    lockfile=/tmp/.$(basename $0).$$
    if [ -f "$lockfile" ];then
        echo
        echo "Already running, please next time to run me!"
        echo
        exit 1
    fi

    touch $lockfile
    check_disk

    for i in `echo $DISK_LIST`
    do
        echo
        echo "auto fdisk: $i"
        auto_fdisk $i > /dev/null 2>&1
    done

    rm -rf $lockfile
    exit 0
}

main
