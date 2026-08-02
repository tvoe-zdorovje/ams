curl -X POST -H "Content-Type: application/json" \
     -d @user-events_administration-db_sink-connector_config.json \
     http://localhost:9083/connectors

curl -X POST -H "Content-Type: application/json" \
     -d @user-events_user-db_source-connector_config.json \
     http://localhost:9083/connectors
