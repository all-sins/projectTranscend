#!/bin/bash
source ./setEnvVars.sh
cat /dev/null > ./pt.log && \
java -jar ./projectTranscend-1.0-SNAPSHOT.jar > ./pt.log
