#!/usr/bin/env bash
registryPortNum=22460
username=sd0407
password=sportingsempre
url=http://192.168.8.171/sd0407/classes/
GeneralRepositoryHostName=l040101-ws03.ua.pt
registryHostName=l040101-ws01.ua.pt


cd ~/Public/deploy/dir_GeneralRepository/
sh serverSideGenRepo_com.sh $registryHostName $registryPortNum $url