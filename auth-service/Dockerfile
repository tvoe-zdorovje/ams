FROM jetty:11-jdk17-alpine-eclipse-temurin

# build argon2

USER root

RUN apk add --no-cache build-base cmake git \
 && git clone https://github.com/P-H-C/phc-winner-argon2.git /tmp/argon2 \
 && cd /tmp/argon2 \
 && make \
 && make install \
 && rm -rf /tmp/argon2 \
 && apk del build-base cmake git

USER jetty
