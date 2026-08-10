# https://www.keycloak.org/server/containers
FROM quay.io/keycloak/keycloak:26.7.1

ADD https://github.com/SnuK87/keycloak-kafka/releases/download/1.4.0/keycloak-kafka-1.4.0-jar-with-dependencies.jar /opt/keycloak/providers/

USER root
RUN chown -R keycloak:keycloak /opt/keycloak/providers/
USER keycloak

RUN /opt/keycloak/bin/kc.sh build --verbose

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]