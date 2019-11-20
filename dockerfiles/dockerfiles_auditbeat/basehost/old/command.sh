#!/bin/sh

#cd /home &&
#mkdir fileAccess &&
#cd fileAccess &&
#touch test.txt &&
#cp test.txt copy_of_test.txt &&
#mv test.txt ren_test.txt &&
#rm ren_test.txt
cd /home &&
touch test2.txt &&
#sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_1:/home &&
#sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_2:/home &&
#sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_3:/home &&
#sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_4:/home &&
#sshpass -p 'root' scp -o "StrictHostKeyChecking no" test2.txt dockerfiles_agent_5:/home &&
tail -f /dev/null