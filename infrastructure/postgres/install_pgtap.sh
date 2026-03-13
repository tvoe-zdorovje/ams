#!/bin/sh

PGTAP_VERSION="v1.3.3"

set -ex

apk add --no-cache make
apk add --no-cache --virtual .build-dependencies \
  cmake make musl-dev gcc gettext-dev libintl \
  postgresql \
  postgresql-contrib \
  build-base \
  diffutils \
  patch \
  perl \
  git

git clone https://github.com/theory/pgtap.git
git config --global --add safe.directory /pgtap
cd pgtap/
git checkout "$PGTAP_VERSION"
make && make install

apk del .build-dependencies
