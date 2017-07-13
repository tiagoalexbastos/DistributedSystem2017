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

sh compile_source_code.sh

echo "compressing source code..."
tar -czf dir_registry.tar.gz deploy/dir_registry/
tar -czf dir_GeneralRepository.tar.gz deploy/dir_GeneralRepository/
tar -czf dir_Museum.tar.gz deploy/dir_Museum/
tar -czf dir_ConcentrationSite.tar.gz deploy/dir_ConcentrationSite/
tar -czf dir_AssaultParty.tar.gz deploy/dir_AssaultParty/
tar -czf dir_CollectionSite.tar.gz deploy/dir_CollectionSite/
tar -czf dir_Thief.tar.gz deploy/dir_Thief/
tar -czf dir_MasterThief.tar.gz deploy/dir_MasterThief/


sleep 5

echo "Unziping code in machines"
sshpass -p $password scp dir_registry.tar.gz $username@$registryHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/Public/ ; tar -xmzf dir_registry.tar.gz" &

sshpass -p $password scp dir_GeneralRepository.tar.gz $username@$GeneralRepositoryHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$GeneralRepositoryHostName "cd ~/Public/ ; tar -xmzf dir_GeneralRepository.tar.gz" &

sshpass -p $password scp dir_Museum.tar.gz $username@$MuseumHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/Public/ ; tar -xmzf dir_Museum.tar.gz" &

sshpass -p $password scp dir_ConcentrationSite.tar.gz $username@$ConcentrationHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/Public/ ; tar -xmzf dir_ConcentrationSite.tar.gz" &

sshpass -p $password scp dir_AssaultParty.tar.gz $username@$AssaultPartyHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/Public/ ; tar -xmzf dir_AssaultParty.tar.gz" &

sshpass -p $password scp dir_CollectionSite.tar.gz $username@$ColectionHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/Public/ ; tar -xmzf dir_CollectionSite.tar.gz" &

sshpass -p $password scp dir_Thief.tar.gz $username@$ThievesHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/Public/ ; tar -xmzf dir_Thief.tar.gz" &

sshpass -p $password scp dir_MasterThief.tar.gz $username@$MasterThiefHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/Public/ ; tar -xmzf dir_MasterThief.tar.gz" &


echo "Setting RMI repository.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/Public/ ; mkdir classes" &
sshpass -p $password scp set_rmiregistry.sh $username@$registryHostName:~/Public/
sshpass -p $password scp -r deploy/interfaces/ $username@$registryHostName:~/Public/classes/interfaces/
sshpass -p $password scp -r deploy/support/ $username@$registryHostName:~/Public/classes/support/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "sh ~/Public/set_rmiregistry.sh $registryPortNum" &
sleep 5
echo " "


echo "Setting Service Register.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/Public/deploy/dir_registry/ ; ./registry_com.sh $registryHostName $registryPortNum $url" &
sleep 5
echo " "


echo "Setting General Repository.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no $username@$GeneralRepositoryHostName 'bash -s' < runLogMachine.sh & PID_Logging=$!
sleep 5
echo " "


echo "Setting Museum.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MuseumHostName "cd ~/Public/deploy/dir_Museum/ ; ./serverSideMuseum_com.sh $registryHostName $registryPortNum $url" &
sleep 5
echo " "


echo "Setting Concentration Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ConcentrationHostName "cd ~/Public/deploy/dir_ConcentrationSite/ ; ./serverSideConcentration_com.sh $registryHostName $registryPortNum $url" &
sleep 5
echo " "


echo "Setting Assault Party Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$AssaultPartyHostName "cd ~/Public/deploy/dir_AssaultParty/ ; ./serverSideAssault_com.sh $registryHostName $registryPortNum $url" &
sleep 5
echo " "


echo "Setting Collection Site.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ColectionHostName "cd ~/Public/deploy/dir_CollectionSite/ ; ./serverSideCollection_com.sh $registryHostName $registryPortNum $url" &
sleep 5
echo " "


echo "Setting Ordinary Thieves.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$ThievesHostName "cd ~/Public/deploy/dir_Thief/ ; ./clientSideThief_com.sh $registryHostName $registryPortNum" &
sleep 5
echo " "


echo "Setting Master Thief.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MasterThiefHostName "cd ~/Public/deploy/dir_MasterThief/ ; ./clientSideMaster_com.sh $registryHostName $registryPortNum" &
sleep 5
echo " "



wait $PID_Logging

sshpass -p $password  scp $username@$GeneralRepositoryHostName:~/Public/deploy/dir_GeneralRepository/*.txt .

sh clean_class_zip.sh
echo " "


subl LogOrdenado.txt