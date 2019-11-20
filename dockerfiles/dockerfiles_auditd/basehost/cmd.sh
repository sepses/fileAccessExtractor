#!/bin/sh
cd /home &&
touch test2.txt &&
sleep 5 &&
sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_1:/home &&
sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_2:/home &&
sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_3:/home &&
sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_4:/home &&
sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_5:/home &&
tail -f /dev/null