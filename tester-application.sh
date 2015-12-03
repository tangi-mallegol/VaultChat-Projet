#!/bin/bash
#
# ./tester-application.sh
#
# Lance la simulation du réseau VaultChat pour l'exemple donné dans le sujet.
# L'annuaire RMI est démarré, puis stoppé par le script.
#
# Gwénolé Lecorvé & David Guennec
# ENSSAT, Université de Rennes 1
# Novembre 2015
#

CLASS_PATH=./out/production/VaultChat-Projet

cd $CLASS_PATH
C:/"Program Files"/Java/jre1.8.0_66/bin/rmiregistry.exe 2020 &
cd - > /dev/null
sleep 0.2
java -Djava.security.policy=./security.policy -Djava.rmi.server.codebase=file:${CLASS_PATH} -cp ${CLASS_PATH} NoeudCentralSimulateur rmi://localhost:2020/noeud-central &
sleep 0.2
java -Djava.security.policy=./security.policy -Djava.rmi.server.codebase=file:${CLASS_PATH} -cp ${CLASS_PATH} AbriSimulateur rmi://localhost:2020/abri31 &
sleep 0.2
java -Djava.security.policy=./security.policy -Djava.rmi.server.codebase=file:${CLASS_PATH} -cp ${CLASS_PATH} AbriSimulateur rmi://localhost:2020/abri57 &
sleep 0.2
java -Djava.security.policy=./security.policy -Djava.rmi.server.codebase=file:${CLASS_PATH} -cp ${CLASS_PATH} AbriSimulateur rmi://localhost:2020/abri58 &
sleep 0.2
java -Djava.security.policy=./security.policy -Djava.rmi.server.codebase=file:${CLASS_PATH} -cp ${CLASS_PATH} AbriSimulateur rmi://localhost:2020/abri71 &
sleep 0.2
java -Djava.security.policy=./security.policy -Djava.rmi.server.codebase=file:${CLASS_PATH} -cp ${CLASS_PATH} AbriSimulateur rmi://localhost:2020/abri108
killall rmiregistry
