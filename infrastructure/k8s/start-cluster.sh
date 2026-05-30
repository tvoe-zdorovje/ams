#!/usr/bin/env bash
set -euo pipefail

if command -v readlink >/dev/null && readlink -f "$0" >/dev/null 2>&1; then
    SCRIPT_PATH="$(readlink -f "$0")"
else
    SCRIPT_PATH="$0"
fi
SCRIPT_DIR="$(cd "$(dirname "$SCRIPT_PATH")" && pwd)"
PROJECT_DIR="${SCRIPT_DIR}/../.."

echo "script directory = $SCRIPT_DIR"
echo "project root directory = $PROJECT_DIR"

CLUSTER_NAME="ams"

echo "=== 👨🏻‍🔧 Starting Kubernetes cluster 🆕 ==="

if ! minikube status >/dev/null 2>&1; then
  minikube start --cpus=4 --memory=6g
else
  echo "Minikube already running"
fi

# === KAFKA ===

echo ""
echo "=== ️📥 Creating Kafka namespace ==="

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
echo "== ️️⚙️ Installing Strimzi operator =="

if kubectl get deployment strimzi-cluster-operator -n "$STRIMZI_NAMESPACE" &>/dev/null; then
    echo "Strimzi operator already exists in namespace '$STRIMZI_NAMESPACE'"
else
    helm repo add strimzi https://strimzi.io/charts/ >/dev/null 2>&1 || true
    helm repo update

    helm upgrade --install "$STRIMZI_RELEASE" strimzi/strimzi-kafka-operator \
      --namespace "$STRIMZI_NAMESPACE"

    echo ""
    echo "== Waiting for Strimzi operator =="

    kubectl wait deployment/strimzi-cluster-operator \
      -n "$STRIMZI_NAMESPACE" \
      --for=condition=Available \
      --timeout=300s
fi

echo ""
echo "== ️🛡️ Applying secrets =="

for path in "${SECRETS[@]}"; do
    kubectl apply -f "$path" -n "$NAMESPACE"
done

echo ""
echo "== 📤 Install Kafka =="
helm install kafka ./kafka-infra/kafka -n kafka

echo ""
echo "== 📤 Install Schema Registry =="
helm install schema-registry ./kafka-infra/schema-registry -n kafka

echo ""
echo "== 📤 Install Kafka-Connect =="
helm install kafka-connect ./kafka-infra/kafka-connect -n kafka


# === SERVICES ===

echo ""
echo "=== 📥 Creating Services namespace ==="

NAMESPACE="services"
SECRETS=(
  ./secrets/github-secret.yaml
)

if ! kubectl get namespace "$NAMESPACE" >/dev/null 2>&1; then
    echo "Creating namespace: $NAMESPACE"
    kubectl create namespace "$NAMESPACE"
  else
    echo "Namespace already exists: $NAMESPACE"
  fi

echo ""
echo "== 🛡️ Applying secrets =="

for path in "${SECRETS[@]}"; do
    kubectl apply -f "$path" -n "$NAMESPACE"
done

echo ""
echo "== Install Services Charts =="
SERVICES_DIR="$SCRIPT_DIR/services/"
cd "$SCRIPT_DIR"
cd "$SERVICES_DIR"

SERVICES=(
  user
)

POSTGRESQL_IMAGE="docker.io/bitnamilegacy/postgresql:15.4.0-debian-11-r45"
echo ""
echo "📥 load $POSTGRESQL_IMAGE to the minikube"
minikube image load "$POSTGRESQL_IMAGE" # in order to speed up postgres startup
POSTGRESQL_IMAGE="postgres:17"
echo ""
echo "📥 load $POSTGRESQL_IMAGE to the minikube"
minikube image load "$POSTGRESQL_IMAGE" # in order to speed up liquibase initContainer startup
for service in "${SERVICES[@]}"; do
    SERVICE_IMAGE="ghcr.io/tvoe-zdorovje/ams/$service-service:0.1.0"
    echo ""
    echo "📥 load $SERVICE_IMAGE to the minikube"
    minikube image load "$SERVICE_IMAGE" # in order to speed up service startup
    LIQUIBASE_IMAGE="ghcr.io/tvoe-zdorovje/ams/$service-service-liquibase:0.1.0"
    echo ""
    echo "📥 load $LIQUIBASE_IMAGE to the minikube"
    minikube image load "$LIQUIBASE_IMAGE" # in order to speed up liquibase startup

    echo ""
    echo "== 🧩 Generate [ $service ] DB init scripts secret =="
    cd "./$service"
    kubectl create secret generic postgres-init-scripts \
      --from-file=01-init.sql="$PROJECT_DIR/$service-service/database/init_db.sql" \
      --dry-run=client -o yaml > ./templates/init-scripts_secret.yaml

    echo ""
    echo "== 📥 Install [ $service ] Service =="

    cd "./charts/service"
    helm dependency update
    cd "../liquibase"
    helm dependency update
    cd ../..
    helm dependency update

    RELEASE_NAME="$service-service"
    helm install "$RELEASE_NAME" . -n "$NAMESPACE" --timeout 30m

    cd "$SERVICES_DIR"
done

cd "$SCRIPT_DIR"

echo ""
echo "=== 🧑🏻‍🔧💬 Cluster bootstrap finished ==="

kubectl get pods -A

# kubectl port-forward pod/user-service-postgresql-0 5432:5432 -n services

