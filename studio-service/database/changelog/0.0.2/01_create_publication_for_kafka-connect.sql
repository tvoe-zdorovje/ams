CREATE PUBLICATION studio_outbox_publication FOR TABLE studios.studio, debezium.heartbeat_table;
