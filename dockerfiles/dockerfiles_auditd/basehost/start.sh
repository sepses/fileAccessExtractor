sshpass -p 'root' ssh -o 'StrictHostKeyChecking no' dockerfiles_virtuoso_1 'screen -d -m /bin/start.sh' && sshpass -p 'root' ssh -o 'StrictHostKeyChecking no' dockerfiles_agent_1 'screen -d -m /usr/local/bin/ran_cmd_734232643_1.sh'