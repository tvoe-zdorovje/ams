#!/usr/bin/env bash
set -euo pipefail

SERVICES=(
  administration
  appointment
  auth
  brand
  gateway
  studio
  user
)

TAG_LATEST=false
PUSH_IMAGES=false
LIQUIBASE=false
SERVICES_FROM_ARGS=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --latest)
      TAG_LATEST=true
      shift
      ;;
    --push)
      PUSH_IMAGES=true
      shift
      ;;
    --liquibase)
      LIQUIBASE=true
      shift
      ;;
    --services)
      SERVICES_FROM_ARGS="$2"
      shift 2
      ;;
    *)
      echo "Unknown arg: $1"
      shift
      ;;
  esac
done

if [[ -n "$SERVICES_FROM_ARGS" ]]; then
  IFS=',' read -r -a SERVICES <<< "$SERVICES_FROM_ARGS"
fi

echo "Services: ${SERVICES[@]}"
echo "TAG_LATEST=$TAG_LATEST, PUSH_IMAGES=$PUSH_IMAGES, LIQUIBASE=$LIQUIBASE"

if command -v readlink >/dev/null && readlink -f "$0" >/dev/null 2>&1; then
  SCRIPT_PATH="$(readlink -f "$0")"
else
  SCRIPT_PATH="$0"
fi

PROJECT_DIR="$(cd "$(dirname "$SCRIPT_PATH")" && pwd)"

ALL_IMAGES=()

for service in "${SERVICES[@]}"; do
  echo ""
  echo "♨️  Build $service-service image"
  if [ "$service" = "gateway" ]; then
    TASK="gateway:gateway-service:bootBuildImage"
  else
    TASK=":$service-service:bootBuildImage"
  fi

  ./gradlew "$TASK"
  OUTPUT=$(./gradlew "$TASK" 2>&1 | tee /dev/tty)

  SUCCESS_LABEL="Successfully built image"
  SERVICE_IMAGE_NAME=$(echo "$OUTPUT" | grep -o "$SUCCESS_LABEL '[^']*'" | tail -1 | sed "s/.*'\(.*\)'.*/\1/")

  if [[ -z "$SERVICE_IMAGE_NAME" ]]; then
      echo "💔  Something went wrong"
      exit 1
  fi


  IMAGES=(
    "$SERVICE_IMAGE_NAME"
  )

  BUILD_LIQUIBASE_FOR_SERVICE=$LIQUIBASE
  if [[ "$service" = "gateway" || "$service" = "auth" ]]; then
    BUILD_LIQUIBASE_FOR_SERVICE=false
  fi
  if [[ "$BUILD_LIQUIBASE_FOR_SERVICE" = true ]]; then
    cd "$PROJECT_DIR/$service-service/database"
    LIQUIBASE_IMAGE_VERSION=$(grep -oE '[0-9]+\.[0-9]+\.[0-9]+' ./changelog/changelog.yaml | tail -1)
    LIQUIBASE_IMAGE_NAME="${SERVICE_IMAGE_NAME%:*}-liquibase:$LIQUIBASE_IMAGE_VERSION"

    echo ""
    echo "♨️  Build $LIQUIBASE_IMAGE_NAME"

    docker build -f ./liquibase.Dockerfile -t "$LIQUIBASE_IMAGE_NAME" .
    IMAGES+=("$LIQUIBASE_IMAGE_NAME")
    cd "$PROJECT_DIR"
  fi

  for image in "${IMAGES[@]}"; do
    if [ "$TAG_LATEST" = true ]; then
      LATEST_IMAGE_NAME="${image%:*}:latest"
      docker tag "$image" "$LATEST_IMAGE_NAME"
      IMAGES+=("$LATEST_IMAGE_NAME")
    fi
  done

  echo ""
  echo "✅ Successfully built images:"
    for image in "${IMAGES[@]}"; do
      echo "↘️ $image"
      ALL_IMAGES+=("$image")
    done

  cd "$PROJECT_DIR"
done

echo ""
  echo "✅ Successfully built images:"
    for image in "${ALL_IMAGES[@]}"; do
      echo "↘️ $image"
done
if [ "$PUSH_IMAGES" = true ]; then
    for image in "${ALL_IMAGES[@]}"; do
      echo ""
      echo "🌬 Push image: $image"

      docker push "$image"
    done
fi
