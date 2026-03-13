#!/usr/bin/env bash
set -euo pipefail

CLUSTER_NAME="ams"

NAMESPACES=(
  kafka
#  services
#  databases
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
minikube delete

echo "Cluster removed"