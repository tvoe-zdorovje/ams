#!/bin/sh

echo '🔧 Composing Supergraph...'

#for service in administration appointment brand studio user; do
#  ext=".graphqls"
#  sourceDir="/tmp/app/schemas/service"
#  output="/tmp/app/schemas/$service$ext"
#
#  find "$sourceDir/common" -type f -name "*$ext" -exec cat {} + > "$output"
#  find "$sourceDir/$service" -type f -name "*$ext" -exec cat {} + >> "$output"
#done

# merges all .graphqls files in the schemas directory into one file in reverse order for each service
for service in administration appointment auth brand studio user; do
  ext=".graphqls"
  sourceDir="/tmp/app/schemas/service"
  output="/tmp/app/schemas/$service$ext"

  # Очищаем файл вывода
  > "$output"

  # Функция для добавления файлов с комментариями и разделителями
  append_files() {
    dir=$1
    if [ -d "$dir" ]; then
      for file in $(find "$dir" -type f -name "*$ext" | sort -r); do
        echo "# >>> from ${file}" >> "$output"
        cat "$file" >> "$output"
        echo "\n" >> "$output"
      done
    fi
  }

  append_files "$sourceDir/common"
  append_files "$sourceDir/$service"
done

/rover supergraph compose \
  --elv2-license accept \
  --config /tmp/app/supergraph-config.yaml > /tmp/app/supergraph.graphql
