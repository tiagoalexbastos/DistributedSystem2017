#!/usr/bin/env bash

echo "compiling source code..."

javac -cp \* support/*.java interfaces/*.java registry/*.java serverSide/generalRepository/*.java serverSide/museum/*.java serverSide/concentrationSite/*.java serverSide/assaultParty/*.java serverSide/collectionSite/*.java clientSide/thief/*.java clientSide/masterThief/*.java
echo "Copying Interfaces .class files ... "
cp interfaces/Register.class deploy/dir_registry/interfaces/
cp interfaces/*.class deploy/dir_AssaultParty/interfaces/
cp interfaces/*.class deploy/dir_CollectionSite/interfaces/
cp interfaces/*.class deploy/dir_ConcentrationSite/interfaces/
cp interfaces/*.class deploy/dir_GeneralRepository/interfaces/
cp interfaces/*.class deploy/dir_Museum/interfaces/
cp interfaces/*.class deploy/dir_MasterThief/interfaces/
cp interfaces/*.class deploy/dir_Thief/interfaces/
cp interfaces/*.class deploy/interfaces/


echo "Copying registry .class files ... "
cp registry/*.class deploy/dir_registry/registry/

echo "Copying Server Side .class files ... "
cp serverSide/generalRepository/*.class deploy/dir_GeneralRepository/serverSide/generalRepository/
cp serverSide/museum/*.class deploy/dir_Museum/serverSide/museum/
cp serverSide/concentrationSite/*.class deploy/dir_ConcentrationSite/serverSide/concentrationSite/
cp serverSide/assaultParty/*.class deploy/dir_AssaultParty/serverSide/assaultParty/
cp serverSide/collectionSite/*.class deploy/dir_CollectionSite/serverSide/collectionSite/

echo "Copying Client Side .class files ... "
cp clientSide/masterThief/*.class deploy/dir_MasterThief/clientSide/masterThief/
cp clientSide/thief/*.class deploy/dir_Thief/clientSide/thief/

echo "Copying support .class files ... "
cp support/*.class deploy/dir_GeneralRepository/support/
cp support/*.class deploy/dir_Museum/support/
cp support/*.class deploy/dir_ConcentrationSite/support/
cp support/*.class deploy/dir_AssaultParty/support/
cp support/*.class deploy/dir_CollectionSite/support/
cp support/*.class deploy/dir_MasterThief/support/
cp support/*.class deploy/dir_Thief/support/
cp support/*.class deploy/support/
echo "done..."
