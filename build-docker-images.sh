#!/usr/bin/env bash
set -euo pipefail

TAG_LATEST=false
PUSH_IMAGES=false
LIQUIBASE=false

for arg in "$@"; do
  if [ "$arg" = "--latest" ]; then
    TAG_LATEST=true
  fi
  if [ "$arg" = "--push" ]; then
    PUSH_IMAGES=true
  fi
  if [ "$arg" = "--liquibase" ]; then
    LIQUIBASE=true
  fi
done

if command -v readlink >/dev/null && readlink -f "$0" >/dev/null 2>&1; then
  SCRIPT_PATH="$(readlink -f "$0")"
else
  SCRIPT_PATH="$0"
fi

PROJECT_DIR="$(cd "$(dirname "$SCRIPT_PATH")" && pwd)"

SERVICES=(
  administration
  appointment
  auth
  brand
  gateway
  studio
  user
)

ALL_IMAGES=()

for service in "${SERVICES[@]}"; do
  echo ""
  echo "♨️  Build $service-service image"
  if [ "$service" = "gateway" ]; then
    # TODO
    echo ""
    echo "💔 Gateway service is not supported yet"
    continue
  else
    ./gradlew ":$service-service:bootBuildImage"

      OUTPUT=$(./gradlew ":$service:bootBuildImage" 2>&1 | tee /dev/tty)

      SUCCESS_LABEL="Successfully built image"
      SERVICE_IMAGE_NAME=$(echo "$OUTPUT" | grep -o "$SUCCESS_LABEL '[^']*'" | tail -1 | sed "s/.*'\(.*\)'.*/\1/")

      if [[ -z "$SERVICE_IMAGE_NAME" ]]; then
          echo "💔  Something went wrong"
          exit 1
      fi
  fi


  IMAGES=(
    "$SERVICE_IMAGE_NAME"
  )

  if [[ "$LIQUIBASE" = true && "$service" != "auth" ]]; then
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
