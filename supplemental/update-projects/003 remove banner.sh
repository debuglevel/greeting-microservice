#!/bin/bash

OLD_PWD=$PWD

pwd

cd src/main/kotlin/*/*/*/

if grep -q ".banner(false)" Application.kt
then
  : # noop
else
  # code if not found
  sed -i '/            .start()/i\            .banner(false)' Application.kt
fi

cd $OLD_PWD