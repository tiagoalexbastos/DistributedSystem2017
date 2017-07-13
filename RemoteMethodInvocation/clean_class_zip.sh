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

find . -maxdepth 8 -type f -name "*.class" -delete

rm -rf *.tar.gz
echo "Cleaning..."
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/Public/ ; rm -rf *.tar.gz; rm -rf classes/ ; rm -rf deploy/ ; rm -rf *.sh"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$GeneralRepositoryHostName "cd ~/Public/ ; rm -rf *"
echo "Cleaning done! all .class & folders deleted!"