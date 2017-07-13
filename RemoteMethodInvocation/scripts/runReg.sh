#!/usr/bin/env bash
registryPortNum=22460
localhost=localhost

cd deploy/dir_registry/
sh registry_com_alt.sh $localhost $registryPortNum
