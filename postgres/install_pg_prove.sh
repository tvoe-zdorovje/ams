#!/bin/sh

set -ex

apk update
apk add --no-cache \
  perl \
  perl-app-cpanminus \
  postgresql16-client

cpanm TAP::Parser::SourceHandler::pgTAP
