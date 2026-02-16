CREATE PUBLICATION brand_outbox_publication FOR TABLE brands.brand, debezium.heartbeat_table;
