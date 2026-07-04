#!/usr/bin/env bash
set -euo pipefail

if command -v readlink >/dev/null && readlink -f "$0" >/dev/null 2>&1; then
    SCRIPT_PATH="$(readlink -f "$0")"
else
    SCRIPT_PATH="$0"
fi
SCRIPT_DIR="$(cd "$(dirname "$SCRIPT_PATH")" && pwd)"
PROJECT_DIR="${SCRIPT_DIR}/../.."
SERVICES_DIR="$SCRIPT_DIR/services/"

echo "script directory = $SCRIPT_DIR"
echo "project root directory = $PROJECT_DIR"
echo "services directory = $SERVICES_DIR"

CLUSTER_NAME="ams"

echo "=== 👨🏻‍🔧 Starting Kubernetes cluster 🆕 ==="

if ! minikube status >/dev/null 2>&1; then
  minikube start --cpus=6 --memory=8g
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

KAFKA_IMAGE="quay.io/strimzi/kafka:1.0.1-kafka-4.2.0"
echo ""
echo "📥 load $KAFKA_IMAGE to the minikube"
minikube image load "$KAFKA_IMAGE" # in order to speed up kafka startup

helm install kafka ./kafka-infra/kafka -n kafka

echo ""
echo "== 📤 Install Schema Registry =="

SCHEMA_REGISTRY_IMAGE="confluentinc/cp-schema-registry:8.0.3"
echo ""
echo "📥 load $SCHEMA_REGISTRY_IMAGE to the minikube"
minikube image load "$SCHEMA_REGISTRY_IMAGE" # in order to speed up schema-registry startup

helm install schema-registry ./kafka-infra/schema-registry -n kafka

echo ""
echo "== 📤 Install Kafka-Connect =="

KAFKA_CONNECT_IMAGE="quay.io/strimzi/buildah:1.0.1"
echo ""
echo "📥 load $KAFKA_CONNECT_IMAGE to the minikube"
minikube image load "$KAFKA_CONNECT_IMAGE" # in order to speed up schema-registry startup

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
cd "$SCRIPT_DIR"
cd "$SERVICES_DIR"

SERVICES=(
  "administration:8181:5441"
  "appointment:8182:5442"
  "brand:8183:5443"
  "studio:8184:5444"
  "user:8185:5445"
)

POSTGRESQL_IMAGE="docker.io/bitnamilegacy/postgresql:15.4.0-debian-11-r45"
echo ""
echo "📥 load $POSTGRESQL_IMAGE to the minikube"
minikube image load "$POSTGRESQL_IMAGE" # in order to speed up postgres startup
POSTGRESQL_IMAGE="postgres:17"
echo ""
echo "📥 load $POSTGRESQL_IMAGE to the minikube"
minikube image load "$POSTGRESQL_IMAGE" # in order to speed up liquibase initContainer startup

for entry in "${SERVICES[@]}"; do
  IFS=':' read -r service servicePort dbPort <<< "$entry"

  SERVICE_VERSION=$(grep '^appVersion:' "$SERVICES_DIR/$service/charts/service/Chart.yaml" | sed -E 's/^appVersion:[[:space:]]*//; s/^"//; s/"$//')
  SERVICE_IMAGE="ghcr.io/tvoe-zdorovje/ams/$service-service:$SERVICE_VERSION"
  echo ""
  echo "📥 load $SERVICE_IMAGE to the minikube"
  minikube image load "$SERVICE_IMAGE" # in order to speed up service startup
  LIQUIBASE_VERSION=$(grep '^appVersion:' "$SERVICES_DIR/$service/charts/liquibase/Chart.yaml" | sed -E 's/^appVersion:[[:space:]]*//; s/^"//; s/"$//')
  LIQUIBASE_IMAGE="ghcr.io/tvoe-zdorovje/ams/$service-service-liquibase:$LIQUIBASE_VERSION"
  echo ""
  echo "📥 load $LIQUIBASE_IMAGE to the minikube"
  minikube image load "$LIQUIBASE_IMAGE" # in order to speed up liquibase startup

  echo ""
  echo "== 🧩 Generate [ $service ] DB init scripts secret =="
  cd "./$service"
  kubectl create secret generic "$service-postgres-init-scripts" \
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

  echo "Port forward for $service: $servicePort -> 8080   $dbPort:5432"
  kubectl port-forward -n services "service/$service-service" "$servicePort:8080" &
  kubectl port-forward -n services "service/$service-service-postgresql" "$dbPort:5432" &

  cd "$SERVICES_DIR"
done

cd "$SCRIPT_DIR"

echo ""
echo "=== 🧑🏻‍🔧💬 Cluster bootstrap finished ==="

kubectl get pods -A

# kubectl port-forward pod/user-service-postgresql-0 5432:5432 -n services

