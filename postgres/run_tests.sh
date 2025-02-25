#!/bin/sh

set -ex

pg_prove ./tests/*.sql
