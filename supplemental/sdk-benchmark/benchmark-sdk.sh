#!/bin/bash
SDK=$1

. $HOME/.sdkman/bin/sdkman-init.sh && \
sdk install java $SDK && \
sdk use java $SDK && \
./gradlew build

exit $?
