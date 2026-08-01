#!/usr/bin/env bash
set -euo pipefail

DELETE_MODE=false

for arg in "$@"; do
    if [ "$arg" = "--delete" ]; then
        DELETE_MODE=true
        break
    fi
done

CLUSTER_NAME="ams"

NAMESPACES=(
  kafka
  services
  infrastructure
)

STRIMZI_RELEASE="strimzi"
STRIMZI_NAMESPACE="kafka"

echo "=== Removing Strimzi ==="

helm uninstall "$STRIMZI_RELEASE" -n "$STRIMZI_NAMESPACE" 2>/dev/null || true

echo ""
echo "=== Deleting namespaces ==="

for ns in "${NAMESPACES[@]}"; do
  kubectl delete namespace "$ns" --ignore-not-found=true
done

echo ""
echo "=== Stopping minikube ==="

minikube stop
if [ "$DELETE_MODE" = true ]; then
    minikube delete --purge
fi

echo "Cluster removed"