#!/bin/bash
SDK=$1

. /home/gitpod/.sdkman/bin/sdkman-init.sh && \
sdk install java $SDK && \
sdk use java $SDK && \
./gradlew build

exit $?
