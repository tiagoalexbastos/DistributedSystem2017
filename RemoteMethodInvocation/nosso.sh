#!/usr/bin/env bash
registryPortNum=22460
localhost=localhost


sh compile_source_code.sh

echo "running... "
sh set_rmiregistry_alt.sh 22460 &
sleep 2
sh scripts/runReg.sh &
sleep 2
sh scripts/runGen.sh & PID_Logging=$!
sleep 2
sh scripts/runMuseum.sh  &
sleep 2
sh scripts/runConc.sh  &
sleep 2
sh scripts/runAss.sh  &
sleep 2
sh scripts/runCol.sh  &
sleep 2
sh scripts/runThief.sh  &
sleep 2
sh scripts/runMaster.sh  &

wait $PID_Logging

find . -maxdepth 8 -type f -name "*.class" -delete

rm -rf *.zip

kill `lsof -t -i:22460`
kill `lsof -t -i:22461`