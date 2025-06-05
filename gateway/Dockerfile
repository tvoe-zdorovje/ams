# TODO: should I build sources or just copy already builded bin?
FROM rust:1.86 AS rust_builder

RUN curl -sSL https://rover.apollo.dev/nix/latest | sh

FROM golang:1.24-alpine AS go_builder


RUN apk add --no-cache git

WORKDIR /app/proxy
COPY proxy/src .
RUN go mod download && go build -o build/proxy ./cmd/proxy


FROM ghcr.io/apollographql/router:v2.2.0

ENV ROVER_ELV2_LICENSE=accept

RUN mkdir -p /tmp/app/schemas
COPY --from=rust_builder --chmod=755 /root/.rover/bin/rover /rover
COPY --chmod=755 apollo/router/compose.sh /tmp/app/schemas/compose.sh

COPY --from=go_builder /app/proxy/build/proxy /proxy
#COPY --from=go_builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

ENTRYPOINT exec sh -c "\
  /tmp/app/schemas/compose.sh && \
  ./router --supergraph /tmp/app/supergraph.graphql --dev & \
  /proxy \
"



