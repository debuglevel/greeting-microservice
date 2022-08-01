#!/bin/bash
SDK=$1

# Depends on local vs. GitHub Codespace
#. $HOME/.sdkman/bin/sdkman-init.sh && \
. /usr/local/sdkman/bin/sdkman-init.sh && \
sdk install java $SDK && \
sdk use java $SDK && \
./gradlew build --no-daemon

exit $?
