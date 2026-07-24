#!/bin/bash

set -e

if command -v readlink >/dev/null && readlink -f "$0" >/dev/null 2>&1; then
    SCRIPT_PATH="$(readlink -f "$0")"
else
    SCRIPT_PATH="$0"
fi
WORK_DIR="$(cd "$(dirname "$SCRIPT_PATH")" && pwd)"
BUILD_DIR="$WORK_DIR/build"
CONFIG_PATH="config/router.yaml"
SCHEMA_CONFIG_PATH="schema/supergraph.yaml"
SCHEMA_PATH="$BUILD_DIR/schema/supergraph.graphql"
PROJECT_DIR="${WORK_DIR}/../../../"
SOURCE_DIR="$PROJECT_DIR/graphql"

echo "work directory: $WORK_DIR"
echo "source directory: $SOURCE_DIR"

mkdir -p "$BUILD_DIR/config"
mkdir -p "$BUILD_DIR/schema"

echo ""
echo '🔧 Composing Supergraph...'

# merges all .graphqls files in the schemas directory into one file in reverse order for each service
TMP_SCHEMAS_DIR="$WORK_DIR/schema/schemas"
mkdir "$TMP_SCHEMAS_DIR"
for service in administration appointment auth brand studio user; do
  ext=".graphqls"
  sourceDir="$SOURCE_DIR"
  output="$TMP_SCHEMAS_DIR/$service$ext"

  echo ""
  echo "Compose $output"

  # clear the output file
  > "$output"

  # Write gql files
  append_files() {
    dir=$1
    if [ -d "$dir" ]; then
      for file in $(find "$dir" -type f -name "*$ext" | sort -r); do
        echo "# >>> from ${file}" >> "$output"
        cat "$file" >> "$output"
        echo "" >> "$output"
      done
    fi
  }

  append_files "$sourceDir/common"
  append_files "$sourceDir/$service"
done

echo ""
echo "Rover compose $SCHEMA_CONFIG_PATH"

rover supergraph compose \
  --elv2-license accept \
  --config "$SCHEMA_CONFIG_PATH" > "$SCHEMA_PATH"

rm -rf "$TMP_SCHEMAS_DIR"

echo ""
echo "Copy $BUILD_DIR/$CONFIG_PATH"
cp "$CONFIG_PATH" "$BUILD_DIR/$CONFIG_PATH"