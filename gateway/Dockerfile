# deprecated
FROM ghcr.io/apollographql/router:v2.2.0

ENV ROVER_ELV2_LICENSE=accept
ENV APOLLO_ROUTER_SUPERGRAPH_PATH=/dist/schema/supergraph.graphql
ENV APOLLO_ROUTER_CONFIG_PATH=/dist/config/router.yaml

COPY proxy/build/proxy /proxy

ENTRYPOINT exec sh -c "\
  ./router & \
  /proxy \
"
