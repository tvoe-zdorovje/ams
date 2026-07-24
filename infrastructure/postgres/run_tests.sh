#!/bin/sh

set -ex

pg_prove ./tests/ --recurse --ext .sql
pg_prove --verbose --failures --comments --runtests
