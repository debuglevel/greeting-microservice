#!/bin/bash

grep -rl io.micronaut.test.annotation.MicronautTest . | xargs -r sed -i 's/io.micronaut.test.annotation.MicronautTest/io.micronaut.test.extensions.junit5.annotation.MicronautTest/g'
