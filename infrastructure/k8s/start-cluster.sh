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
  minikube start --cpus=6 --memory=12g
else
  echo "Minikube already running"
fi

echo ""
echo "Enable the ingress addon"
minikube addons enable ingress

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

# FIXME smth wrong happens
#KAFKA_CONNECT_IMAGE="ghcr.io/tvoe-zdorovje/ams/kafka-connect:strimzi-4.2.0"
#echo ""
#echo "📥 load $KAFKA_CONNECT_IMAGE to the minikube"
#minikube image load "$KAFKA_CONNECT_IMAGE" # in order to speed up Kafka Connect startup

KAFKA_CONNECT_BUILD_IMAGE="quay.io/strimzi/buildah:1.0.1"
echo ""
echo "📥 load $KAFKA_CONNECT_BUILD_IMAGE to the minikube"
minikube image load "$KAFKA_CONNECT_BUILD_IMAGE" # in order to speed up Kafka Connect Build startup

helm install kafka-connect ./kafka-infra/kafka-connect -n kafka


# === INFRASTRUCTURE ===

echo ""
echo "== ️️📥 Creating INFRASTRUCTURE namespace =="

INF_DIR="$PROJECT_DIR/infrastructure/k8s/infrastructure"
cd "$INF_DIR"

NAMESPACE="infrastructure"
if ! kubectl get namespace "$NAMESPACE" >/dev/null 2>&1; then
  echo "Creating namespace: $NAMESPACE"
  kubectl create namespace "$NAMESPACE"
else
  echo "Namespace already exists: $NAMESPACE"
fi

SERVICE="redis"

echo ""
echo "== ️️📥 Install [$SERVICE] =="

cd "$INF_DIR/$SERVICE"

helm install "$SERVICE" oci://registry-1.docker.io/bitnamicharts/redis -f values.yaml -n "$NAMESPACE"

SERVICE="config-server"

echo ""
echo "== ️️📥 Install [$SERVICE] =="
cd "$INF_DIR/$SERVICE"

SERVICE_VERSION=$(grep '^appVersion:' "$INF_DIR/$SERVICE/Chart.yaml" | sed -E 's/^appVersion:[[:space:]]*//; s/^"//; s/"$//')
SERVICE_IMAGE="springcloud/spring-cloud-kubernetes-configserver:$SERVICE_VERSION"
echo ""
echo "📥 load $SERVICE_IMAGE to the minikube"
minikube image load "$SERVICE_IMAGE" # in order to speed up service startup

RELEASE_NAME="$SERVICE"
helm install "$RELEASE_NAME" . -n "$NAMESPACE" --timeout 5m

cd $SCRIPT_DIR

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
  "auth:8180:0000"
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

  WITH_DB=true
  if [ "$dbPort" = "0000" ]; then
    WITH_DB=false
  fi

  SERVICE_VERSION=$(grep '^appVersion:' "$SERVICES_DIR/$service/charts/service/Chart.yaml" | sed -E 's/^appVersion:[[:space:]]*//; s/^"//; s/"$//')
  SERVICE_IMAGE="ghcr.io/tvoe-zdorovje/ams/$service-service:$SERVICE_VERSION"
  echo ""
  echo "📥 load $SERVICE_IMAGE to the minikube"
  minikube image load "$SERVICE_IMAGE" # in order to speed up service startup

  if [ "$WITH_DB" = true ]; then
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

    cd "$SERVICES_DIR"
  fi

  echo ""
  echo "== 📥 Install [ $service ] Service =="

  cd "./$service/charts/service"
  helm dependency update
  if [ "$WITH_DB" = true ]; then
    cd "../liquibase"
    helm dependency update
  fi
  cd ../..
  helm dependency update

  RELEASE_NAME="$service-service"
  helm install "$RELEASE_NAME" . -n "$NAMESPACE" --timeout 30m

  echo ":: Port forward for $service: $servicePort -> 8080"
  kubectl port-forward -n services "service/$service-service" "$servicePort:8080" &
  if [ "$WITH_DB" = true ]; then
    echo ":: Port forward for $service: $dbPort:5432"
    kubectl port-forward -n services "service/$service-service-postgresql" "$dbPort:5432" &
  fi

  cd "$SERVICES_DIR"
done

echo ""
echo "== 📥 Install [ Gateway ] Service =="

SERVICE="gateway"
SERVICE_VERSION=$(grep '^appVersion:' "$SERVICES_DIR/$SERVICE/Chart.yaml" | sed -E 's/^appVersion:[[:space:]]*//; s/^"//; s/"$//')
SERVICE_IMAGE="ghcr.io/tvoe-zdorovje/ams/$SERVICE-service:$SERVICE_VERSION"
echo ""
echo "📥 load $SERVICE_IMAGE to the minikube"
minikube image load "$SERVICE_IMAGE" # in order to speed up service startup

SERVICE_IMAGE="ghcr.io/apollographql/router:v2.2.0"
echo ""
echo "📥 load $SERVICE_IMAGE to the minikube"
minikube image load "$SERVICE_IMAGE" # in order to speed up service startup

echo ""
echo " 📥 Mount graphql directory"
nohup minikube mount "$PROJECT_DIR/$SERVICE/apollo/router/build/":"/gateway/router/build/" &

cd "$SERVICE"
helm dependency update

RELEASE_NAME="$SERVICE"
helm install "$RELEASE_NAME" . -n "$NAMESPACE" --timeout 30m

cd "$SCRIPT_DIR"

echo ""
echo "=== 🧑🏻‍🔧💬 Cluster bootstrap finished ==="

kubectl get pods -A

# kubectl port-forward pod/user-service-postgresql-0 5432:5432 -n services

