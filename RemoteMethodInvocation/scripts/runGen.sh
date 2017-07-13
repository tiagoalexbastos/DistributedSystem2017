#!/usr/bin/env bash
registryPortNum=22460
localhost=localhost
cd deploy/dir_GeneralRepository/
rm *.txt
sh serverSideGenRepo_com_alt.sh $localhost $registryPortNum
