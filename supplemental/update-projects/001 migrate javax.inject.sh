#!/bin/bash

grep -rl javax.inject . | xargs -r sed -i 's/javax.inject/jakarta.inject/g'
