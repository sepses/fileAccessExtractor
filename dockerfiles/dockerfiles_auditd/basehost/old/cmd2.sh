#!/bin/sh
cd /home && echo 'go to /home'
touch test.doc && echo 'create file test.doc'
sleep 2 && echo 'sleep for 2 second(s)' 
mv test.doc ren_test.doc && echo 'rename test.docto ren_test.doc'
sleep 2 && echo 'sleep for 2 second(s)' 
cp test.doc copy_of_test.doc && echo 'copy test.doc to copy_of_test.doc'
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_test.doc dockerfiles_agent_1:/home
sleep 4 && echo 'sleep for 4 second(s)' 
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_test.doc dockerfiles_agent_2:/home
sleep 4 && echo 'sleep for 4 second(s)' 
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_test.doc dockerfiles_agent_3:/home
sleep 4 && echo 'sleep for 4 second(s)' 
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_test.doc dockerfiles_agent_4:/home
sleep 4 && echo 'sleep for 4 second(s)' 
sshpass -p 'root' scp -o "StrictHostKeyChecking no" ren_test.doc dockerfiles_agent_5:/home
sleep 4 && echo 'sleep for 4 second(s)' 
sleep 2 && echo 'sleep for 2 second(s)' 
rm ren_test.doc && echo 'remove ren_test.doc' 
tail -f