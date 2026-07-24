FROM liquibase/liquibase:latest-alpine

WORKDIR /liquibase/changelog

COPY ./changelog ./

ENTRYPOINT ["liquibase"]
