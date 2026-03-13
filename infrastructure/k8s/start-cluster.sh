#!/usr/bin/env bash
set -euo pipefail

CLUSTER_NAME="ams"

echo "=== Starting Kubernetes cluster ==="

if ! minikube status >/dev/null 2>&1; then
  minikube start --cpus=4 --memory=6g
else
  echo "Minikube already running"
fi

# === KAFKA ===

echo ""
echo "=== Creating Kafka namespace ==="

NAMESPACE="kafka"
SECRETS=(
  ./secrets/github-secret.yaml
)

STRIMZI_RELEASE="strimzi-rlz"
STRIMZI_NAMESPACE="$NAMESPACE"

if ! kubectl get namespace "$NAMESPACE" >/dev/null 2>&1; then
    echo "Creating namespace: $NAMESPACE"
    kubectl create namespace "$NAMESPACE"
  else
    echo "Namespace already exists: $NAMESPACE"
  fi

echo ""
echo "== Installing Strimzi operator =="

helm repo add strimzi https://strimzi.io/charts/ >/dev/null 2>&1 || true
helm repo update

helm upgrade --install "$STRIMZI_RELEASE" strimzi/strimzi-kafka-operator \
  --namespace "$STRIMZI_NAMESPACE"

echo ""
echo "== Waiting for Strimzi operator =="

kubectl wait deployment/strimzi-cluster-operator \
  -n "$STRIMZI_NAMESPACE" \
  --for=condition=Available \
  --timeout=180s

echo ""
echo "== Applying secrets =="

for path in "${SECRETS[@]}"; do
    kubectl apply -f "$path" -n "$NAMESPACE"
done

echo ""
echo "== Install Kafka =="
helm install kafka ./kafka-infra/kafka -n kafka

echo ""
echo "== Install Kafka-Connect =="
helm install kafka-connect ./kafka-infra/kafka-connect -n kafka

# ...

echo ""
echo "=== Cluster bootstrap finished ==="

kubectl get pods -A