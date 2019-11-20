#!/bin/sh
cd /home &&
mkdir testdir1 &&
cd testdir1 &&
touch test1.txt &&
ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no dockerfiles_app_2 mkdir /home/test2 &&
expect "*?assword:*"
send "root"
