#!/bin/bash

COMMAND="$1"

REAL_SCRIPT_PATH=$(realpath "${BASH_SOURCE[0]}")
echo "- Running '$REAL_SCRIPT_PATH'..."
SCRIPT_DIRECTORY=$(dirname "$REAL_SCRIPT_PATH")
source "$SCRIPT_DIRECTORY/config.sh"

LOGFILE="/tmp/repos/summary.log"

echo
rm -rf /tmp/repos
mkdir -p /tmp/repos

echo -e -n "project\t" > $LOGFILE
echo -e -n "clone\t" >> $LOGFILE
echo -e -n "clean_before\t" >> $LOGFILE
echo -e -n "assemble_before\t" >> $LOGFILE
echo -e -n "build_before\t" >> $LOGFILE
echo -e -n "apply\t" >> $LOGFILE
echo -e -n "changed_files\t" >> $LOGFILE
echo -e -n "commit\t" >> $LOGFILE
echo -e -n "clean_after\t" >> $LOGFILE
echo -e -n "assemble_after\t" >> $LOGFILE
echo -e -n "build_after\t" >> $LOGFILE
echo -e -n "push\t" >> $LOGFILE
echo -e "" >> $LOGFILE

for index in ${!PROJECT_URLS[*]}
do
  BASENAME=$(basename "${PROJECT_URLS[$index]}")
  echo "- Processing $BASENAME"
  echo -e -n "$BASENAME\t" >> $LOGFILE

  echo " - Cloning $BASENAME"
  cd /tmp/repos
  git clone --quiet "${PROJECT_URLS[$index]}" /tmp/repos/$BASENAME
  RC=$?
  echo "  - Cloning: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  cd /tmp/repos/$BASENAME

  echo " - Cleaning before..."
  ./gradlew clean
  RC=$?
  echo "  - Cleaning before: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  echo " - Assembling before..."
  ./gradlew assemble
  RC=$?
  echo "  - Assembling before: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  echo " - Building before..."
  ./gradlew build
  RC=$?
  echo "  - Building before: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  APPLY_SCRIPT="$SCRIPT_DIRECTORY/$COMMAND"
  echo " - Applying $APPLY_SCRIPT ..."
  "$APPLY_SCRIPT"
  RC=$?
  echo "  - Applying: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  echo " - Counting changed files..."
  CHANGED_FILES_COUNT=$(git ls-files -m | wc -l)
  echo "  - Counted changed files: $CHANGED_FILES_COUNT"
  echo -e -n "$CHANGED_FILES_COUNT\t" >> $LOGFILE

  echo " - Committing..."
  git commit --all --quiet --message="Applyied $COMMAND"
  RC=$?
  echo "  - Committing: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  echo " - Cleaning after..."
  ./gradlew clean
  RC=$?
  echo "  - Cleaning after: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  echo " - Assembling after..."
  ./gradlew assemble
  RC=$?
  echo "  - Assembling after: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  echo " - Building after..."
  ./gradlew build
  RC=$?
  echo "  - Building after: Exit code=$RC"
  echo -e -n "$RC\t" >> $LOGFILE

  #echo " - Pushing..."
  #git push
  #RC=$?
  #echo "  - Pushing: Exit code=$RC"
  RC="X"
  echo -e -n "$RC\t" >> $LOGFILE

  echo -e >> $LOGFILE
done

cat $LOGFILE | column -t | tee $LOGFILE.columns