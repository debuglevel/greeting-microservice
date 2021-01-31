#!/bin/bash

# run this if file is not yet present or it should be updated first:
#  curl -o update-from-template.sh https://raw.githubusercontent.com/debuglevel/greeting-microservice/master/update-from-template.sh && chmod +x update-from-template.sh && git add --chmod=+x update-from-template.sh && git commit update-from-template.sh -m "Add/Update update-from-template.sh script"

echo "Adding template git remote..."
git remote add template https://github.com/debuglevel/greeting-microservice.git
echo "Fetching from git remote..."
git fetch --no-tags template

files=(
    # can usually be replaced without any merges
    update-from-template.sh
    gradle/wrapper/gradle-wrapper.jar
    gradle/wrapper/gradle-wrapper.properties
    gradlew
    gradlew.bat
    LICENSE
    .gitlab-ci.yml
    .dockerignore
    Dockerfile
    Procfile

    # might need some merges
    settings.gradle
    .gitignore
    .gitpod.yml
    .gitpod.Dockerfile
    docker-compose.yml
    src/main/resources/logback.xml
    src/test/resources/application-test.yml
    gradle.properties

    # might need heavy merges
    .travis.yml
    build.gradle
    src/main/resources/application.yml
    )

echo ""
echo "The following files from the template will be processed:"
for file in "${files[@]}"; do
  echo "- $file";
done

echo ""
echo "The following runs a 'git checkout --patch' command:"
echo "  Press [y] to apply a patch."
echo "  Press [n] to discard a patch."
echo "  Press [e] to edit a patch."
echo "  Press [s] to split into smaller hunks."
echo "  Press [q] to quit that thing (maybe also useful if this script itself was updated to re-execute it)."
echo "  Press [?] to get information about all that letters you can choose."

git checkout -p template/master ${files[*]}

echo "Now you should carefully check changes before committing."
