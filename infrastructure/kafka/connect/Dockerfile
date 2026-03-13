FROM confluentinc/cp-kafka-connect:8.0.3

LABEL org.opencontainers.image.source=https://github.com/tvoe-zdorovje/ams

USER root

SHELL ["/bin/bash", "-o", "pipefail", "-c"]

ARG DEBEZIUM_VERSION=3.4.1.Final

RUN set -eux \
  && PG_PLUGIN_ZIP="/tmp/debezium-connector-postgres-${DEBEZIUM_VERSION}-plugin.zip" \
  && PG_PLUGIN_URL="https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/${DEBEZIUM_VERSION}/debezium-connector-postgres-${DEBEZIUM_VERSION}-plugin.zip" \
  && JDBC_PLUGIN_ZIP="/tmp/debezium-connector-jdbc-${DEBEZIUM_VERSION}-plugin.zip" \
  && JDBC_PLUGIN_URL="https://repo1.maven.org/maven2/io/debezium/debezium-connector-jdbc/${DEBEZIUM_VERSION}/debezium-connector-jdbc-${DEBEZIUM_VERSION}-plugin.zip" \
  && wget -qO "${PG_PLUGIN_ZIP}" "${PG_PLUGIN_URL}" \
  && wget -qO "${JDBC_PLUGIN_ZIP}" "${JDBC_PLUGIN_URL}" \
  && test -s "${PG_PLUGIN_ZIP}" \
  && test -s "${JDBC_PLUGIN_ZIP}" \
  && mkdir -p /usr/share/confluent-hub-components/debezium-connector-postgresql \
  && mkdir -p /usr/share/confluent-hub-components/debezium-connector-jdbc \
  && cd /usr/share/confluent-hub-components/debezium-connector-postgresql \
  && jar xf "${PG_PLUGIN_ZIP}" \
  && cd /usr/share/confluent-hub-components/debezium-connector-jdbc \
  && jar xf "${JDBC_PLUGIN_ZIP}" \
  && rm -f "${PG_PLUGIN_ZIP}" "${JDBC_PLUGIN_ZIP}"

USER appuser
