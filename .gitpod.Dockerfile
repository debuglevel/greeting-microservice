FROM gitpod/workspace-full

# Install custom tools, runtimes, etc.
# For example "bastet", a command-line tetris clone:
# RUN brew install bastet
#
# More information: https://www.gitpod.io/docs/config-docker/

USER gitpod

# See https://www.gitpod.io/docs/languages/kotlin
RUN brew install kotlin

# Add helm tools
RUN brew install helm

# Gitpod defaults to Zulu 11 as of 2022-02-28
RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && sdk install java 17.0.2-zulu"
