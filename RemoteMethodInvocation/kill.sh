#!/usr/bin/env bash
username=sd0407
password=sportingsempre
url=http://192.168.8.171/sd0407/classes/
registryHostName=l040101-ws01.ua.pt
GeneralRepositoryHostName=l040101-ws03.ua.pt
MuseumHostName=l040101-ws04.ua.pt
ConcentrationHostName=l040101-ws05.ua.pt
AssaultPartyHostName=l040101-ws06.ua.pt
ColectionHostName=l040101-ws07.ua.pt
ThievesHostName=l040101-ws08.ua.pt
MasterThiefHostName=l040101-ws09.ua.pt
registryPortNum=22460
kill `lsof -t -i:22460`
kill `lsof -t -i:22461`
kill `lsof -t -i:22462`
kill `lsof -t -i:22463`
kill `lsof -t -i:22464`
kill `lsof -t -i:22465`
kill `lsof -t -i:22466`
kill `lsof -t -i:22467`
kill `lsof -t -i:22468`
kill `lsof -t -i:22469`

sshpass -p $password ssh $username@$registryHostName -o StrictHostKeyChecking=no "kill -13 \$(netstat -tlnp | awk '/:2246 */ {split(\$NF,a,\"/\"); print a[1]}')"
sshpass -p $password ssh $username@$GeneralRepositoryHostName -o StrictHostKeyChecking=no "kill -13 \$(netstat -tlnp | awk '/:2246 */ {split(\$NF,a,\"/\"); print a[1]}')"
sshpass -p $password ssh $username@$MuseumHostName -o StrictHostKeyChecking=no "kill -13 \$(netstat -tlnp | awk '/:2246 */ {split(\$NF,a,\"/\"); print a[1]}')"
sshpass -p $password ssh $username@$ConcentrationHostName -o StrictHostKeyChecking=no "kill -13 \$(netstat -tlnp | awk '/:2246 */ {split(\$NF,a,\"/\"); print a[1]}')"
sshpass -p $password ssh $username@$AssaultPartyHostName -o StrictHostKeyChecking=no "kill -13 \$(netstat -tlnp | awk '/:2246 */ {split(\$NF,a,\"/\"); print a[1]}')"
sshpass -p $password ssh $username@$ColectionHostName -o StrictHostKeyChecking=no "kill -13 \$(netstat -tlnp | awk '/:2246 */ {split(\$NF,a,\"/\"); print a[1]}')"
sshpass -p $password ssh $username@$ThievesHostName -o StrictHostKeyChecking=no "kill -13 \$(netstat -tlnp | awk '/:2246 */ {split(\$NF,a,\"/\"); print a[1]}')"
sshpass -p $password ssh $username@$MasterThiefHostName -o StrictHostKeyChecking=no "kill -13 \$(netstat -tlnp | awk '/:2246 */ {split(\$NF,a,\"/\"); print a[1]}')"
