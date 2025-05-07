
FROM rust:1.86 AS builder

RUN curl -sSL https://rover.apollo.dev/nix/latest | sh

FROM ghcr.io/apollographql/router:v2.2.0

ENV ROVER_ELV2_LICENSE=accept

RUN mkdir -p /tmp/app/schemas
COPY --from=builder --chmod=755 /root/.rover/bin/rover /rover
COPY --chmod=755 compose.sh /tmp/app/schemas/compose.sh

ENTRYPOINT exec sh -c "\
  /tmp/app/schemas/compose.sh && \
  ./router --supergraph /tmp/app/supergraph.graphql"



