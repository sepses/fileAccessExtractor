#!/bin/sh
cd /home && echo 'go to /home'
touch nullnull && echo 'create file nullnull'
sleep null && echo 'sleep for null second(s)' 
mv nullnull ren_nullnull && echo 'rename nullnullto ren_nullnull'
sleep null && echo 'sleep for null second(s)' 
cp ren_nullnull copy_of_ren_nullnull && echo 'copy ren_nullnull to copy_of_ren_nullnull'
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_nullnull dockerfiles_agent_1:/home
sleep null && echo 'sleep for null second(s)' 
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_nullnull dockerfiles_agent_2:/home
sleep null && echo 'sleep for null second(s)' 
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_nullnull dockerfiles_agent_3:/home
sleep null && echo 'sleep for null second(s)' 
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_nullnull dockerfiles_agent_4:/home
sleep null && echo 'sleep for null second(s)' 
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_nullnull dockerfiles_agent_5:/home
sleep null && echo 'sleep for null second(s)' 
sleep null && echo 'sleep for null second(s)' 
rm ren_nullnull && echo 'remove ren_nullnull' 
tail -f